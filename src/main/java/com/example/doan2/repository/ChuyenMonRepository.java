package com.example.doan2.repository;

import com.example.doan2.entity.ChuyenMon;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChuyenMonRepository extends JpaRepository<ChuyenMon,Integer> {
    ChuyenMon findByTenChuyenMon(String tenChuyenMon);
    List<ChuyenMon> findByActiveFlag(Boolean activeFlag);
    void deleteById(Integer id);
}
