package com.example.otp.domain.user.repository;

import com.example.otp.domain.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.apache.http.auth.AUTH;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.jooq.tables.User.USER;
import static com.example.otp.global.utils.JooqUtils.eqIfNotEmpty;

@Repository
@RequiredArgsConstructor
public class UserJooqRepository {

    private final DSLContext dslContext;

    /**
     * 이름 or 전화번호에 해당하는 사용자 정보 반환
     * 매개변수내에 값이 존재하지 않으면 모든 사용자 반환
     * @param name
     * @param phone
     * @return
     */
    public List<UserResponseDto.Info> search(String name, String phone) {
        return dslContext
                .select(
                        USER.USER_ID.as("id"),
                        USER.NAME,
                        USER.PHONE,
                        USER.CREATED_AT
                )
                .from(USER)
                .where(
                    eqIfNotEmpty(USER.NAME, name)
                            .and(eqIfNotEmpty(USER.PHONE, phone))
                )
                .fetchInto(UserResponseDto.Info.class);
    }
}
