package com.example.doan2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Table
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SinhVien_DeTai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    private String vaiTroSinhVien;
    private String vaiTroGiangVien;
    private Date ngayTao;
    private Date ngayCapNhat;

    @ManyToOne
    @JoinColumn(name = "sinh_vien_id")
    private SinhVien sinhVien;

    // giảng viên phản biện
    @ManyToOne
    @JoinColumn(name = "giang_vien_id")
    private GiangVien giangVien;

    @ManyToOne
    @JoinColumn(name = "de_tai_id")
    private DeTai deTai;

    @ManyToOne
    @JoinColumn(name = "ky_id")
    private Ky ky;

    // thiếu quan hệ điểm thành phần đề tài
}
