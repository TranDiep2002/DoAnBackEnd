package com.example.doan2.repository;

import com.example.doan2.entity.BoMon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoMonRepository extends JpaRepository<BoMon,Integer> {
    BoMon findByTenBoMon(String tenBoMon);
    List<BoMon> findByActiveFlag(Boolean activeFlag);
    void deleteById(Integer id);
}
