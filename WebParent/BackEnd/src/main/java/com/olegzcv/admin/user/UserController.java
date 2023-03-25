package com.olegzcv.admin.user;

import com.olegzcv.common.entity.Role;
import com.olegzcv.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/users")
    public String usersList(Model model) {
        List<User> users = service.getAllUsers();
        model.addAttribute("usersList", users);
        return "users";
    }

    @GetMapping("users/new")
    public String addNewUser(Model model) {
        User user = new User();
        user.setEnabled(true); // The enabled status is TRUE by default in model
        model.addAttribute("user", user);
        List<Role> roles = service.getRoles();
        model.addAttribute("rolesList", roles);
        return "user_form";
    }

    @PostMapping("users/save")
    public String saveUser(User user, RedirectAttributes redirect) {
        System.out.println("NEW USER: " + user);
        service.save(user);
        redirect.addFlashAttribute("message", "Great! New user in the team");
        return "redirect:/users";
    }


}
