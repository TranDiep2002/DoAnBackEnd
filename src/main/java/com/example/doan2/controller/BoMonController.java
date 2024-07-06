package com.example.doan2.controller;

import com.example.doan2.dto.bomon.BoMonDTO;
import com.example.doan2.entity.BoMon;
import com.example.doan2.repository.BoMonRepository;
import com.example.doan2.req.boMonReq.BoMon_InsertReq;
import com.example.doan2.req.boMonReq.BoMon_UpdateReq;
import com.example.doan2.until.DateUntil;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class BoMonController {
    @Autowired
    private BoMonRepository boMonRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private DateUntil dateUntil;

    @GetMapping("getallBoMon")
    public ResponseEntity<?> getallBoMon(){
        List<BoMon> boMons = boMonRepo.findByActiveFlag(false);
        List<BoMonDTO> boMonDTOS= modelMapper.map(boMons,new TypeToken<List<BoMonDTO>>(){}.getType());
        return ResponseEntity.ok(boMonDTOS);
    }

    @GetMapping("getBoMonbyID/{id}")
    public ResponseEntity<?> getBoMonbyID(@PathVariable Integer id){
        Optional<BoMon> opBoMon = boMonRepo.findById(id);
        if(opBoMon.isPresent()){
            return ResponseEntity.ok(opBoMon.get());
        }
        return ResponseEntity.ok("Không tìm thấy bộ môn với id");
    }
    @PostMapping("insertBoMon")
    public ResponseEntity<?> insertBoMon(@RequestBody BoMon_InsertReq boMon_insertReq){
        BoMon boMon = boMonRepo.findByTenBoMon(boMon_insertReq.getTenBoMon());
        if(boMon==null){
            boMon = new BoMon();
            boMon.setTenBoMon(boMon_insertReq.getTenBoMon());
            boMon.setMoTa(boMon_insertReq.getMoTa());
            boMon.setNgayTao(dateUntil.getDateNow());
            boMon.setNgayCapNhat(dateUntil.getDateNow());
            boMon.setActiveFlag(false);
            boMonRepo.save(boMon);
            return ResponseEntity.ok("Thêm bộ môn thành công");
        }
        return ResponseEntity.ok("Bộ môn đã tồn tại");
    }

    @PutMapping("updateBoMon")
    public ResponseEntity<?> updateBoMon(@RequestBody BoMon_UpdateReq boMon_updateReq){
        Optional<BoMon> opBoMon = boMonRepo.findById(boMon_updateReq.getId());
        if(opBoMon.isPresent()){
            BoMon boMon = opBoMon.get();
            boMon.setTenBoMon(boMon_updateReq.getTenBoMon());
            boMon.setMoTa(boMon_updateReq.getMoTa());
            boMon.setNgayCapNhat(dateUntil.getDateNow());
            boMonRepo.save(boMon);
            return ResponseEntity.ok("Cập nhật bộ môn thành công");
        }
        return ResponseEntity.ok("Không tìm thấy bộ môn để cập nhật");
    }

    @DeleteMapping("deleteBoMon/{id}")
    public ResponseEntity<?> deleteBoMon(@PathVariable Integer id){
        Optional<BoMon> boMon= boMonRepo.findById(id);
        if(boMon.isPresent()){
            boMon.get().setActiveFlag(true);
            boMonRepo.save(boMon.get());
            return ResponseEntity.ok("Xóa bộ môn thành công");
        }
        return ResponseEntity.ok("Không tìm thấy bộ môn để xóa");
    }
}
