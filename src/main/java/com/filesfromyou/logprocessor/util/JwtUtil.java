package com.filesfromyou.logprocessor.util;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtUtil {
    public Boolean validateToken(String token, UserDetails userDetails) {
        //Validate token here
        return true;
    }
}