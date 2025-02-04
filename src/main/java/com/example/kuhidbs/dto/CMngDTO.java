package com.example.kuhidbs.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CMngDTO {

    private String companyId;
    private String evalGrade;
    private String businessProgress1;
    private String businessProgress2;
    private String businessProgress3;
    private String businessProgress4;
    private String businessProgress5;
    private String managementStatus1;
    private String managementStatus2;
    private String exitPlan;
    private String exitEstimation;
}
