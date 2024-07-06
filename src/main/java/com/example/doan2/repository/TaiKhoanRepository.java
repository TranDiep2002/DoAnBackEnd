package com.example.doan2.repository;

import com.example.doan2.entity.GiangVien;
import com.example.doan2.entity.SinhVien;
import com.example.doan2.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Integer> {
    List<TaiKhoan> findByActiveFlag(boolean activeFlag);
    TaiKhoan findByMaUser(String maUser);
    TaiKhoan findBySinhVien(SinhVien sinhVien);
    TaiKhoan findByGiangVien(GiangVien giangVien);
    void deleteById(Integer id);

    @Query(value = "select t.loaiTaiKhoan from TaiKhoan t where t.maUser=:maUser")
    String getLoaiTaiKhoan(String maUser);

    List<TaiKhoan> findByLoaiTaiKhoan(String loaiTaiKhoan);

    @Query(value = "from TaiKhoan t where t.maUser like %:maUser%")
    List<TaiKhoan> getTaiKhoanByMaUser(String maUser);
}
