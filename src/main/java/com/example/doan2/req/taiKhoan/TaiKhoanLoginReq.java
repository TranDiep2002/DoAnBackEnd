package com.example.doan2.req.taiKhoan;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaiKhoanLoginReq {
    private String maUser;
    private String passWord;
}
