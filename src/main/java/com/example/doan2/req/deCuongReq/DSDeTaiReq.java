package com.example.doan2.req.deCuongReq;

import com.example.doan2.entity.DeTai;
import lombok.Data;

import java.util.List;

@Data
public class DSDeTaiReq {
    private String maGV;
    List<DeTai> deTaiList;
}
