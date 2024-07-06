package com.example.doan2.repository;

import com.example.doan2.entity.SinhVien;
import com.example.doan2.entity.TaiKhoan;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SinhVienRepository extends JpaRepository<SinhVien,Integer> {
    List<SinhVien> findByActiveFlag(boolean activeFlag);
    SinhVien findByMaSV(String maSV);
    Optional<SinhVien> findById(Integer id);
    void deleteById(Integer id);

    @Query(value = "from SinhVien sv  where sv.id not in " +
            "(select sd.sinhVien.id from SinhVien_DeTai sd join Ky k " +
            "on sd.ky.id = k.id where k.tenKy=:tenKy and k.namHoc=:namHoc)")
    List<SinhVien> getSinhVienbyKyandNam(String tenKy,String namHoc);

    @Query(value = "from SinhVien  v where v.hoTen like %:hoTen% ")
    List<SinhVien> getSinhVienbyhoTen(String hoTen);

    List<SinhVien> findByNganh(String nganh);
}
