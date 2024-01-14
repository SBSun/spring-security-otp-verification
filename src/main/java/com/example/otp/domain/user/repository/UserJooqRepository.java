package com.example.otp.domain.user.repository;

import com.example.otp.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.jooq.tables.User.USER;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserJooqRepository {

    private final DSLContext dslContext;

    public List<User> getAllUser(){
        return dslContext
                .selectFrom(USER)
                .fetchInto(User.class);
    }

    public Optional<User> findByAccountId(String accountId) {

        User user = dslContext
                .selectFrom(USER)
                .where(USER.ACCOUNT_ID.eq(accountId))
                .fetchOneInto(User.class);

        return Optional.ofNullable(user);
    }

    public User findById(Long id) {
        return dslContext
                .selectFrom(USER)
                .where(USER.USER_ID.eq(id))
                .fetchOneInto(User.class);
    }
}