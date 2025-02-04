package com.example.kuhidbs.service;

import com.example.kuhidbs.dto.CIvtDTO;
import com.example.kuhidbs.entity.Account;
import com.example.kuhidbs.entity.Company;
import com.example.kuhidbs.entity.Investment;
import com.example.kuhidbs.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvestmentService {

    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    private CmpRepository companyRepository;

    @Autowired
    private AccountRepository accountRepository;

    /**
     * 투자 정보를 저장하는 메서드.
     *
     * @param dto CIvtDTO 객체
     * @return 저장된 Investment 엔터티
     */
    public Investment saveInvestment(CIvtDTO dto) {

        // Company 객체 조회
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid company ID: " + dto.getCompanyId()));

        // DTO를 Ivt 엔터티로 변환
        Investment investment = toEntity(dto, company);

        // Ivt 엔터티 저장
        Investment savedInvestment = investmentRepository.save(investment);

        // Account 엔터티 생성 및 저장
        Account account = toAccountEntity(dto, savedInvestment);
        accountRepository.save(account);

        return savedInvestment;
    }


    /**
     * CIvtDTO를 Investment 엔터티로 변환하는 메서드.
     *
     * @param dto 변환할 DTO 객체
     * @return 변환된 Investment 엔터티
     */
    private Investment toEntity(CIvtDTO dto, Company company) {
        return Investment.builder()
                .fundId(dto.getFundId()) // 투자 재원
                .company(company) // 회사 고유 번호
                .investmentProduct(dto.getInvestmentProduct()) // 투자 상품
                .investmentSumPrice(dto.getInvestmentSumPrice()) // 투자 금액
                .shareCount(dto.getShareCount()) // 주식 수량
                .investmentUnitPrice(dto.getInvestmentUnitPrice()) // 투자 단가
                .equityRate(dto.getEquityRate()) // 지분율
                .investmentValue(dto.getInvestmentValue()) // 투자 밸류
                .tangibleInvestment(dto.getTangibleInvestment()) // 현물 투자
                .investmentStep(dto.getInvestmentStep()) // 투자 단계
                .investmentDate(dto.getInvestmentDate()) // 투자 일자
                .investmentEmployee(dto.getInvestmentEmployee()) // 투자 담당자
                .totalShares(dto.getTotalShares()) // 발행 주식 수
                .investmentState(dto.getInvestmentState()) // 투자 상태
                .investmentMemo(dto.getInvestmentMemo()) // 기타 메모
                .build();
    }

    private Account toAccountEntity(CIvtDTO dto, Investment savedInvestment) {
        return Account.builder()
                .investment(savedInvestment) // Ivt 엔터티 설정 (ManyToOne 관계)
                .unitPrice(dto.getInvestmentUnitPrice()) // 투자 단가
                .heldShareCount(dto.getShareCount()) // 보유 주식 수량
                .totalPrincipal(dto.getInvestmentSumPrice()) // 투자 원금 (투자 금액)
                .functionType("최초투자") // 실행 함수 (예: "SAVE_INVESTMENT")
                .kuhEquityRate(dto.getEquityRate()) // KUH 지분율
                .build();
    }


}
