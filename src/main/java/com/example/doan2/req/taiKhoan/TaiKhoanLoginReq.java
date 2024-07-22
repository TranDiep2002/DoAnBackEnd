package com.example.doan2.req.taiKhoan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaiKhoanLoginReq {
    private String maUser;
    private String passWord;
}
