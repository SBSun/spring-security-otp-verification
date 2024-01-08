package com.example.demo.otp;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import java.util.HashMap;

public class GoogleOTP {

    public static HashMap<String, String> generate(String accountId) {
        HashMap<String, String> map = new HashMap<String, String>();

        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        // 인증키 생성
        GoogleAuthenticatorKey googleAuthenticatorKey = googleAuthenticator.createCredentials();

        // 실제론 생성한 key를 DB에 저장해놔야 나중에 OTP를 검증할 수 있음
        String key = googleAuthenticatorKey.getKey();

        String qrUrl = GoogleAuthenticatorQRGenerator.getOtpAuthURL("account", accountId, googleAuthenticatorKey);

        map.put("key", key);
        map.put("qrUrl", qrUrl);

        return map;
    }

    public static boolean verifyOTP(String key, String otpCode) {
        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();

        return googleAuthenticator.authorize(key, Integer.parseInt(otpCode));
    }
}
