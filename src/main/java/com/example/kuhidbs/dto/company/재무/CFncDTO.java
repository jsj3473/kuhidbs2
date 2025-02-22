package com.example.kuhidbs.dto.company.재무;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CFncDTO {

    private String companyId; // Company 객체 대신 ID만 포함
    private Integer financialYear;
    private Integer financialHalf;
    private Integer revenue;
    private Integer operatingProfit;
    private Integer netIncome;
    private Integer totalAssets;
    private Integer totalCapital;
    private Integer capital;
    private Integer employeeCount;
    private Integer totalDebt;
}
