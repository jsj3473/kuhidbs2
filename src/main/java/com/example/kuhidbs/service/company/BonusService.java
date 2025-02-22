package com.example.kuhidbs.service.company;

import com.example.kuhidbs.dto.company.CBonusDTO;
import com.example.kuhidbs.entity.company.Account;
import com.example.kuhidbs.entity.company.Bonus;
import com.example.kuhidbs.entity.company.Investment;
import com.example.kuhidbs.repository.company.AccountRepository;
import com.example.kuhidbs.repository.company.BonusRepository;
import com.example.kuhidbs.repository.company.InvestmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BonusService {

    private final BonusRepository bonusRepository;
    private final InvestmentRepository investmentRepository;
    private final AccountRepository accountRepository;

    // 무상증자 생성
    @Transactional
    public void createBonus(CBonusDTO dto) {
        // 투자 고유 번호로 Ivt 객체 조회
        Investment investment = investmentRepository.findById(dto.getInvestmentId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid investment ID: " + dto.getInvestmentId()));
        // DTO → Entity 변환 후 저장
        Bonus bonus = Bonus.builder()
                .investment(investment)
                .changedShareCount(dto.getChangedShareCount())
                .unitPrice(dto.getUnitPrice())
                .build();

        bonusRepository.save(bonus);

        // 최신 계좌 데이터 조회
        Account latestAccount = accountRepository.findTop1ByInvestmentInvestmentIdOrderByAccountIdDesc(dto.getInvestmentId());

        // 일부 컬럼 수정
        Account updatedAccount = Account.builder()
                .investment(latestAccount.getInvestment()) // 기존 투자 엔터티 유지
                .unitPrice(dto.getUnitPrice()) // 새로운 주당 가치로 업데이트
                .heldShareCount(dto.getChangedShareCount()) // 기존 보유 주식 수 유지
                .totalPrincipal(latestAccount.getTotalPrincipal()) // 기존 투자 원금 유지
                .functionType("무증") // 실행 함수 업데이트(감액 or 복원)
                .kuhEquityRate(latestAccount.getKuhEquityRate()) // 기존 KUH 지분율 유지
                .build();

        accountRepository.save(updatedAccount);
    }
}
