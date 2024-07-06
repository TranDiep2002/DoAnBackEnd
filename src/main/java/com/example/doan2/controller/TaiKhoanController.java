package com.example.doan2.controller;

import com.example.doan2.dto.login.TaiKhoanDTO;
import com.example.doan2.dto.sinhvien.SinhVienDTO;
import com.example.doan2.entity.SinhVien;
import com.example.doan2.entity.TaiKhoan;
import com.example.doan2.jwt.JwtTokenProvider;
import com.example.doan2.repository.TaiKhoanRepository;
import com.example.doan2.req.taiKhoan.TaiKhoanLoginReq;
import com.example.doan2.req.taiKhoan.TaiKhoan_InsertReq;
import com.example.doan2.req.taiKhoan.TaiKhoan_UpdateReq;
import com.example.doan2.service.MailService;
import com.example.doan2.service.UserDetail;
import com.example.doan2.service.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class TaiKhoanController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TaiKhoanRepository taiKhoanRepo;

    @Autowired
    private ModelMapper modelMapper;



    @PostMapping("login")
    public String login(@RequestBody TaiKhoanLoginReq taiKhoanLoginReq){
        UserDetail userDetail= (UserDetail) userService.loadUserByUsername(taiKhoanLoginReq.getMaUser());
        if (userDetail!=null&& passwordEncoder.matches(taiKhoanLoginReq.getPassWord(),userDetail.getPassword())){
            String token = jwtTokenProvider.generateToken(userDetail);
            return token;
        }
        return "không tìm thấy tài khoản để đăng nhập";
    }
    @GetMapping("testlogin")
    public String tetsLogin(){
        return "login thành công";
    }

    @GetMapping("getallTaiKhoan")
    public ResponseEntity<?> getAllTaiKhoan(){
        List<TaiKhoan> taiKhoans = taiKhoanRepo.findByActiveFlag(false);
        List<TaiKhoanDTO> taiKhoanDTOS= modelMapper.map(taiKhoans,new TypeToken<List<TaiKhoan>>(){}.getType());
        return ResponseEntity.ok(taiKhoanDTOS);
    }
    // tìm tài khoản theo mã user
    @GetMapping("getTaiKhoanbyMaUser/{maUser}")
    public ResponseEntity<?> getTaiKhoanbyMaUser(@PathVariable String maUser){
        List<TaiKhoan> taiKhoans= taiKhoanRepo.getTaiKhoanByMaUser(maUser);
        List<TaiKhoanDTO> taiKhoanDTOS= modelMapper.map(taiKhoans,new TypeToken<List<TaiKhoan>>(){}.getType());
        return ResponseEntity.ok(taiKhoanDTOS);
    }
    // tìm tài khoản theo loại tài khoản
    @GetMapping("getTaiKhoanbyLoaiTaiKhoan/{loaiTaiKhoan}")
    public ResponseEntity<?> getTaiKhoanbyLoaiTaiKhoan(@PathVariable String loaiTaiKhoan){
        List<TaiKhoan> taiKhoans = taiKhoanRepo.findByLoaiTaiKhoan(loaiTaiKhoan);
        List<TaiKhoanDTO> taiKhoanDTOS= modelMapper.map(taiKhoans,new TypeToken<List<TaiKhoan>>(){}.getType());
        return ResponseEntity.ok(taiKhoanDTOS);
    }
    // thêm tài khoản chỉ dành cho trường hợp những giảng viên không thuộc khoa
    @PostMapping("insertTaiKhoan")
    public ResponseEntity<?> insertTaiKhoan(@RequestBody TaiKhoan_InsertReq taiKhoan_insertReq){
        TaiKhoan taiKhoan1 = taiKhoanRepo.findByMaUser(taiKhoan_insertReq.getMaUser());
        if(taiKhoan1!=null){
            return ResponseEntity.ok("Tài khoản với mã user đã tồn tại , không thể thêm");
        }
        // Lấy thời gian hiện tại
        LocalDateTime now = LocalDateTime.now();

        // Định dạng thời gian
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        String formattedDateTime = now.format(formatter);
        System.out.println("Thời gian hiện tại: " + formattedDateTime);

        // Chuyển đổi LocalDateTime sang Date
        Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setMaUser(taiKhoan_insertReq.getMaUser());
        taiKhoan.setPassWord(taiKhoan_insertReq.getPassWord());
        taiKhoan.setLoaiTaiKhoan(taiKhoan_insertReq.getLoaiTaiKhoan());
        taiKhoan.setNgayTao(date);
        taiKhoan.setActiveFlag(false);
        taiKhoanRepo.save(taiKhoan);

        return ResponseEntity.ok("Thêm tài khoản thành công");
    }

    @PutMapping("updateTaiKhoan")
    public ResponseEntity<?> updateTaiKhoan(@RequestBody TaiKhoan_UpdateReq taiKhoan_updateReq){
        TaiKhoan taiKhoan = taiKhoanRepo.findByMaUser(taiKhoan_updateReq.getMaUser());
        if(taiKhoan==null){
            return ResponseEntity.ok("không tìm thấy tài khoản để cập nhật");
        }
        taiKhoan.setMaUser(taiKhoan.getMaUser());
        taiKhoan.setPassWord(taiKhoan_updateReq.getPassWord());
        taiKhoan.setLoaiTaiKhoan(taiKhoan_updateReq.getLoaiTaiKhoan());
        taiKhoan.setNgayTao(taiKhoan_updateReq.getNgayTao());
        taiKhoanRepo.save(taiKhoan);
        return ResponseEntity.ok("Cập nhật tài khoản thành công");
    }

    @Transactional
    @DeleteMapping("deleteTaiKhoan/{id}")
    public ResponseEntity<?> deleteTaiKhoan(@PathVariable Integer id){
        Optional<TaiKhoan> taiKhoan = taiKhoanRepo.findById(id);
        if(taiKhoan.isPresent()){
            taiKhoan.get().setActiveFlag(true);
            return ResponseEntity.ok("Xóa user thành công");
        }
        return ResponseEntity.ok(" không tìm thấy user để xóa ");
    }

    // lấy ra loại tài khoản từ mã người dùng
    @GetMapping("getLoaiTaiKhoan/{maUser}")
    public String getLoaiTaiKhoan(@PathVariable String maUser){
        String loaiTaiKhoan = taiKhoanRepo.getLoaiTaiKhoan(maUser);
        return loaiTaiKhoan;
    }

}
