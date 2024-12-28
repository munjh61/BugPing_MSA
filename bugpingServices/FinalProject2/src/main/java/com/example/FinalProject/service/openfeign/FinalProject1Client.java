package com.example.FinalProject.service.openfeign;

import com.example.FinalProject.dto.openfeign.WorkResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "FinalProject1") // 호출할 서비스 이름
public interface FinalProject1Client {

    @GetMapping("/api/connect/work/{workId}") // 다른 마이크로서비스의 엔드포인트
    WorkResponseDTO getWorkDetails(@PathVariable Integer workId);
}
