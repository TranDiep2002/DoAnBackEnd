package com.example.doan2.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Table
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ky {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    private String tenKy;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private String namHoc;
    private String trangThai;// để biết năm này kỳ này có phài là năm học và kỳ học hiện tại không: đang tiến hành , đã hoàn thành
    private Boolean activeFlag;

    @OneToMany(mappedBy = "ky")

    private List<SinhVien_DeTai> sinhVien_deTais;

    @OneToMany(mappedBy = "ky")
    private List<GiangVien_Ky> giangVien_kies;

    @OneToMany(mappedBy = "ky")
    private List<DangKy> dangKys;

    @OneToMany(mappedBy = "ky")
    private List<ThongBao> thongBaos;
    // còn mối quan hệ với hội đồng
}
