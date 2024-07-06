package com.example.doan2.controller;

import com.example.doan2.dto.thongBao.ThongBaoDTO;
import com.example.doan2.entity.Ky;
import com.example.doan2.entity.ThongBao;
import com.example.doan2.repository.KyRepository;
import com.example.doan2.repository.ThongBaoRepository;
import com.example.doan2.req.thongBao.ThongBao_InsertReq;
import com.example.doan2.until.DateUntil;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@Slf4j
public class ThongBaoController {

    @Autowired
    private ThongBaoRepository thongBaoRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private KyRepository kyRepo;
    @Autowired
    private DateUntil dateUntil;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/application")
    @SendTo("/topic/all")
    @PostMapping("ThemThongBao")
    public ResponseEntity<?> ThemThongBao(@RequestBody ThongBao_InsertReq thongBao_insertReq){
        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
        ThongBao thongBao = new ThongBao();
        thongBao.setNgayBatDau(dateUntil.getDateNow());
        thongBao.setNoiDungThongBao(thongBao_insertReq.getNoiDungThongBao());
        thongBao.setNgayKetThuc(thongBao_insertReq.getNgayKetThuc());
        thongBao.setKy(ky);
        thongBaoRepo.save(thongBao);
        messagingTemplate.convertAndSend("/topic/notifications", "Có thông báo mới");
        return ResponseEntity.ok("Thêm thông báo thành công");
    }

    @MessageMapping("/topic/all")
    public ResponseEntity<?> abc(@RequestBody Object obj){
        System.out.println("a");
        return ResponseEntity.ok("Thêm thông báo thành công");
    }

    @MessageMapping("/topic/notifications")
    public ResponseEntity<?> abc2(@RequestBody Object obj){
        System.out.println("a");
        return ResponseEntity.ok("Thêm thông báo thành công");
    }


    @GetMapping("allGetThongBao")
    public ResponseEntity<?> GetAllThongBao(){
        List<ThongBao> thongBaos = thongBaoRepo.getAllThongBao();
        List<ThongBaoDTO> thongBaoDTOS = new ArrayList<>();
        thongBaos.forEach(thongBao -> {
            ThongBaoDTO thongBaoDTO = new ThongBaoDTO();
            thongBaoDTO.setNoiDungThongBao(thongBao.getNoiDungThongBao());
            thongBaoDTO.setNgayBatDau(thongBao.getNgayBatDau());
            thongBaoDTO.setNgayKetThuc(thongBao.getNgayKetThuc());
            thongBaoDTO.setTenKy(thongBao.getKy().getTenKy());
            thongBaoDTO.setNamHoc(thongBao.getKy().getNamHoc());
            thongBaoDTOS.add(thongBaoDTO);
        });
        return ResponseEntity.ok(thongBaoDTOS);
    }
}
