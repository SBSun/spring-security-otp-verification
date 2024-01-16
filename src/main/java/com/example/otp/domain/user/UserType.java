package com.example.otp.domain.user;

import java.util.Arrays;
import java.util.Optional;

public enum UserType {

    U('U'),
    A('A'),
    S('S');

    private Character type;

    UserType(Character type) {
        this.type = type;
    }

    public static UserType of(Character type) {
        if (type == null) {
            throw new IllegalArgumentException("일치하는 사용자 타입 존재하지 않습니다.");
        }

        UserType userType = Arrays.stream(UserType.values())
                .filter(t -> t.type == type)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("일치하는 사용자 타입 존재하지 않습니다."));

        return userType;
    }

    public Character getType() {
        return type;
    }
}
