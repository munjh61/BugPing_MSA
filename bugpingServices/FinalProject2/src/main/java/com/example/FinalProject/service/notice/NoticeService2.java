package com.example.FinalProject.service.notice;

import com.example.FinalProject.controller.openfeign.RemoteWorkService;
import com.example.FinalProject.entity.file.File;
import com.example.FinalProject.entity.notice.Notice;
import com.example.FinalProject.repository.notice.FileRepository;
import com.example.FinalProject.repository.notice.NoticeRepository2;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class NoticeService2 {
    private final NoticeRepository2 noticeRepository2;
    private final FileRepository fileRepository;
    private final FileStorageService fileStorageService;
    private final ObjectMapper objectMapper;
    private final RemoteWorkService remoteWorkService;

    @Autowired
    public NoticeService2(NoticeRepository2 noticeRepository2, FileRepository fileRepository, FileStorageService fileStorageService, ObjectMapper objectMapper, RemoteWorkService remoteWorkService) {
        this.noticeRepository2 = noticeRepository2;
        this.fileRepository = fileRepository;
        this.fileStorageService = fileStorageService;
        this.objectMapper = objectMapper;
        this.remoteWorkService = remoteWorkService;
    }
    //출력
    public Page<Notice> getList(String userId, Integer companyId, String type, String tab, Integer page){
        PageRequest pageRequest = PageRequest.of(page, 10);
        if(tab.equals("작성자전체")){
            return noticeRepository2.findByCompanyIdAndType(companyId,type,pageRequest);
        }

        return noticeRepository2.findByCompanyIdAndTypeAndUserId(companyId,type,userId,pageRequest);
    }
    //삭제
    public void deleteNotices(List<Integer> noticeIds, Integer companyId) {
        for (Integer noticeId : noticeIds) {
            Notice notice = noticeRepository2.findById(noticeId)
                    .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다: " + noticeId));

            // Notice와 연결된 파일 삭제
            for (File file : notice.getFiles()) {
                fileStorageService.deleteFile(file.getFilePath());
            }
            notice.getFiles().clear();
            noticeRepository2.delete(notice);
        }
    }

}
