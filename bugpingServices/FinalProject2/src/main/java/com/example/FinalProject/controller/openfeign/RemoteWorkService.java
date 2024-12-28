package com.example.FinalProject.controller.openfeign;

import com.example.FinalProject.dto.openfeign.WorkResponseDTO;
import com.example.FinalProject.entity.work.Work;
import com.example.FinalProject.service.openfeign.FinalProject1Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/connect/work")
public class RemoteWorkService {

    private final FinalProject1Client finalProject1Client;

    @Autowired
    public RemoteWorkService(FinalProject1Client finalProject1Client) {
        this.finalProject1Client = finalProject1Client;
    }

    @GetMapping("/{workId}")
    public WorkResponseDTO getWorkDetailsFromServer(@PathVariable Integer workId) {
        log.info("Fetching work details for Work ID: {}", workId);

        // OpenFeign 클라이언트를 사용해 다른 서비스 호출
        return finalProject1Client.getWorkDetails(workId);
    }
}

