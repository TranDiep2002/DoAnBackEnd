package com.example.doan2.controller;

import com.example.doan2.dto.chuyenMon.ChuyenMonDTO;
import com.example.doan2.entity.ChuyenMon;
import com.example.doan2.repository.ChuyenMonRepository;
import com.example.doan2.req.chuyenMonReq.ChuyenMon_InsertReq;
import com.example.doan2.req.chuyenMonReq.ChuyenMon_UpdateReq;
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
public class ChuyenMonController {
    @Autowired
    private ChuyenMonRepository chuyenMonRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DateUntil dateUntil;

    @GetMapping("getallChuyenMon")
    public ResponseEntity<?> getallChuyenMon(){
        List<ChuyenMon> chuyenMons= chuyenMonRepo.findByActiveFlag(false);
        List<ChuyenMonDTO> chuyenMonDTOS = modelMapper.map(chuyenMons, new TypeToken<List<ChuyenMonDTO>>(){}.getType());
        return ResponseEntity.ok(chuyenMonDTOS);
    }

    @GetMapping("getChuyenMonbyID/{id}")
    public ResponseEntity<?> getChuyenMonbyID(@PathVariable Integer id){
        Optional<ChuyenMon> opChuyenMon = chuyenMonRepo.findById(id);
        if (opChuyenMon.isPresent()){
            return ResponseEntity.ok(opChuyenMon.get());
        }
        return ResponseEntity.ok("Không tìm thấy chuyên môn với id");
    }

    @PostMapping("insertChuyenMon")
    public ResponseEntity<?> insertChuyenMon(@RequestBody ChuyenMon_InsertReq chuyenMon_insertReq){
        ChuyenMon chuyenMon = chuyenMonRepo.findByTenChuyenMon(chuyenMon_insertReq.getTenChuyenMon());
        if(chuyenMon==null){
            chuyenMon = new ChuyenMon();
            chuyenMon.setTenChuyenMon(chuyenMon_insertReq.getTenChuyenMon());
            chuyenMon.setNgayTao(dateUntil.getDateNow());
            chuyenMon.setNgayCapNhat(dateUntil.getDateNow());
            chuyenMon.setActiveFlag(false);
            chuyenMonRepo.save(chuyenMon);
            return ResponseEntity.ok("Thêm chuyên môn thành công");
        }
        return ResponseEntity.ok("Chuyên môn đã tồn tại");
    }

    @PutMapping("updateChuyenMon")
    public ResponseEntity<?> updateChuyenMon(@RequestBody ChuyenMon_UpdateReq chuyenMon_updateReq){
        Optional<ChuyenMon> opChuyenMon = chuyenMonRepo.findById(chuyenMon_updateReq.getId());
        if(opChuyenMon.isPresent()){
            ChuyenMon chuyenMon = opChuyenMon.get();
            chuyenMon.setTenChuyenMon(chuyenMon_updateReq.getTenChuyenMon());
            chuyenMon.setNgayCapNhat(dateUntil.getDateNow());
            chuyenMonRepo.save(chuyenMon);
            return ResponseEntity.ok("Cập nhật chuyên môn thành công");
        }
        return ResponseEntity.ok("Không tìm thấy chuyên môn để cập nhật");
    }

    @DeleteMapping("deleteChuyenMonbyID/{id}")
    public ResponseEntity<?> deleteChuyenMonbyID(@PathVariable Integer id){
        Optional<ChuyenMon> opChuyenMon = chuyenMonRepo.findById(id);
        if(opChuyenMon.isPresent()){
            opChuyenMon.get().setActiveFlag(true);
            chuyenMonRepo.save(opChuyenMon.get());
            return ResponseEntity.ok("Xóa chuyên môn thành công");
        }
        return ResponseEntity.ok("Không tìm thấy chuyên môn để xóa");
    }

}
