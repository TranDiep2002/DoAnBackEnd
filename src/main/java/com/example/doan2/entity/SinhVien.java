package com.example.doan2.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.task.TaskSchedulingProperties;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "taikhoan_id"))
@NoArgsConstructor
@AllArgsConstructor
public class SinhVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    private String hoTen;
    private String maSV;
    private String email;
    private String nganh;
    private Date ngayTao;
    private Date ngayCapNhat;
    private String gioiTinh;
    private String lopHanhChinh;
    private Boolean activeFlag;// khi xóa thì trạng thái thành true

    @OneToOne(mappedBy = "sinhVien")
    @JsonBackReference // Sử dụng @JsonBackReference để tránh vòng lặp
    private TaiKhoan taiKhoans;

    @JsonManagedReference
    @OneToMany(mappedBy = "sinhVien")
    private List<DangKy> dangKyList;

    @JsonManagedReference
    @OneToMany(mappedBy = "sinhVien")
    private List<SinhVien_DeTai> sinhVien_deTais;
}
