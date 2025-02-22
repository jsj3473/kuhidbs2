package com.example.kuhidbs.service.company;

import com.example.kuhidbs.dto.company.kuh투자.RIvtDTO;
import com.example.kuhidbs.dto.company.기본정보.CCmpInfDTO;
import com.example.kuhidbs.dto.company.기본정보.RCmpInfDTO;
import com.example.kuhidbs.dto.company.사후관리.RMngDTO;
import com.example.kuhidbs.dto.company.재무.RFncDTO;
import com.example.kuhidbs.dto.company.주주명부.RShrDTO;
import com.example.kuhidbs.entity.company.Company;
import com.example.kuhidbs.entity.company.Client;
import com.example.kuhidbs.repository.company.ClientRepository;
import com.example.kuhidbs.repository.company.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final ClientRepository clientRepository;

    private final InvestmentService investmentService;
    private final FinancialService financialService;
    private final ShareholderService shareholderService;
    private final ManageService manageService;
    private final ReviewerService reviewerService;
    private final FollowupService followupService;

    public void saveCompany(CCmpInfDTO CCmpInfDTO) {

        Company company = Company.builder()
                .companyId(CCmpInfDTO.getCompanyId())
                .companyName(CCmpInfDTO.getCompanyName())
                .establishmentDate(CCmpInfDTO.getEstablishmentDate())
                .ceoName(CCmpInfDTO.getCeoName())
                .companyAddress(CCmpInfDTO.getCompanyAddress())
                .businessRegistrationNumber(CCmpInfDTO.getBusinessRegistrationNumber())
                .corporateRegistrationNumber(CCmpInfDTO.getCorporateRegistrationNumber())
                .industryCode(CCmpInfDTO.getIndustryCode())
                .capital(CCmpInfDTO.getCapital())
                .parValue(CCmpInfDTO.getParValue())
                .employeeCount(CCmpInfDTO.getEmployeeCount())
                .startupType(CCmpInfDTO.getStartupType())
                .regionalCompany(CCmpInfDTO.getRegionalCompany())
                .kuhStartup(CCmpInfDTO.getKuhStartup())
                .ventureRecognition(CCmpInfDTO.getVentureRecognition())
                .researchRecognition(CCmpInfDTO.getResearchRecognition())
                .earlyStartupType(CCmpInfDTO.getEarlyStartupType())
                .kuhSubsidiary(CCmpInfDTO.getKuhSubsidiary())
                .investmentSector(CCmpInfDTO.getInvestmentSector())
                .dueDiligence(CCmpInfDTO.getDueDiligence())
                .mainProducts(CCmpInfDTO.getMainProducts())
                .investmentPoint1(CCmpInfDTO.getInvestmentPoint1())
                .investmentPoint2(CCmpInfDTO.getInvestmentPoint2())
                .investmentPoint3(CCmpInfDTO.getInvestmentPoint3())
                .publicTechnologyTransfer(CCmpInfDTO.getPublicTechnologyTransfer())
                .smeStatus(CCmpInfDTO.getSmeStatus())
                .listingDate(CCmpInfDTO.getListingDate())
                .listingStatus(CCmpInfDTO.getListingStatus())
                .companyPostalCode(CCmpInfDTO.getCompanyPostalCode())
                .build();

        companyRepository.save(company);
    }

    @Transactional(readOnly = true)
    public RCmpInfDTO getCompanyInfo(String companyId) {
        // 1. 회사 기본 정보 조회
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + companyId));

        // 2. 최근 2개 재무제표 조회 (FinancialService 호출)
        List<RFncDTO> recentFinancials = financialService.getRecentFinancialsByCompanyId(companyId);

        // 3. 투자 내역 조회 (InvestmentService 호출)
        RIvtDTO rivtDTO = investmentService.getInvestmentByCompanyId(companyId);


        // 4. 주주명부 조회 (ShareholderService 호출)
        RShrDTO shrDTO = shareholderService.getShareholderByCompanyId(companyId);

        // 5. 사후관리 정보 조회 (ManageService 호출)
        RMngDTO rmngDTO = manageService.getManageByCompanyId(companyId);

        Optional<Client> clientOpt = clientRepository.findByCompany_CompanyId(companyId);
        Client client = clientOpt.orElse(null);


        // 6. 담당자 정보 조회 (최신 정보 가져오기)
        String latestManager1 = reviewerService.getLatestManagerByType(companyId, "발굴자");
        String latestManager2 = reviewerService.getLatestManagerByType(companyId, "심사자");
        String latestManager3 = reviewerService.getLatestManagerByType(companyId, "사후관리자");

        Long currentCompanyValue = followupService.getCurrentCompanyValue(companyId);


        // 7. DTO 생성 및 데이터 매핑
        return RCmpInfDTO.builder()
                .companyId(company.getCompanyId())
                .companyName(company.getCompanyName())
                .establishmentDate(company.getEstablishmentDate())
                .ceoName(company.getCeoName())
                .companyAddress(company.getCompanyAddress())
                .businessRegistrationNumber(company.getBusinessRegistrationNumber())
                .corporateRegistrationNumber(company.getCorporateRegistrationNumber())
                .industryCode(company.getIndustryCode())
                .capital(company.getCapital())
                .parValue(company.getParValue())
                .employeeCount(company.getEmployeeCount())
                .startupType(company.getStartupType())
                .regionalCompany(company.getRegionalCompany())
                .kuhStartup(company.getKuhStartup())
                .ventureRecognition(company.getVentureRecognition())
                .researchRecognition(company.getResearchRecognition())
                .earlyStartupType(company.getEarlyStartupType())
                .kuhSubsidiary(company.getKuhSubsidiary())
                .investmentSector(company.getInvestmentSector())
                .dueDiligence(company.getDueDiligence())
                .mainProducts(company.getMainProducts())
                .investmentPoint1(company.getInvestmentPoint1())
                .investmentPoint2(company.getInvestmentPoint2())
                .investmentPoint3(company.getInvestmentPoint3())
                .publicTechnologyTransfer(company.getPublicTechnologyTransfer())
                .smeStatus(company.getSmeStatus())
                .listingDate(company.getListingDate())
                .listingStatus(company.getListingStatus())
                .companyPostalCode(company.getCompanyPostalCode())

                // 최근 2개 재무제표
                .recentFinancials(recentFinancials)

                // 투자 내역
                .rivtDTO(rivtDTO)

                // 주주명부
                .shrDTO(shrDTO)

                // 담당자 정보
                .manager1(latestManager1)
                .manager2(latestManager2)
                .manager3(latestManager3)

                //client table 피투자기업 실무자
                .positionType(client != null ? client.getPositionType() : null)
                .phoneNumber(client != null ? client.getPhoneNumber() : null)
                .email(client != null ? client.getEmail() : null)
                .name(client != null ? client.getName() : null)

                // 사후관리 정보
                .rmngDTO(rmngDTO)

                // 최신 투자 밸류
                .currentCompanyValue(currentCompanyValue)
                .build();
    }
}
