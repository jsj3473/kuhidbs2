package com.example.kuhidbs.service;

import com.example.kuhidbs.dto.CCmpInfDTO;
import com.example.kuhidbs.dto.CFncDTO;
import com.example.kuhidbs.entity.Company;
import com.example.kuhidbs.entity.Financial;
import com.example.kuhidbs.repository.CmpRepository;
import com.example.kuhidbs.repository.FinancialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FinancialService {

    @Autowired
    private FinancialRepository financialRepository;

    @Autowired
    private CmpRepository companyRepository;


    public void saveFinancial(CCmpInfDTO CCmpInfDTO) {

        // Company 객체 조회
        Company company = companyRepository.findById(CCmpInfDTO.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid company ID: " + CCmpInfDTO.getCompanyId()));
        Financial financial = Financial.builder()
                .company(company)
                .financialYear(CCmpInfDTO.getFinancialYear())
                .financialHalf(CCmpInfDTO.getFinancialHalf())
                .revenue(CCmpInfDTO.getRevenue())
                .operatingProfit(CCmpInfDTO.getOperatingProfit())
                .netIncome(CCmpInfDTO.getNetIncome())
                .totalAssets(CCmpInfDTO.getTotalAssets())
                .totalCapital(CCmpInfDTO.getTotalCapital())
                .capital(CCmpInfDTO.getCapital())
                .employeeCount(CCmpInfDTO.getEmployeeCount())
                .totalDebt(CCmpInfDTO.getTotalDebt())
                .build();

        financialRepository.save(financial);
    }

    public Financial saveFinancialForCFncDTO(CFncDTO dto) {

        // Company 객체 조회
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid company ID: " + dto.getCompanyId()));
        Financial financial = Financial.builder()
                .company(company)
                .financialYear(dto.getFinancialYear())
                .financialHalf(dto.getFinancialHalf())
                .revenue(dto.getRevenue())
                .operatingProfit(dto.getOperatingProfit())
                .netIncome(dto.getNetIncome())
                .totalAssets(dto.getTotalAssets())
                .totalCapital(dto.getTotalCapital())
                .capital(dto.getCapital())
                .employeeCount(dto.getEmployeeCount())
                .totalDebt(dto.getTotalDebt())
                .build();

        return financialRepository.save(financial);
    }
}
