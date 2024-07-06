package com.example.doan2.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class BoMon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String tenBoMon;
    private String moTa;
    private Date ngayTao;
    private Date ngayCapNhat;
    private Boolean activeFlag;

    @OneToMany(mappedBy = "boMon")
    @JsonBackReference
    private List<GiangVien> giangVienList;

}
