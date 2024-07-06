package com.example.doan2.repository;

import com.example.doan2.entity.Ky;
import com.example.doan2.entity.SinhVien;
import com.example.doan2.entity.SinhVien_DeTai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SinhVien_DeTaiRepository extends JpaRepository<SinhVien_DeTai,Integer> {

    // tìm những sinh viên đã đăng ký đề tài trong kỳ đó
    // , nếu kỳ đó , với id sinh viên đó đã tồn tại thì không thể đăng ký thêm
    // vì nếu đăng ký thêm thì đồng nghĩa sinh viên đó đăng ký 2 lần trong 1 kỳ
    SinhVien_DeTai findByKyAndSinhVien(Ky ky, SinhVien sinhVien);
    // thống kê số sinh viên mà giảng viên đã hướng dẫn trong kỳ đó để đưa ra giới hạn
    @Query(value = "select count(sinhVien) from SinhVien_DeTai  where giangVien.id = :id")
    Integer soluongSV(Integer id);

    List<SinhVien_DeTai> findBySinhVien(SinhVien sinhVien);
}
