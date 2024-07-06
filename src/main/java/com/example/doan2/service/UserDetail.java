package com.example.doan2.service;

import com.example.doan2.dto.login.TaiKhoanDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.Date;

@Data
@AllArgsConstructor
public class UserDetail implements UserDetails {

    private ModelMapper modelMapper;
    @Autowired
    public UserDetail(ModelMapper modelMapper){this.modelMapper = modelMapper;}
    private Integer id;
    private String maUser;
    private String passWord;
    private String loaiTaiKhoan;
    private Date ngayTao;

    public Collection<? extends GrantedAuthority> authorities;

    public UserDetail(Integer id, String maUser,String passWord,String loaiTaiKhoan,Date ngayTao){
        this.id = id;
        this.maUser = maUser;
        this.passWord= passWord;
        this.loaiTaiKhoan = loaiTaiKhoan;
        this.ngayTao = ngayTao;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
    // map thong tin của user sang UserDetail
    public static UserDetail mapUserToUserDetail(TaiKhoanDTO taiKhoanDTO){
        return new UserDetail(
                taiKhoanDTO.getId(),
                taiKhoanDTO.getMaUser(),
                taiKhoanDTO.getPassWord(),
                taiKhoanDTO.getLoaiTaiKhoan(),
                taiKhoanDTO.getNgayTao()
        );
    }

    @Override
    public String getPassword() {
        return this.passWord;
    }

    @Override
    public String getUsername() {
        return this.maUser;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
