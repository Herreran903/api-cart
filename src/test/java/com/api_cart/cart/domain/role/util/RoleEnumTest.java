package com.api_cart.cart.domain.role.util;

import org.apache.commons.lang3.EnumUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RoleEnumTest {
    @Test
    void testRoleEnum() {
        assertTrue(EnumUtils.isValidEnum(RoleEnum.class, "ROLE_ADMIN"));
        assertTrue(EnumUtils.isValidEnum(RoleEnum.class, "ROLE_CLIENT"));
        assertTrue(EnumUtils.isValidEnum(RoleEnum.class, "ROLE_WAREHOUSE_ASSISTANT"));

        assertFalse(EnumUtils.isValidEnum(RoleEnum.class, "ROLE_UNKNOWN"));
    }
}