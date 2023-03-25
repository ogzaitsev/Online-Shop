package com.olegzcv.admin.user;

import com.olegzcv.common.entity.Role;
import com.olegzcv.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
    @Autowired
    private UserRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateAdminUser() {
        Role adminRole = entityManager.find(Role.class, 1);
        User adminUser = new User("oleg.zaitsev@proton.me",
                                "admin", "Oleg Zaitsev");
        adminUser.addRole(adminRole);

        assertThat(adminRole.getName()).isEqualTo("Admin");

        User savedUser = repository.save(adminUser);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateTwoRoleUser() {
        Role editorRole = entityManager.find(Role.class, 3);
        Role assistantRole = entityManager.find(Role.class, 5);
        User testUser = new User("voitenko@mail.ru", "12345", "Igor Voitenko");
        testUser.addRoles(editorRole, assistantRole);

        assertThat(editorRole.getName()).isEqualTo("Editor");
        assertThat(assistantRole.getName()).isEqualTo("Assistant");

        User savedUser = repository.save(testUser);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers() {
        Iterable<User> users = repository.findAll();
        Iterator<User> iterator = users.iterator();
        long i = 0;
        while(iterator.hasNext()) {
            iterator.next();
            i++;
        }
        assertThat(i).isGreaterThan(0);
        assertThat(repository.count()).isEqualTo(i);
        users.forEach(user -> System.out.println(user));
    }

    @Test
    public void testGetUserById() {
        User returned = repository.findById(1).get();
        System.out.println(returned);
        assertThat(returned).isNotNull();
    }

    @Test
    public void testUpdateUserDetails() {
        User userToUpdate1 = repository.findById(1).get();
        userToUpdate1.setEnabled(true);
        User savedUser1 = repository.save(userToUpdate1);

        assertThat(savedUser1.isEnabled()).isEqualTo(userToUpdate1.isEnabled());

        User userToUpdate2 = repository.findById(2).get();
        userToUpdate2.setEnabled(true);
        userToUpdate2.setEmail("igor_voitenko@gmail.com");
        User savedUser2 = repository.save(userToUpdate2);

        assertThat(savedUser2.isEnabled()).isEqualTo(userToUpdate2.isEnabled());
        assertThat(savedUser2.getEmail()).isEqualTo(userToUpdate2.getEmail());
    }

    @Test
    public void testUpdateUserRoles() {
        User userToUpdate = repository.findById(2).get();
        Role salesperson = entityManager.find(Role.class, 2);
        Role assistant = entityManager.find(Role.class, 5);

        userToUpdate.getRoles().remove(salesperson);
        userToUpdate.addRole(assistant);

        User updated = repository.save(userToUpdate);

        Iterator<Role> iterator = userToUpdate.getRoles().iterator();
        while (iterator.hasNext()) {
            assertThat(updated.getRoles().contains(iterator.next())).isEqualTo(true);
        }
    }

    @Test
    public void testGetUserByEmail() {
        String email = "oleg.zaitsev@proton.me";
        User userByEmail = repository.getUserByEmail(email);
        assertThat(userByEmail).isNotNull();
        assertThat(userByEmail.getUsername()).isEqualTo("Oleg Zaitsev");
    }
}
