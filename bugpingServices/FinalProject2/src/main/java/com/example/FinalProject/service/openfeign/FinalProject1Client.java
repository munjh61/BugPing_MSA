package com.example.FinalProject.service.openfeign;

import com.example.FinalProject.dto.openfeign.WorkResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "FinalProject1") // 호출할 서비스 이름
public interface FinalProject1Client {

    @GetMapping("/connect/work/{workId}") // 다른 마이크로서비스의 엔드포인트
    WorkResponseDTO getWorkDetails(@PathVariable Integer workId);

    @GetMapping("/connect/work/getWork")
    WorkResponseDTO getWorkFromClient(@RequestParam String userId, @RequestParam Integer companyId);
}
