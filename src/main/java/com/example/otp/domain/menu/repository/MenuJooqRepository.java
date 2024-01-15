package com.example.otp.domain.menu.repository;

import com.example.jooq.tables.MenuRelation;
import com.example.otp.domain.menu.dto.MenuResponseDto;
import lombok.RequiredArgsConstructor;
import org.jooq.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.jooq.tables.Menu.MENU;
import static com.example.jooq.tables.MenuRelation.MENU_RELATION;
import static org.jooq.impl.DSL.*;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuJooqRepository {

    private final DSLContext dslContext;

    /*
        특정 메뉴의 하위 메뉴들의 Id, Name, 상위 Menu Id를 조회
    */
    public List<MenuResponseDto.MenuInfo> getMenuHierarchy(Long menuId) {
        MenuRelation mr = MENU_RELATION.as("mr");

        // 해당하는 메뉴의 하위 메뉴들의 menu_id 값과 ancestor_id 값 추출
        Result<Record3<Long, String, Long>> menuHierarchy  = dslContext.withRecursive("menu_hierarchy", "id", "ancestorId")
                .as(
                        select(
                                mr.DESCENDANTID.as("id"),
                                mr.ANCESTORID)
                                .from(mr)
                                // menuId를 상위 메뉴로 가지는 연관 관계중에 1(자식)인 것들이거나
                                .where((mr.ANCESTORID.eq(menuId)
                                                .and(mr.DEPTH.eq(1L)))
                                        // menuId를 1(자식)로 가지는 연관 관계를 조회하여 찾으려는 menuId의 상위 메뉴 id값까지 추출
                                        .or((mr.DESCENDANTID.eq(menuId)
                                                .and(mr.DEPTH.eq(1L)))))
                                .unionAll(
                                        select(
                                                mr.DESCENDANTID.as("id"),
                                                mr.ANCESTORID)
                                                .from(mr)
                                                .innerJoin(table(name("menu_hierarchy")))
                                                // menu_hierarchy 테이블에 저장된 메뉴가 상위 메뉴에 해당하는 메뉴들 조회
                                                .on(mr.ANCESTORID.eq(field(name("menu_hierarchy", "id"), Long.class)))
                                                // 자식인 것만 조회
                                                .where(mr.ANCESTORID.eq(field(name("menu_hierarchy", "id"), Long.class))
                                                        .and(mr.DEPTH.eq(1L)))
                                )
                )
                .selectDistinct(
                        field(name("menu_hierarchy", "id"), Long.class),
                        MENU.MENU_NAME,
                        field(name("menu_hierarchy", "ancestorId"), Long.class)
                )
                .from(table(name("menu_hierarchy")))
                .innerJoin(MENU)
                .on(MENU.MENU_ID.eq(field(name("menu_hierarchy", "id"), Long.class)))
                .fetch();

        return menuHierarchy.into(MenuResponseDto.MenuInfo.class);
    }
}