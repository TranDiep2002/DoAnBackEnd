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
public class ChuyenMon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    private String tenChuyenMon;
    private Date ngayTao;
    private Date ngayCapNhat;
    private Boolean activeFlag;

    @OneToMany(mappedBy = "chuyenMon")
    private List<DeTai> deTais;

    @OneToMany(mappedBy = "chuyenMon")
    private List<ChuyenMon_GiangVien> chuyenMon_giangViens;
}
