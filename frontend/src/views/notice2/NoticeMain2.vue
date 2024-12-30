<template>
    <div class="main-wrapper">
        <div class="content-wrapper">
            <div class="notice-main">
            <!-- 탭 섹션 -->
            <div class="tabs">
                <button
                class="tab"
                :class="{ active: tab === '작성자전체' }"
                @click="setActiveTab('작성자전체')"
                >
                전체 게시물
                </button>
                <button
                class="tab"
                :class="{ active: tab === '작성자자신' }"
                @click="setActiveTab('작성자자신')"
                >
                내 게시물
                </button>
            </div>

            <!-- 카테고리 및 공지 작성 버튼 섹션 -->
            <div class="categories-container">
                <div class="categories">
                <button
                    class="category"
                    :class="{ active: category === '공지' }"
                    @click="setActiveCategory('공지')"
                >
                    <img
                    src="@/assets/noticeimg/megaphone.png"
                    alt="공지 아이콘"
                    class="category-icon"
                    />
                    공지
                </button>
                <button
                    class="category"
                    :class="{ active: category === '매뉴얼' }"
                    @click="setActiveCategory('매뉴얼')"
                >
                    <img
                    src="@/assets/noticeimg/checklist.png"
                    alt="매뉴얼 아이콘"
                    class="category-icon"
                    />
                    매뉴얼
                </button>
                <button
                    class="category"
                    :class="{ active: category === '특이사항' }"
                    @click="setActiveCategory('특이사항')"
                >
                    <img
                    src="@/assets/noticeimg/question-mark.png"
                    alt="특이사항 아이콘"
                    class="category-icon"
                    />
                    특이사항
                </button>
                </div>
                <button class="create-notice-button" @click="createNotice">공지 작성하기</button>
            </div>

            <!-- 공지사항 목록 테이블 -->
            <table class="board-table">
                <thead>
                <tr>
                    <th>
                    <div class="checkbox-action">
                        <button @click="deleteSelected" class="delete-button">
                        삭제
                        </button>
                        <input type="checkbox" @click="toggleAll" />
                    </div>
                    </th>
                    <th>No.</th>
                    <th>제목</th>
                    <th>작성자</th>
                    <th>보기권한</th>
                    <th>작성 날짜</th>
                    <th>상태</th>
                </tr>
                </thead>
                <tbody>
                <tr
                    v-for="(item, index) in notices"
                    :key="item.noticeId"
                    @click="goToDetail(item.noticeId)"
                    class="clickable-row"
                >
                    <td class="checkbox-cell">
                    <input
                        type="checkbox"
                        v-model="selectedItems"
                        :value="item.noticeId"
                        @click.stop
                    />
                    </td>
                    <td>{{ (currentPage - 1) * 10 + index + 1 }}</td>
                    <td>{{ item.title }}</td>
                    <td>{{ item.author || "작성자 없음" }}</td>
                    <td>{{ item.viewers || "제한 없음" }}</td>
                    <td>{{ formatDate(item.createdAt) }}</td>
                    <td>{{ getStatusLabel(item.status) }}</td>
                </tr>
                <tr v-if="notices.length === 0">
                    <td colspan="7">게시물이 없습니다.</td>
                </tr>
                </tbody>
            </table>

            <!-- 페이징 섹션 -->
            <div class="pagination">
                <button @click="prevPage" :disabled="currentPage === 1">&lt;</button>
                <span
                v-for="pageNum in totalPages"
                :key="pageNum"
                :class="{ active: currentPage === pageNum }"
                @click="goToPage(pageNum)"
                >
                {{ pageNum }}
                </span>
                <button @click="nextPage" :disabled="currentPage === totalPages">
                &gt;
                </button>
            </div>
            </div>
        </div>
    </div>
</template>
<script setup>
import router from '@/router';
import { axiosAddress } from '@/stores/axiosAddress';
import { useUserStore } from '@/stores/userStore';
import axios from 'axios';
import { reactive, ref } from 'vue';

// 카테고리 & 탭
const tab = ref('작성자전체');
const setActiveTab = (e) => {
    tab.value = e;
}
const category = ref("전체");
const setActiveCategory = (e) => {
    category.value = e;
}

// 페이지 이동
const page = ref(0);
const totalPages = ref(0);
const currentPage = ref(1); // 현재 페이지
const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--;
    getData(); // 페이지 이동 후 데이터 갱신
  }
};
const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++;
    getData(); // 페이지 이동 후 데이터 갱신
  }
};

// 글 조회
const userStore = useUserStore()
const companyId = userStore.company.companyId;
const userId = userStore.userId;
const notices = reactive([]); // 공지사항 데이터 리스트
const getData = async () => {
    try {
        // Axios 요청
        const res = await axios.get(`${axiosAddress}/getNoticeList`, {
            params: {
                companyId: companyId,
                tab: tab.value,
                userId: userId,
                type: category.value,
                page: currentPage.value - 1 // 0부터 시작하는 페이지 설정
            }
        });

        // 데이터 처리
        notices.splice(0, notices.length, ...res.data.content); // 반응형 배열에 데이터 업데이트
        totalPages.value = res.data.totalPages; // 총 페이지 수 업데이트
    } catch (error) {
        console.error("데이터를 불러오는 중 오류가 발생했습니다:", error);
    }
};
const createNotice = () => {
  router.push({name: "noticeCreate2"})
}
</script>
<style scoped>
/* 전체 레이아웃 스타일 */
.main-wrapper {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding: 20px;
  background-color: #f4f4f4;
}

.content-wrapper {
  width: 100%;
  max-width: 1200px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

/* 탭 섹션 */
.tabs {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.tab {
  padding: 10px 20px;
  border: 1px solid #ddd;
  background-color: #f9f9f9;
  cursor: pointer;
  border-radius: 4px;
  transition: background-color 0.3s ease, color 0.3s ease;
}

.tab.active {
  background-color: #007bff;
  color: #fff;
}

.tab:hover {
  background-color: #e0e0e0;
}

/* 카테고리 및 공지 작성 버튼 섹션 */
.categories-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.categories {
  display: flex;
  gap: 10px;
}

.category {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: #f9f9f9;
  cursor: pointer;
  transition: background-color 0.3s ease, color 0.3s ease;
}

.category.active {
  background-color: #28a745;
  color: white;
}

.category:hover {
  background-color: #e0e0e0;
}

.category-icon {
  width: 20px;
  height: 20px;
}

/* 공지 작성 버튼 */
.create-notice-button {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  background-color: #007bff;
  color: white;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.create-notice-button:hover {
  background-color: #0056b3;
}

/* 테이블 스타일 */
.board-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
  background-color: #fff;
}

.board-table th,
.board-table td {
  text-align: center;
  padding: 10px;
  border-bottom: 1px solid #ddd;
}

.board-table th {
  background-color: #f9f9f9;
}

.board-table .checkbox-action {
  display: flex;
  align-items: center;
  gap: 5px;
}

.checkbox-cell {
  display: flex;
  justify-content: center;
  align-items: center;
}

.delete-button {
  padding: 5px 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: #f5f5f5;
  cursor: pointer;
  transition: background-color 0.3s ease, color 0.3s ease;
}

.delete-button:hover {
  background-color: #007bff;
  color: white;
}

.clickable-row {
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.clickable-row:hover {
  background-color: #f4f4f4;
}

.board-table td {
  vertical-align: middle;
}

/* 페이징 스타일 */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
  gap: 5px;
}

.pagination button {
  padding: 5px 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: #f9f9f9;
  cursor: pointer;
  transition: background-color 0.3s ease, color 0.3s ease;
}

.pagination button:hover:not(:disabled) {
  background-color: #e0e0e0;
}

.pagination button:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

.pagination span {
  padding: 5px 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.pagination span:hover {
  background-color: #e0e0e0;
}

.pagination span.active {
  background-color: #007bff;
  color: white;
  font-weight: bold;
}

</style>