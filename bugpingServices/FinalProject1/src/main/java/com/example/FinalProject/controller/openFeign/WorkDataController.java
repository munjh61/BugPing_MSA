package com.example.FinalProject.controller.openFeign;

import com.example.FinalProject.entity.work.Work;
import com.example.FinalProject.repository.work.WorkRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/connect/work")
public class WorkDataController {

    @Resource
    private WorkRepository workRepository;

    @GetMapping("/{workId}")
    public Work getWorkDetails(@PathVariable Integer workId) {
        log.info("Fetching work details for Work ID: {}", workId);

        // Work 엔티티 조회
        return workRepository.findById(workId)
                .orElseThrow(() -> new IllegalArgumentException("Work ID " + workId + " not found"));
    }
}
