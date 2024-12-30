package com.example.FinalProject.controller.notice;

import com.example.FinalProject.dto.NoticeDTO;
import com.example.FinalProject.entity.company.Company;
import com.example.FinalProject.entity.file.File;
import com.example.FinalProject.entity.notice.Notice;
import com.example.FinalProject.entity.work.Work;
import com.example.FinalProject.repository.notice.NoticeRepository2;
import com.example.FinalProject.service.notice.FileStorageService;
import com.example.FinalProject.service.notice.NoticeService2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notice")
public class NoticeController2 {

    private final NoticeRepository2 noticeRepository2;
    private final NoticeService2 noticeService2;
    private final FileStorageService fileStorageService;

    public NoticeController2(NoticeRepository2 noticeRepository2, NoticeService2 noticeService2, FileStorageService fileStorageService){
        this.noticeRepository2 = noticeRepository2;
        this.noticeService2 = noticeService2;
        this.fileStorageService = fileStorageService;
    }
    /* 조회 */
    @GetMapping("getNoticeList")
    public ResponseEntity<Page<Notice>>getNoticeList(@RequestParam String userId, @RequestParam Integer companyId, @RequestParam String type, @RequestParam String tab, @RequestParam Integer page){
        return new ResponseEntity<>(noticeService2.getList(userId, companyId,type, tab,page),HttpStatus.OK);
    }

    /* 공지사항 생성하기 */
    @PostMapping("/create")
    public ResponseEntity<String> createNotice(
            @RequestParam("userId") String userId,
            @RequestParam("userName") String userName,
            @RequestParam("companyId") Integer companyId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("type") String type,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "files", required = false) List<MultipartFile> files
    ) {
        // Notice 객체 생성
        Notice notice = new Notice();
        notice.setTitle(title);
        notice.setContent(content);
        notice.setType(type);
        notice.setStatus("VISIBLE");
        notice.setUserId(userId);
        notice.setUserName(userName);
        notice.setCompanyId(companyId);


        try {
            // 이미지 업로드 처리
            if (image != null && !image.isEmpty()) {
                String imagePath = fileStorageService.storeFile(image);
                File imageFile = new File();
                imageFile.setFilePath(imagePath);
                imageFile.setFileType(image.getContentType());
                notice.addFile(imageFile);
            }

            // 일반 파일 업로드 처리
            if (files != null && !files.isEmpty()) {
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        String filePath = fileStorageService.storeFile(file);
                        File uploadedFile = new File();
                        uploadedFile.setFilePath(filePath);
                        uploadedFile.setFileType(file.getContentType());
                        notice.addFile(uploadedFile);
                    }
                }
            }
            // 공지사항 저장
            noticeRepository2.save(notice);
            return new ResponseEntity<String>(HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    /* 공지사항 삭제 */
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteNotices(@RequestBody List<Integer> noticeIds, @RequestParam Integer companyId) {
        noticeService2.deleteNotices(noticeIds, companyId);
        return ResponseEntity.noContent().build();
    }
    /* 특정 ID의 공지사항 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<Notice> getNoticeById(@PathVariable Integer id) {
        Optional<Notice> notice = noticeRepository2.findById(id);
        if(notice.isPresent()){
            return new ResponseEntity<>(notice.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
