package com.example.doan2.controller;

import com.example.doan2.dto.ky.KyDTO;
import com.example.doan2.entity.Ky;
import com.example.doan2.repository.KyRepository;
import com.example.doan2.req.kyReq.Ky_InsertReq;
import com.example.doan2.until.DateUntil;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class KyController {
    @Autowired
    private KyRepository kyRepo;
    @Autowired
    private DateUntil dateUntil;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("ThemKyHoc")
    public ResponseEntity<?> InsertKyHoc(@RequestBody Ky_InsertReq ky_insertReq){
        Ky ky= kyRepo.findByTenKyAndAndNamHoc(ky_insertReq.getTenKy(),ky_insertReq.getNamHoc());
        if(ky==null){
            kyRepo.updateKy();
            ky= new Ky();
            ky.setTenKy(ky_insertReq.getTenKy());
            ky.setNamHoc(ky_insertReq.getNamHoc());
            ky.setActiveFlag(false);
            ky.setNgayBatDau(dateUntil.getDateNow());
            ky.setNgayKetThuc(dateUntil.getDateNow());
            ky.setTrangThai("Đang tiến hành");
            kyRepo.save(ky);
            return ResponseEntity.ok("Thêm kỳ thành công");
        }
        return ResponseEntity.ok("Đã tồn tại kỳ học với năm học này");
    }
    @GetMapping("getNamHocKyHocHienTai")
    public ResponseEntity<?> getNamHockyHocHienTai(){
        Ky ky =kyRepo.findByTrangThai("Đang tiến hành");
        KyDTO kyDTO = modelMapper.map(ky, new TypeToken<KyDTO>(){}.getType());
        return ResponseEntity.ok(kyDTO);
    }
}
