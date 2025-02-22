package com.example.kuhidbs.service.company;

import com.example.kuhidbs.dto.company.CShrupDTO;
import com.example.kuhidbs.entity.company.Account;
import com.example.kuhidbs.entity.company.Investment;
import com.example.kuhidbs.entity.company.ShareUpdate;
import com.example.kuhidbs.repository.company.AccountRepository;
import com.example.kuhidbs.repository.company.InvestmentRepository;
import com.example.kuhidbs.repository.company.ShrupRepository;
import org.springframework.stereotype.Service;

@Service
public class ShrupService {

    private final ShrupRepository shrupRepository;
    private final InvestmentRepository investmentRepository;
    private final AccountRepository accountRepository;

    public ShrupService(ShrupRepository shrupRepository, InvestmentRepository investmentRepository, AccountRepository accountRepository) {
        this.shrupRepository = shrupRepository;
        this.investmentRepository = investmentRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * 감액/복원 데이터 저장
     */
    public ShareUpdate saveShrup(CShrupDTO shrupDTO) {
        // 투자 고유 번호로 Ivt 객체 조회
        Investment investment = investmentRepository.findById(shrupDTO.getInvestmentId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid investment ID: " + shrupDTO.getInvestmentId()));

        // DTO를 엔터티로 변환
        ShareUpdate shareUpdate = ShareUpdate.builder()
                .investment(investment)
                .shareUpdateDate(shrupDTO.getShareUpdateDate())
                .shareUnitValue(shrupDTO.getShareUnitValue())
                .shareUpdateType(shrupDTO.getShareUpdateType())
                .shareUpdateReason(shrupDTO.getShareUpdateReason())
                .shareUpdateAction(shrupDTO.getShareUpdateAction())
                .build();
        ShareUpdate savedShareUpdate = shrupRepository.save(shareUpdate);

        // 최신 계좌 데이터 조회
        Account latestAccount = accountRepository.findTop1ByInvestmentInvestmentIdOrderByAccountIdDesc(shrupDTO.getInvestmentId());

        // 일부 컬럼 수정
        Account updatedAccount = Account.builder()
                .investment(latestAccount.getInvestment()) // 기존 투자 엔터티 유지
                .unitPrice(shrupDTO.getShareUnitValue()) // 새로운 주당 가치로 업데이트
                .heldShareCount(latestAccount.getHeldShareCount()) // 기존 보유 주식 수 유지
                .totalPrincipal(shrupDTO.getShareUnitValue()*latestAccount.getHeldShareCount()) // 기존 투자 원금 유지
                .functionType(shrupDTO.getShareUpdateType()) // 실행 함수 업데이트(감액 or 복원)
                .kuhEquityRate(latestAccount.getKuhEquityRate()) // 기존 KUH 지분율 유지
                .build();

        accountRepository.save(updatedAccount);
        return savedShareUpdate;
    }


}
