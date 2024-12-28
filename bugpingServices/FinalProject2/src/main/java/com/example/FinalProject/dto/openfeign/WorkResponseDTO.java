package com.example.FinalProject.dto.openfeign;

import lombok.Data;
import java.time.LocalDate;

@Data
public class WorkResponseDTO {
    private Integer workId;

    private CompanyDTO company;
    private UserDTO user;

    private LocalDate hireDate;
    private LocalDate resignDate;

    @Data
    public static class CompanyDTO {
        private Integer companyId;
        private String name;
    }

    @Data
    public static class UserDTO {
        private String userId;
        private String name;
    }
}