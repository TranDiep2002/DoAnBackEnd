package com.example.doan2.dto.sinhvien;

import lombok.Data;

import java.util.Date;

@Data
public class SinhVienDTO {
    private Integer id;
    private String hoTen;
    private String maSV;
    private String email;
    private String soDT;
    private String nganh;
    private String lopHanhChinh;
    private Date ngayTao;
    private Date ngayCapNhat;


}
