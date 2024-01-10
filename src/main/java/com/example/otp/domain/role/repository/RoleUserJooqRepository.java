package com.example.otp.domain.role.repository;

import com.example.jooq.tables.MenuRelation;
import com.example.otp.domain.role.dto.RoleResponseDto;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.jooq.tables.MenuRelation.MENU_RELATION;
import static com.example.jooq.tables.Role.ROLE;
import static com.example.jooq.tables.RoleMenu.ROLE_MENU;
import static com.example.jooq.tables.RoleUser.ROLE_USER;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoleUserJooqRepository {

    private final DSLContext dslContext;

    /*
        특정 대상에게 설정한 기능들을 사용할 수 있는 권한 부여
    */
    @Transactional
    public void addRolesToUser(Long userId, List<Long> roleIds) {

        List<Long> saveRoleIds = new ArrayList<Long>();

        // 사용자가 이미 사용가능한 기능이거나 role 테이블에 해당 roleId가 존재하지 않을 경우 X
        for (Long id : roleIds) {
            boolean isNotSaved = dslContext
                    .fetchExists(
                            dslContext.selectOne()
                                    .from(ROLE_USER, ROLE)
                                    .where((ROLE_USER.USER_ID.eq(userId).and(ROLE_USER.ROLE_ID.eq(id)))
                                            .orNot(ROLE.ROLE_ID.eq(id)))
                    );

            if (!isNotSaved) {
                saveRoleIds.add(id);
            }
        }

        if (saveRoleIds.isEmpty()) {
            return;
        }

        var insertRows = saveRoleIds.stream()
                        .map(register -> DSL.row(
                                userId,
                                register
                        ))
                        .collect(Collectors.toList());

        dslContext.insertInto(ROLE_USER, ROLE_USER.USER_ID, ROLE_USER.ROLE_ID)
                .valuesOfRows(insertRows)
                .execute();
    }

    /*
        특정 사용자가 사용가능한 기능 조회
    */
    public List<RoleResponseDto.RoleInfo> getRolesByUser(Long userId) {
        return dslContext
                .select(ROLE.ROLE_ID, ROLE.ROLE_NAME)
                .from(ROLE)
                    // 해당 사용자가 사용 가능한 기능들만 조회
                    .join(ROLE_USER)
                    .on(ROLE_USER.USER_ID.eq(userId))
                .where(ROLE.ROLE_ID.eq(ROLE_USER.ROLE_ID))
                .fetchInto(RoleResponseDto.RoleInfo.class);
    }

    /*
        선택한 3뎁스 메뉴의 기능중 사용자가 사용가능한 기능
    */
    public List<RoleResponseDto.RoleInfo> getRolesByUserAndMenu(Long userId, Long menuId) {
        return dslContext
                .select(ROLE.ROLE_ID, ROLE.ROLE_NAME)
                .from(ROLE)
                    // 해당 사용자가 사용 가능한 기능들만 조회
                    .join(ROLE_USER)
                        .on(ROLE_USER.USER_ID.eq(userId))
                    // 선택한 3뎁스의 메뉴만 조회
                    .join(ROLE_MENU)
                        .on(ROLE_MENU.MENU_ID.eq(menuId))
                // 해당 사용자가 사용 가능한 기능인지
                .where(ROLE.ROLE_ID.eq(ROLE_USER.ROLE_ID)
                        // 해당 기능이 선택한 3뎁스 메뉴의 기능인지
                        .and(ROLE_USER.ROLE_ID.eq(ROLE_MENU.ROLE_ID)))
                .fetchInto(RoleResponseDto.RoleInfo.class);
    }

    /*
        해당 메뉴가 3Depth가 맞는지 여부
    */
    public boolean is3DepthMenu(Long menuId) {
        MenuRelation r = MENU_RELATION.as("r");

        return dslContext.fetchExists(
                dslContext
                        .selectOne()
                        .from(r)
                        // 계층 관계에서 3Depth에 해당하는 자식 메뉴인지
                        .where(r.DEPTH.eq(3L)
                                .and(r.DESCENDANTID.eq(menuId)))
        );
    }
}
