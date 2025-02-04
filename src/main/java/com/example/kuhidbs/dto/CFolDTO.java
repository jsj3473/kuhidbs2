package com.example.kuhidbs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CFolDTO {

    private String companyId; // 회사 고유 번호
    private String followupStartDate; // 투자 일자
    private String followupCompanyName; // 투자 기관
    private String followupProduct; // 투자 상품
    private Long followupSumPrice; // 투자 금액
    private Long followupShareCount; // 주식 수량
    private Long followupUnitPrice; // 투자 단가
    private BigDecimal followupEquityRate; // 지분율
    private Long followupInvestmentValue; // 투자 밸류
    private String followupStep; // 투자 단계
}
