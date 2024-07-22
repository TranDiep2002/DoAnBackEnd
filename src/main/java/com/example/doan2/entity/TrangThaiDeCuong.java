package com.example.doan2.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class TrangThaiDeCuong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date thoiGianNop;
    private String trangThai;
    private String maSV;
    private Integer deTai_Id;
    private Integer ky_id;
    private String ghiChu;
    private String maGV; // giang vien cham de cuong
}
