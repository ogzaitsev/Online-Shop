package com.olegzcv.admin.user;

import com.olegzcv.common.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository repository;

    @Test
    public void testCreateAdminRole() {
        Role adminRole = new Role("Admin", "Manage everything");
        Role savedRole = repository.save(adminRole);
        assertThat(savedRole.getId()).isGreaterThan(0);
    }

    @Test
    public void testAddAllRoles() {
        Role adminRole = new Role("Admin", "Manage everything");
        Role salespersonRole = new Role("Salesperson",
                "Manage product price, customers, shipping, orders and sales report");
        Role editorRole = new Role("Editor",
                "Manage categories, brands, products, articles, menus");
        Role shipperRole = new Role("Shipper",
                "Manage order status, view products and orders");
        Role assistantRole = new Role("Assistant", "Manage questions and reviews");

        Iterable<Role> savedRoles = new ArrayList<>();
        savedRoles = repository
                .saveAll(List.of(adminRole, salespersonRole, editorRole, shipperRole, assistantRole));
        for(Role role : savedRoles) {
            assertThat(role.getId()).isGreaterThan(0);
        }
    }

}
