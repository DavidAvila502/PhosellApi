package com.dev.phosell.Authentication.Application.services;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenCookieService {

    private  final JwtService jwtService;

    public RefreshTokenCookieService(JwtService jwtService){
        this.jwtService = jwtService;
    }


    public Cookie generateCookie(String refreshToken, String path){
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true); // Enable in production
        refreshCookie.setPath(path);
        refreshCookie.setMaxAge((int) (jwtService.getRefreshTokenExpiration() / 1000));

        return  refreshCookie;
    }

    public Cookie cleanCookie(String path){
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath(path);
        cookie.setMaxAge(0);
        return cookie;
    }



}
