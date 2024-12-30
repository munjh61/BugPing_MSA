package com.example.FinalProject.repository.notice;

import com.example.FinalProject.entity.notice.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository2 extends JpaRepository<Notice, Integer> {
    Page<Notice>findByCompanyIdAndType(Integer workId, String type, Pageable pageable);

    Page<Notice>findByCompanyIdAndTypeAndUserId(Integer workId, String type, String userId, Pageable pageable);
}
