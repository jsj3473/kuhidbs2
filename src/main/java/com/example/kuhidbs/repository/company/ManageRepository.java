package com.example.kuhidbs.repository.company;

import com.example.kuhidbs.entity.company.Manage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ManageRepository extends JpaRepository<Manage, Long> {

    // 특정 회사 ID에 해당하는 사후관리 데이터 조회
    Optional<Manage> findByCompany_CompanyId(String companyId);
}
