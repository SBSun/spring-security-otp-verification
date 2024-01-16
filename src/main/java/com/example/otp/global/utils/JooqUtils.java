package com.example.otp.global.utils;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.jooq.tools.StringUtils;

import java.util.Objects;

public class JooqUtils {

    /**
     * equal 동적 쿼리
     */
    public static Condition eqIfNotEmpty(Field field, Object param) {
        if (Objects.isNull(param)) {
            return DSL.noCondition();
        }

        if (param instanceof String && StringUtils.isBlank(param.toString())) {
            return DSL.noCondition();
        }

        return field.eq(param);
    }
}
