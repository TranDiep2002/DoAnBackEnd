package com.example.doan2.controller;

import com.example.doan2.dto.dangKy.DangKyDTO;
import com.example.doan2.dto.dangKy.DangKyEditDTO;
import com.example.doan2.dto.giangvien.GiangVienDTO;
import com.example.doan2.dto.ky.KyDTO;
import com.example.doan2.dto.sinhvien.SinhVienDTO;
import com.example.doan2.entity.*;
import com.example.doan2.repository.*;
import com.example.doan2.req.dangKyReq.DangKyDeTaiReq;
import com.example.doan2.req.dangKyReq.DangKy_UpdateGVHDReq;
import com.example.doan2.req.dangKyReq.DangKy_UpdateReq;
import com.example.doan2.until.DateUntil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.analysis.function.Sinh;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.internal.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class DangKyController {
    @Autowired
    private ChuyenMonRepository chuyenMonRepo;
    @Autowired
    private DeTaiRepository deTaiRepo;
    @Autowired
    private KyRepository kyRepo;
    @Autowired
    private GiangVienRepository giangVienRepo;
    @Autowired
    private SinhVienRepository sinhVienRepo;
    @Autowired
    private SinhVien_DeTaiRepository sinhVien_deTaiRepo;
    @Autowired
    private DangKyRepository dangKyRepo;
    @Autowired
    private LoaiDangKyRepository loaiDangKyRepo;
    @Autowired
    private DateUntil dateUntil;

    @Autowired
    private GiangVien_KyRepository giangVien_kyRepo;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("sinhVienDangKyDeTai")
    public String a(){return "a";}

    @PostMapping("sinhVienDangKyDeTai")
    public ResponseEntity<?> dangKyDeTai(@RequestBody DangKyDeTaiReq  dangKyDeTaiReq){
        DeTai deTai = deTaiRepo.findByTenDeTaiAndMoTa(dangKyDeTaiReq.getTenDeTai(),dangKyDeTaiReq.getMoTa());
        ChuyenMon chuyenMon = chuyenMonRepo.findByTenChuyenMon(dangKyDeTaiReq.getNganh());
        GiangVien giangVien = giangVienRepo.findByMaGV(dangKyDeTaiReq.getMaGV());

        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
        if (ky==null){
            log.error("không tìm thấy kỳ");
            return ResponseEntity.ok("Không tìm thấy kỳ học");
        }
        if(giangVien==null){
            log.error("không tìm thấy giảng viên hướng dẫn");
        }else {
            GiangVien_Ky giangVien_ky = giangVien_kyRepo.findByGiangVienAndKy(giangVien,ky);
            if(giangVien_ky==null){
             Insert_GiangVien_Ky(giangVien,ky);
            }
        }
        Optional<LoaiDangKy> oploaiDangKy = loaiDangKyRepo.findById(1);
        LoaiDangKy loaiDangKy = oploaiDangKy.get();
// kiểm tra đề tài nếu đề tài chưa có, thêm đề tài
        if (deTai==null){
                DeTai deTai1 = Insert_DeTai(dangKyDeTaiReq.getTenDeTai(),dangKyDeTaiReq.getMoTa(),chuyenMon);
                // lưu xong đề tài thì lưu tới bảng sinhvien_detai
                // đầu tiên là lưu dữ liệu của sinh viên thứ nhất
                SinhVien sinhVien1 = sinhVienRepo.findByMaSV(dangKyDeTaiReq.getMaSV1());
                if(sinhVien1!=null){
                    // kiểm tra nếu sinhvien_detai với kỳ đó đã có, sinh viên đó đã đăng ký đề tài trog kỳ đó rồi
                    SinhVien_DeTai sinhVien_deTai1 = sinhVien_deTaiRepo.findByKyAndSinhVien(ky,sinhVien1);
                    if(sinhVien_deTai1==null) {
                       InsertSinhVien_DeTai(deTai1,sinhVien1,ky,dangKyDeTaiReq.getVaiTroSV1());
                        // thêm dữ liệu vào bảng đăng ký cho sinh viên 1
                         Insert_DangKy(loaiDangKy,deTai1,giangVien,sinhVien1,ky);
                    }
                    else {
                        log.error("Sinh viên đã đăng ký đề tài trong kỳ này");
                        return ResponseEntity.ok("Sinh viên đã đăng ký trong kỳ này");
                    }
                }else {
                    System.out.println("không tìm thấy sinh viên thứ nhất");
                    return ResponseEntity.ok("Không tìm thấy sinh viên thứ nhất");
                }
                    List<SinhVienDTO> sinhVienList;
                    sinhVienList = dangKyDeTaiReq.getSinhViens();
            if(sinhVienList==null){
                return ResponseEntity.ok("khoong co sinh vien tiep theo");
            }
                // tiếp theo là lưu dữ liệu của sinh viên thứ 2 trong bảng sinhvien_detai
                SinhVien sinhVien2 = sinhVienRepo.findByMaSV(sinhVienList.get(0).getMaSV());
                if(sinhVien2!=null){
                    InsertSinhVien_DeTai(deTai1,sinhVien2,ky, dangKyDeTaiReq.getVaiTroSV2());
                    // thêm dữ liệu vào bảng đăng ký cho sinh viên 2
                    Insert_DangKy(loaiDangKy,deTai1,giangVien,sinhVien2,ky);
                }else{
                    System.out.println("nhóm không có sinh viên thứ 2");
                }
                // sinh viên thứ 3
            if(sinhVienList.size()>1) {
                SinhVien sinhVien3 = sinhVienRepo.findByMaSV(sinhVienList.get(1).getMaSV());
                if (sinhVien3 != null) {
                    InsertSinhVien_DeTai(deTai1, sinhVien3, ky, dangKyDeTaiReq.getVaiTroSV3());
                    // thêm dữ liệu vào bảng đăng ký cho sinh viên 3
                    Insert_DangKy(loaiDangKy, deTai1, giangVien, sinhVien3, ky);
                }
            }else{
                System.out.println("nhóm không có sinh viên thứ 3");
            }
            if(sinhVienList.size()>2){
            // sinh viên thứ 4
            SinhVien sinhVien4 = sinhVienRepo.findByMaSV(sinhVienList.get(2).getMaSV());
            if(sinhVien4!=null){
                InsertSinhVien_DeTai(deTai1,sinhVien4,ky, dangKyDeTaiReq.getVaiTroSV4());
                // thêm dữ liệu vào bảng đăng ký cho sinh viên 4
                Insert_DangKy(loaiDangKy,deTai1,giangVien,sinhVien4,ky);
            }}else{
                System.out.println("nhóm không có sinh viên thứ 4");
            }
            // sinh viên thứ 5
            if(sinhVienList.size()>3) {
                SinhVien sinhVien5 = sinhVienRepo.findByMaSV(sinhVienList.get(3).getMaSV());
                if (sinhVien2 != null) {
                    InsertSinhVien_DeTai(deTai1, sinhVien5, ky, dangKyDeTaiReq.getVaiTroSV5());
                    // thêm dữ liệu vào bảng đăng ký cho sinh viên 5
                    Insert_DangKy(loaiDangKy, deTai1, giangVien, sinhVien5, ky);
                }
            }else{
                System.out.println("nhóm không có sinh viên thứ 5");
            }
                return ResponseEntity.ok("thêm đăng ký đề tài thành công");
            }
        else {
            SinhVien sinhVien1 = sinhVienRepo.findByMaSV(dangKyDeTaiReq.getMaSV1());
            if(sinhVien1!=null){
                // kiểm tra nếu sinhvien_detai với kỳ đó đã có, sinh viên đó đã đăng ký đề tài trog kỳ đó rồi
                SinhVien_DeTai sinhVien_deTai1 = sinhVien_deTaiRepo.findByKyAndSinhVien(ky,sinhVien1);
                if(sinhVien_deTai1==null) {
                    InsertSinhVien_DeTai(deTai,sinhVien1,ky,dangKyDeTaiReq.getVaiTroSV1());
                    // thêm dữ liệu vào bảng đăng ký cho sinh viên 1
                    Insert_DangKy(loaiDangKy, deTai,giangVien,sinhVien1,ky);
                }
                else {
                    log.error("Sinh viên đã đăng ký đề tài trong kỳ này");
                    return ResponseEntity.ok("Sinh viên đã đăng ký trong kỳ này");
                }
            }else {
                System.out.println("không tìm thấy sinh viên thứ nhất");
                return ResponseEntity.ok("Không tìm thấy sinh viên thứ nhất");
            }
            List<SinhVienDTO> sinhVienList;
           sinhVienList = dangKyDeTaiReq.getSinhViens();
           if(sinhVienList==null){
               return ResponseEntity.ok("khoong co sinh vien tiep theo");
           }
            // tiếp theo là lưu dữ liệu của sinh viên thứ 2 trong bảng sinhvien_detai
            SinhVien sinhVien2 = sinhVienRepo.findByMaSV(sinhVienList.get(0).getMaSV());
            if(sinhVien2!=null){
                InsertSinhVien_DeTai(deTai,sinhVien2,ky, dangKyDeTaiReq.getVaiTroSV2());
                // thêm dữ liệu vào bảng đăng ký cho sinh viên 2
                Insert_DangKy(loaiDangKy,deTai,giangVien,sinhVien2,ky);
            }else{
                System.out.println("nhóm không có sinh viên thứ 2");
            }
            if(sinhVienList.size()>1){
            // sinh viên thứ 3
            SinhVien sinhVien3 = sinhVienRepo.findByMaSV(sinhVienList.get(1).getMaSV());
            if(sinhVien3!=null){
                InsertSinhVien_DeTai(deTai,sinhVien3,ky, dangKyDeTaiReq.getVaiTroSV3());
                // thêm dữ liệu vào bảng đăng ký cho sinh viên 3
                Insert_DangKy(loaiDangKy,deTai,giangVien,sinhVien3,ky);
            }
            }else{
                System.out.println("nhóm không có sinh viên thứ 3");
            }
            // sinh viên thứ 4
            if(sinhVienList.size()>2) {
                SinhVien sinhVien4 = sinhVienRepo.findByMaSV(sinhVienList.get(2).getMaSV());
                if (sinhVien4 != null) {
                    InsertSinhVien_DeTai(deTai, sinhVien4, ky, dangKyDeTaiReq.getVaiTroSV4());
                    // thêm dữ liệu vào bảng đăng ký cho sinh viên 4
                    Insert_DangKy(loaiDangKy, deTai, giangVien, sinhVien4, ky);
                }
            }else{
                System.out.println("nhóm không có sinh viên thứ 4");
            }
            // sinh viên thứ 5
            if(sinhVienList.size()>3) {

                SinhVien sinhVien5 = sinhVienRepo.findByMaSV(sinhVienList.get(3).getMaSV());
                if (sinhVien2 != null) {
                    InsertSinhVien_DeTai(deTai, sinhVien5, ky, dangKyDeTaiReq.getVaiTroSV5());
                    // thêm dữ liệu vào bảng đăng ký cho sinh viên 5
                    Insert_DangKy(loaiDangKy, deTai, giangVien, sinhVien5, ky);
                }
            }else{
                System.out.println("nhóm không có sinh viên thứ 5");
            }
            return ResponseEntity.ok("Thêm đăng ký đề tài thành công");
        }

    }
    // thêm giảng viên_ky
    public void Insert_GiangVien_Ky(GiangVien giangVien, Ky ky){
        GiangVien_Ky giangVien_ky = new GiangVien_Ky();
        giangVien_ky.setGiangVien(giangVien);
        giangVien_ky.setKy(ky);
        giangVien_kyRepo.save(giangVien_ky);
    }
    // thêm đề tài
    public DeTai Insert_DeTai(String tenDeTai,String moTa,ChuyenMon chuyenMon){
        DeTai deTai1 = new DeTai();
        deTai1.setTenDeTai(tenDeTai);
        deTai1.setMoTa(moTa);
        deTai1.setChuyenMon(chuyenMon);
        deTai1.setTrangThai("Chưa thục hiện");
        deTai1.setNgayBatDau(dateUntil.getDateNow());
        deTaiRepo.save(deTai1);
        return deTai1;
    }
// thêm sinhvien_deTai
    public void InsertSinhVien_DeTai(DeTai deTai, SinhVien sinhVien, Ky ky,String vaiTro){
        SinhVien_DeTai sinhVien_deTai2 = new SinhVien_DeTai();
        sinhVien_deTai2.setDeTai(deTai);
        sinhVien_deTai2.setSinhVien(sinhVien);
        sinhVien_deTai2.setKy(ky);
        sinhVien_deTai2.setVaiTroSinhVien(vaiTro);
        sinhVien_deTai2.setNgayTao(dateUntil.getDateNow());
        sinhVien_deTai2.setNgayCapNhat(dateUntil.getDateNow());
        sinhVien_deTaiRepo.save(sinhVien_deTai2);
    }
    // thêm đăng ký
    public  void Insert_DangKy(LoaiDangKy loaiDangKy,DeTai deTai, GiangVien giangVien, SinhVien sinhVien, Ky ky){
        DangKy dangKy1 = new DangKy();
        dangKy1.setLoaiDangKy(loaiDangKy);
        dangKy1.setDeTai(deTai);
        if(giangVien!=null) {
            dangKy1.setGiangVien(giangVien);
            giangVien_kyRepo.updateSoLuongSV(ky.getTenKy(),giangVien.getMaGV());
        }
        else {
            log.error("Khong tim thay giảng vien để đăng ký");
        }
        dangKy1.setSinhVien(sinhVien);
        dangKy1.setTrangThai("Chờ duyệt");
        dangKy1.setKy(ky);
        dangKy1.setNgayTao(dateUntil.getDateNow());
        dangKy1.setNgayCapNhat(dateUntil.getDateNow());
        dangKyRepo.save(dangKy1);
    }

    // lấy ra những giảng viên có số sinh viên hướng dẫn nhỏ hơn 7 sinh viên
    // ở đây ko cần kỳ học nữa vì chúng ta sẽ lấy ra kỳ học có trạng thái là đang tiến hành luôn

    @GetMapping("getGiangVienbySoLuongSinhVien")
    public  ResponseEntity<?> getGiangVienbySoLuongSinhVien(){
        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
        List<GiangVien> giangVienList = giangVienRepo.getGiangVienbySoLuongSV(ky.getTenKy(),ky.getNamHoc() );
        List<GiangVienDTO> giangVienDTOS= modelMapper.map(giangVienList,new TypeToken<List<GiangVienDTO>>(){}.getType());
        return ResponseEntity.ok(giangVienDTOS);
    }
    // lấy ra những sinh viên trong học kỳ đó chưa có nhóm

    @GetMapping("getSinhVienbyKyandNamHoc/{maSV}")
    public ResponseEntity<?> getSinhVienbyKyandNamHoc(@PathVariable String maSV){
        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
        List<SinhVien> sinhVienList= sinhVienRepo.getSinhVienbyKyandNam(ky.getTenKy(),ky.getNamHoc(),maSV);
        List<SinhVienDTO> sinhvienDTOs= modelMapper.map(sinhVienList,new TypeToken<List<SinhVienDTO>>(){}.getType());
        return ResponseEntity.ok(sinhvienDTOs);
    }
    //lấy ra kỳ học hiện tại
    @GetMapping("getTenKybyTrangThai/{tenTrangThai}")
    public ResponseEntity<?> getKybyTrangThai(@PathVariable String tenTrangThai){
        Ky ky = kyRepo.findByTrangThai(tenTrangThai);
        return ResponseEntity.ok(ky.getTenKy());
    }
//  lấy ra năm học hiện tại
    @GetMapping("getNamHocbyTrangThai/{tenTrangThai}")
    public ResponseEntity<?> getNamHocbyTrangThai(@PathVariable String tenTrangThai){
        Ky ky = kyRepo.findByTrangThai(tenTrangThai);
        return ResponseEntity.ok(ky.getNamHoc());
    }
// lấy ra sinhvien_detai theo mã sinh viên
    @GetMapping("getDangKybyMaSV/{maSV}")
    public ResponseEntity<?> getDangKybyMaSV(@PathVariable String maSV){
        SinhVien sinhVien= sinhVienRepo.findByMaSV(maSV);
        List<DangKy> dangKIES= dangKyRepo.findBySinhVien(sinhVien);
        List<DangKyDTO> dangKyDTOS = new ArrayList<>();
        if(dangKIES.size()!=0){
            dangKIES.forEach(dangKy -> {
                DangKyDTO dangKyDTO = new DangKyDTO();
                dangKyDTO.setId(dangKy.getId());
                dangKyDTO.setNgayTao(dangKy.getNgayTao());
                dangKyDTO.setNgayCapNhat(dangKy.getNgayCapNhat());
                dangKyDTO.setTenDeTai(dangKy.getDeTai().getTenDeTai());
                dangKyDTO.setMoTaDeTai(dangKy.getDeTai().getMoTa());
                dangKyDTO.setTrangThai(dangKy.getTrangThai());
                dangKyDTO.setHoTen(dangKy.getGiangVien().getHoTen());
                dangKyDTO.setTenKy(dangKy.getKy().getTenKy());
                dangKyDTO.setTenNamHoc(dangKy.getKy().getNamHoc());
                // vì list string trong đăng ký DTO cần add 1 list , nên phải tạo list mới để add tên, sau đó add list đó vào list trong đăng ký dto
                List<String> tenSinhVien = new ArrayList<>();
                List<SinhVien> sinhVienList = dangKyRepo.getSVbyKyHocNamHoc(dangKy.getKy(),dangKy.getDeTai(),dangKy.getGiangVien());
                sinhVienList.forEach(sinhVien1 -> {
                    String ten = sinhVien1.getHoTen();
                    tenSinhVien.add(ten);
                });
                dangKyDTO.setTenSinhViens(tenSinhVien);
                dangKyDTOS.add(dangKyDTO);
            });
            return ResponseEntity.ok(dangKyDTOS);
        }
        return ResponseEntity.ok("Không có nhóm nào với mã sinh viên này");
    }

   // lấy ra tất cả nhóm, đề tài theo kỳ, năm học truyền vào
    // đầu tiên là lấy ra đề tài trong học kỳ, sau đó lấy ra GVHD theo đề tài, sau đó lấy ra những sinh viên theo đề tài , giảng viên, kỳ
    // lấy nhóm theo chuyên môn của giảng viên để giảng viên duyệt
    @GetMapping("/getallDangKy/{tenChuyenMon}")
    public ResponseEntity<?> getallDangKy( @PathVariable String tenChuyenMon){

        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
        List<DeTai> deTais = dangKyRepo.getDeTaibyKy(ky,tenChuyenMon);
        List<DangKyDTO> dangKyDTOS = new ArrayList<>();
        deTais.forEach(deTai -> {
            List<GiangVien> giangVienList= dangKyRepo.giangViens(deTai,ky);
            List<String> tenSVs = new ArrayList<>();
            giangVienList.forEach(giangVien -> {
                DangKyDTO dangKyDTO = new DangKyDTO();
                dangKyDTO.setTenKy(ky.getTenKy());
                dangKyDTO.setTenNamHoc(ky.getNamHoc());
                dangKyDTO.setTenDeTai(deTai.getTenDeTai());
                dangKyDTO.setMoTaDeTai(deTai.getMoTa());
                dangKyDTO.setHoTen(giangVien.getHoTen());
                dangKyDTO.setMaGV(giangVien.getMaGV());
                String trangthai = dangKyRepo.gettrangThai(ky,giangVien,deTai);
                dangKyDTO.setTrangThai(trangthai);
                List<SinhVien> sinhVienList= dangKyRepo.getSVbyKyHocNamHoc(ky,deTai,giangVien);
                sinhVienList.forEach(sinhVien -> {
                    String tenSV = sinhVien.getHoTen();
                    tenSVs.add(tenSV);
                });
                dangKyDTO.setTenSinhViens(tenSVs);
                dangKyDTOS.add(dangKyDTO);
            });
        });
        return ResponseEntity.ok(dangKyDTOS);
    }

    // lấy ra danh sách các nhóm cho giáo vụ
    // lấy ra danh sách các nhóm theo học kỳ và năm học hiện tại
    @GetMapping("/getallDangKyGiaoVu")
    public ResponseEntity<?> getallDangKyGiaoVu(){
        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
        List<DeTai> deTais = dangKyRepo.getDeTaibyKyGiaoVu(ky);
        List<DangKyDTO> dangKyDTOS = new ArrayList<>();
        deTais.forEach(deTai -> {
            List<GiangVien> giangVienList= dangKyRepo.giangViens(deTai,ky);
            List<String> tenSVs = new ArrayList<>();
            giangVienList.forEach(giangVien -> {
                DangKyDTO dangKyDTO = new DangKyDTO();
                dangKyDTO.setTenKy(ky.getTenKy());
                dangKyDTO.setTenNamHoc(ky.getNamHoc());
                dangKyDTO.setTenDeTai(deTai.getTenDeTai());
                dangKyDTO.setMoTaDeTai(deTai.getMoTa());
                dangKyDTO.setHoTen(giangVien.getHoTen());
                dangKyDTO.setMaGV(giangVien.getMaGV());
                String trangthai = dangKyRepo.gettrangThai(ky,giangVien,deTai);
                dangKyDTO.setTrangThai(trangthai);
                List<SinhVien> sinhVienList= dangKyRepo.getSVbyKyHocNamHoc(ky,deTai,giangVien);
                sinhVienList.forEach(sinhVien -> {
                    String tenSV = sinhVien.getHoTen();
                    tenSVs.add(tenSV);
                });
                dangKyDTO.setTenSinhViens(tenSVs);
                dangKyDTOS.add(dangKyDTO);
            });
        });
        return ResponseEntity.ok(dangKyDTOS);
    }
// lấy ra trạng thái đăng ký
    @GetMapping("getTrangThaiDangKy/{MaGiangVien}/{TenDeTai}/{moTa}")
    public ResponseEntity<?> getTrangThai(@PathVariable String MaGiangVien,@PathVariable String moTa, @PathVariable String TenDeTai){
        GiangVien giangVien = giangVienRepo.findByMaGV(MaGiangVien);
        DeTai deTai = deTaiRepo.findByTenDeTaiAndMoTa(TenDeTai,moTa);
        String trangThai = dangKyRepo.getTrangThai(deTai,giangVien);
        return ResponseEntity.ok(trangThai);
    }
    // update đăng ký đề tài
    @PutMapping("UpdateDangKy")
    public ResponseEntity<?> updateDangKy(@RequestBody DangKy_UpdateReq dangKy_updateReq){
        SinhVien sinhVien = sinhVienRepo.findByMaSV(dangKy_updateReq.getMaSV1());
        SinhVien sinhVien1 = sinhVienRepo.findByMaSV(dangKy_updateReq.getMaSV2());
        DeTai deTai = deTaiRepo.findByTenDeTaiAndMoTa(dangKy_updateReq.getTenDeTai(),dangKy_updateReq.getMoTa());
        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
        if (sinhVien!=null) {
            DangKy dangKy = dangKyRepo.getDangKyByDeTaiSinhVienKy(deTai, sinhVien, ky);
            if (dangKy != null) {
                dangKy.setTrangThai("Đã duyệt");
                dangKyRepo.save(dangKy);
                log.info("Cập nhật trạng thái đăng ký cho sinh viên 1 thành công");
            } else {
                return ResponseEntity.ok("Không tồn tại đăng ký của sinh viên 1");
            }
            if (sinhVien1 != null) {
                DangKy dangKy1 = dangKyRepo.getDangKyByDeTaiSinhVienKy(deTai, sinhVien1, ky);
                if (dangKy1 != null) {
                    dangKy1.setTrangThai("Đã duyệt");
                    dangKyRepo.save(dangKy1);
                    log.info("Cập nhật trạng thái đăng ký cho sinh viên 2 thành công");
                } else {
                    log.info("Không tồn tại đăng ký của sinh viên thứ 2");
                }
            } else {
               log.info("Không có sinh viên thứ 2");
            }
        }
        return ResponseEntity.ok("Không tồn tại sinh viên thứ 1");
    }

    // cập nhật trạng thái cách 2
    @PutMapping("updateTrangThaibyGiangVienDeTai/{MaGiangVien}/{TenDeTai}/{moTa}")
    public ResponseEntity<?> UpDateTrangThaiDangKyThanhCong(@PathVariable String MaGiangVien, @PathVariable String TenDeTai,@PathVariable String moTa ){
        GiangVien giangVien = giangVienRepo.findByMaGV(MaGiangVien);
        DeTai deTai = deTaiRepo.findByTenDeTaiAndMoTa(TenDeTai,moTa);
        dangKyRepo.UpDateTrangThaiDeTai(deTai,giangVien);
        return ResponseEntity.ok("Cập nhật trạng thái thành công");
    }
    // cập nhật trạng thái hủy
    @PutMapping("updateTrangThaiHuybyGiangVienDeTai/{maGV}/{tenDeTai}/{moTa}")
    public ResponseEntity<?> UpdateTrangThaiDangKyHuy(@PathVariable String maGV,@PathVariable String tenDeTai,@PathVariable String moTa){
        GiangVien giangVien = giangVienRepo.findByMaGV(maGV);
        DeTai deTai = deTaiRepo.findByTenDeTaiAndMoTa(tenDeTai,moTa);
        dangKyRepo.UpDateTrangThaiDeTaiHuy(deTai,giangVien);
        return ResponseEntity.ok("Cập nhật trạng thái thành công");
    }

    @PutMapping("updateGhiChu/{tenDeTai}/{moTa}/{ghichu}")
    public ResponseEntity<?> UpdateGhiChu(@PathVariable String ghichu,@PathVariable String tenDeTai,@PathVariable String moTa){
        DeTai deTai = deTaiRepo.findByTenDeTaiAndMoTa(tenDeTai,moTa);
        dangKyRepo.UpdateGhiChu(deTai,ghichu);
        return ResponseEntity.ok("Cập nhật ghi chú thành công");
    }
    // lấy ra đăng ký theo tên đề tài và kỳ
    @GetMapping("getDangKybyTenDeTaiMoTaKyHoc/{tenDeTai}/{moTaDeTai}")
    public ResponseEntity<?> getDangKybyTenDeTaiMoTaKyHoc(@PathVariable String tenDeTai, @PathVariable String moTaDeTai){
        DeTai deTai = deTaiRepo.findByTenDeTaiAndMoTa(tenDeTai,moTaDeTai);
        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
        DangKyEditDTO dangKyEditDTO = dangKyRepo.getDangKybyDeTaiandKy(deTai,ky);
        return ResponseEntity.ok(dangKyEditDTO);
    }
// cập nhật GVHD
    @PutMapping("updateGiangVienHD")
    public ResponseEntity<?> UpdateGVHD(@RequestBody DangKy_UpdateGVHDReq dangKy_updateGVHDReq){
        GiangVien giangVien = giangVienRepo.findByMaGV(dangKy_updateGVHDReq.getMaGV());
        DeTai deTai = deTaiRepo.findByTenDeTaiAndMoTa(dangKy_updateGVHDReq.getTenDeTai(),dangKy_updateGVHDReq.getMoTaDeTai());
        Ky ky = kyRepo.findByTrangThai("Đang tiến hành");
        GiangVien giangVien1 = dangKyRepo.getGiangVienToUpdateSoLuongSinhVien(deTai,ky);
        GiangVien_Ky giangVien_ky1= giangVien_kyRepo.findByGiangVienAndKy(giangVien1,ky);
        giangVien_ky1.setSoLuongSV(giangVien_ky1.getSoLuongSV()- dangKyRepo.getSoLuongSVTrongNhom(deTai,ky));
        List dangKy = dangKyRepo.findByDeTaiAndKy(deTai,ky);
        if(dangKy!=null) {
            dangKyRepo.UpdateGiangVienHD(giangVien, deTai, ky);
            GiangVien_Ky giangVien_ky = giangVien_kyRepo.findByGiangVienAndKy(giangVien,ky);
            giangVien_ky.setSoLuongSV(dangKyRepo.getSoLuongSVTrongNhom(deTai,ky)+giangVien_ky.getSoLuongSV());
            return ResponseEntity.ok("Cập nhật giảng viên hướng dẫn thành công");
        }
        return ResponseEntity.ok("Không tìm thấy đăng ký để cập nhật");
    }

}
