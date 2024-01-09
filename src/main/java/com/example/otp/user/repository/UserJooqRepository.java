package com.example.otp.user.repository;

import com.example.otp.user.User;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.jooq.tables.User.USER;

@Repository
@RequiredArgsConstructor
public class UserJooqRepository {

    private final DSLContext dslContext;

    public List<User> getAllUser(){
        return dslContext
                .selectFrom(USER)
                .fetchInto(User.class);
    }

    public User findById(Long id) {
        return dslContext
                .selectFrom(USER)
                .where(USER.ID.eq(id))
                .fetchOneInto(User.class);
    }
}