package com.example.FinalProject.controller.openFeign;

import com.example.FinalProject.entity.work.Work;
import com.example.FinalProject.repository.work.WorkRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/connect/work")
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
    @GetMapping("getWork")
    public Work getWork(@RequestParam String userId, @RequestParam Integer companyId){
        return workRepository.findTopByUser_userIdAndCompany_CompanyIdOrderByHireDateDesc(userId,companyId)
                .orElseThrow(() -> new IllegalArgumentException("Work not found"));
    }
}
