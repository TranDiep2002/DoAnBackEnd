package com.example.doan2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class ChuyenMon_GiangVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    @ManyToOne
    @JoinColumn(name = "chuyen_mon_id")
    private ChuyenMon chuyenMon;

    @ManyToOne
    @JoinColumn(name = "giang_vien_id")
    private GiangVien giangVien;
}
