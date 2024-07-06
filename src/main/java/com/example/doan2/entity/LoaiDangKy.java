package com.example.doan2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Table
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class LoaiDangKy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String tenLoaiDangKy;
    private String moTa;
    private Date ngayTao;
    private Date ngayCapNhat;


    @OneToMany(mappedBy = "loaiDangKy")
    List<DangKy> dangKyList;


}
