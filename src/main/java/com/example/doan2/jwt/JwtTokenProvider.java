package com.example.doan2.jwt;

import com.example.doan2.service.UserDetail;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {
    @Value("DoAn")
    private String JWT_SECRET;
    @Value("20000000")
    private int JWT_EXPIRATION;

    // tạo jwt từ thông tin user
    public String generateToken(UserDetail userDetail){
        Date now = new Date();
        // tính thời gian hết hiệu lục
        Date dateExpire = new Date(now.getTime()+ JWT_EXPIRATION);

        // tạo chuỗi
        return Jwts.builder().setSubject(userDetail.getMaUser())
                .setIssuedAt(now)
                .setExpiration(dateExpire)
                .signWith(SignatureAlgorithm.HS512,JWT_SECRET)
                .compact();
    }
    // từ jwt lấy lại thông tin user
    public String getMaUserFromJWT(String token){
        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET)
                .parseClaimsJws(token).getBody();

        return claims.getSubject();
    }
    // validate token
    public boolean ValidateToken(String token){
        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            // kiểm tra xem token có hết hạn chưa
            Date expiration = claims.getExpiration();
            Date now = new Date();
            if(expiration.before(now)){
                log.error("Expired JWT Token");
                return false;
            }
            // kiểm tra xem token có đúng chữ kí không
            String username = claims.getSubject();
            if(username==null||username.isEmpty()){
                log.error("JWT claims String is emty");
                return false;
            }
            return true;
        }catch (MalformedJwtException ex){
            log.error("Invalid JWT Token");
        }catch (UnsupportedJwtException ex){
            log.error("Unsupported JWT Token");
        }catch (IllegalArgumentException ex){
            log.error("JWT claims String is empty");
        }catch (ExpiredJwtException ex){
            log.error("Expired JWT Token");
        }

        return false;
    }
}
