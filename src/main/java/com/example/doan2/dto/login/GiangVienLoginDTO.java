package com.example.doan2.dto.login;

import lombok.Data;

import java.util.Date;

@Data
public class GiangVienLoginDTO {
    private Integer id;
    private String maGV;
    private String hoTen;
    private String vaiTro;
    private String email;
    private String soDT;
    private Date ngayTao;
    private Date ngayCapNhat;
}
