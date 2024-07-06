package com.example.doan2.service;

import com.example.doan2.dto.login.SinhVienLoginDTO;
import com.example.doan2.dto.login.TaiKhoanDTO;
import com.example.doan2.entity.SinhVien;
import com.example.doan2.entity.TaiKhoan;
import com.example.doan2.repository.GiangVienRepository;
import com.example.doan2.repository.SinhVienRepository;
import com.example.doan2.repository.TaiKhoanRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceIplm implements UserService {

    @Autowired
    private TaiKhoanRepository taiKhoanrepo;


    // user name ở đây sẽ là maUser
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TaiKhoan taiKhoan = taiKhoanrepo.findByMaUser(username);
        if(taiKhoan==null){
            log.error("không tìm thấy tài khoản");
            throw  new UsernameNotFoundException("khong tim thay tai khoan");
        }
        TaiKhoanDTO taiKhoanDTO = new TaiKhoanDTO();
        taiKhoanDTO.setId(taiKhoan.getId());
        taiKhoanDTO.setMaUser(taiKhoan.getMaUser());
        taiKhoanDTO.setPassWord(taiKhoan.getPassWord());
        taiKhoanDTO.setLoaiTaiKhoan(taiKhoan.getLoaiTaiKhoan());
        taiKhoanDTO.setNgayTao(taiKhoan.getNgayTao());
        return UserDetail.mapUserToUserDetail(taiKhoanDTO);
    }
}
