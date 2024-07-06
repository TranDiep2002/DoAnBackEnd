package com.example.doan2.entity;

import jakarta.persistence.*;
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
public class DeTai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    private String tenDeTai;
    private String moTa;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private String trangThai;
    private String ghiChu;
    private Boolean activeFlag;

    @OneToMany(mappedBy = "deTai")
    private List<DangKy> dangKyList;

    @ManyToOne
    @JoinColumn(name = "chuyen_mon_id")
    private ChuyenMon chuyenMon;

    @OneToMany(mappedBy = "deTai")
    private List<SinhVien_DeTai> sinhVien_deTais;

    // còn mối quan hệ với hội đồng.....
}
