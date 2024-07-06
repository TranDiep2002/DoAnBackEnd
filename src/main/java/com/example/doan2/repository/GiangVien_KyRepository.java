package com.example.doan2.repository;

import com.example.doan2.entity.GiangVien_Ky;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GiangVien_KyRepository extends JpaRepository<GiangVien_Ky,Integer> {

    @Modifying
    @Transactional
    @Query(value = "update GiangVien_Ky  g set g.soLuongSV= g.soLuongSV+1 where g.ky.tenKy=:tenKy and g.giangVien.maGV=:maGV ")
    void updateSoLuongSV(String tenKy,String maGV);
}
