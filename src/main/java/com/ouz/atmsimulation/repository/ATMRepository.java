package com.ouz.atmsimulation.repository;

import com.ouz.atmsimulation.entity.ATM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ATMRepository extends JpaRepository<ATM,Long> {

    Optional<List<ATM>> findByAtmNo(Long atmNo);
}
