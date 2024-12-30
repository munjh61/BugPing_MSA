<template>
    <div class="page-container">
        <div class="main-content">
        <!-- 공지사항 작성 페이지 UI -->
        <div class="notice-page">
            <div class="category-main">
            <h1>카테고리</h1>
            </div>

            <!-- 카테고리 선택 -->
            <div class="category-section">
            <button class="category-card" @click="selectCategory('공지')">
                <img src="@/assets/noticeimg/megaphone.png" alt="카테고리 아이콘" class="category-icon" />공지
            </button>
            </div>
            <div class="category-section">
            <button class="category-card" @click="selectCategory('매뉴얼')">
                <img src="@/assets/noticeimg/checklist.png" alt="카테고리 아이콘" class="category-icon" />매뉴얼
            </button>
            </div>
            <div class="category-section">
            <button class="category-card" @click="selectCategory('특이사항')">
                <img src="@/assets/noticeimg/question-mark.png" alt="카테고리 아이콘" class="category-icon" />특이사항
            </button>
            </div>

            <!-- 제목 입력 -->
            <div class="input-section">
            <label class="input-label" for="title">제목<span class="required">*</span></label>
            <input
                type="text"
                id="title"
                v-model="title"
                placeholder="입력하세요"
                class="input-field"
            />
            </div>

            <!-- 내용 입력 -->
            <div class="input-section">
            <label class="input-label" for="content">내용<span class="required">*</span></label>
            <textarea
                id="content"
                v-model="content"
                placeholder="입력하세요"
                class="textarea-field"
            ></textarea>
            </div>

            <!-- 이미지 및 파일 첨부 -->
            <div class="attachment-section">
            <div class="attachment-wrapper">
                <!-- 이미지 첨부 -->
                <div class="attachment-item">
                <div class="attachment-header">
                    <img src="@/assets/noticeimg/image.png" alt="공지 아이콘" class="attachment-img" />
                    <div class="attachment-text">이미지 첨부</div>
                </div>
                <div class="attachment-card1" @click="triggerFileUpload('image')">
                    <img src="@/assets/noticeimg/add.png" alt="업로드 아이콘" class="upload-icon" />
                </div>
                <!-- 숨겨진 이미지 입력창 -->
                <input
                    ref="imageInput"
                    type="file"
                    accept="image/*"
                    class="file-input-hidden"
                    @change="handleImageUpload"
                />
                <!-- 이미지 미리보기 -->
                <div v-if="previewImage" class="image-preview-container">
                    <img :src="previewImage" alt="미리보기" class="image-preview" />
                    <img
                    src="@/assets/noticeimg/remove.png"
                    alt="삭제 아이콘"
                    class="remove-icon"
                    @click="removePreviewImage"
                    />
                </div>
                </div>

                <!-- 구분선 -->
                <div class="divider-vertical"></div>

                <!-- 파일 첨부 -->
                <div class="attachment-item">
                <div class="attachment-header">
                    <img src="@/assets/noticeimg/file.png" alt="파일 아이콘" class="attachment-img" />
                    <div class="attachment-text">파일 첨부</div>
                </div>
                <div class="attachment-card2" @click="triggerFileUpload('file')">
                    <img src="@/assets/noticeimg/add.png" alt="업로드 아이콘" class="upload-icon" />
                </div>
                <!-- 숨겨진 파일 입력창 -->
                <input
                    ref="fileInput"
                    type="file"
                    multiple
                    class="file-input-hidden"
                    @change="handleFileUpload"
                />
                <!-- 업로드된 파일 목록 -->
                <ul v-if="uploadedFiles.length > 0" class="uploaded-files">
                    <li v-for="(file, index) in uploadedFiles" :key="index">
                    {{ file.name }}
                    <img
                        src="@/assets/noticeimg/remove.png"
                        alt="삭제 아이콘"
                        class="remove-icon"
                        @click="removePreviewFile(index)"
                    />
                    </li>
                </ul>
                </div>
            </div>
            </div>

            <!-- 버튼 섹션 -->
            <div class="button-section">
            <button @click="cancelNotice" class="cancel-button">취소</button>
            <button @click="submitNotice" class="submit-button">작성완료</button>
            </div>
        </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { axiosAddress } from '@/stores/axiosAddress';
import { useUserStore } from '@/stores/userStore';

const category = ref('공지');
const title = ref('');
const content = ref('');
const previewImage = ref(null);
const uploadedFiles = ref([]);
const userStore = useUserStore();
const userId = userStore.userId;
const userName = userStore.userName;
const companyId = userStore.company.companyId;

const selectCategory = (selectedCategory) => {
category.value = selectedCategory;
};

const handleImageUpload = (event) => {
const file = event.target.files[0];
if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
        previewImage.value = e.target.result;
    };
    reader.readAsDataURL(file);
    }
};

const handleFileUpload = (event) => {
const files = event.target.files;
    uploadedFiles.value = Array.from(files);
};

const removePreviewImage = () => {
    previewImage.value = null;
};

const removePreviewFile = (index) => {
    uploadedFiles.value.splice(index, 1);
};

const triggerFileUpload = (type) => {
    if (type === 'image') {
        document.querySelector('input[type="file"][accept="image/*"]').click();
    } else {
        document.querySelector('input[type="file"][multiple]').click();
    }
};

const submitNotice = async () => {
    try {
        const formData = new FormData();
        formData.append('userId', userId);
        formData.append('userName', userName);
        formData.append('companyId', companyId);
        formData.append('title', title.value);
        formData.append('content', content.value);
        formData.append('type', category.value);

        if (previewImage.value) {
        formData.append('image', previewImage.value);
        }

        uploadedFiles.value.forEach((file) => {
        formData.append('files', file);
        });

        const response = await axios.post(`${axiosAddress}/notice/create`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
        });

        alert('공지사항이 성공적으로 등록되었습니다.');
    } catch (error) {
        console.error('공지사항 등록 중 오류 발생:', error);
        alert('공지사항 등록에 실패했습니다.');
    }
};

    const cancelNotice = () => {
    if (confirm('작성을 취소하시겠습니까?')) {
        title.value = '';
        content.value = '';
        previewImage.value = null;
        uploadedFiles.value = [];
    }
};
</script>

<style scoped>

</style>