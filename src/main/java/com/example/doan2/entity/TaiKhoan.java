package com.example.doan2.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class TaiKhoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    private String maUser;
    private String loaiTaiKhoan; // phân quyền là giảng viên hay sinh viên
    private String passWord;
    private Date ngayTao;
    private Boolean activeFlag;// khi xóa thì trạng thái thành true

    @OneToOne
    @JoinColumn(name ="giangvien_id", unique = true)
    @JsonBackReference // Sử dụng @JsonBackReference để tránh vòng lặp
    private GiangVien giangVien;

    @OneToOne
    @JoinColumn(name ="sinhvien_id", unique = true)
    @JsonBackReference // Sử dụng @JsonBackReference để tránh vòng lặp
    private SinhVien sinhVien;

}
