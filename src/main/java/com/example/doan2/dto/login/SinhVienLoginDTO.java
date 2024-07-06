package com.example.doan2.dto.login;

import lombok.Data;

import java.util.Date;

@Data
public class SinhVienLoginDTO {
    private Integer id;
    private String hoTen;
    private String maSV;
    private String email;
    private String soDT;
    private Date ngayTao;
    private Date ngayCapNhat;
}
