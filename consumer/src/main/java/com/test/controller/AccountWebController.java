package com.test.controller;

import com.test.pojo.UserDto;
import com.test.service.RemoteAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/consumer/account")
public class AccountWebController {

    @Autowired
    private RemoteAccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 跳转重置密码页面 (个人)
    @GetMapping("/reset")
    public String toResetPage() {
        return "account_reset";
    }

    // 执行重置密码 (个人)
    @PostMapping("/doReset")
    public String doReset(@RequestParam("password") String password,
                          Model model,
                          HttpServletRequest request,
                          HttpServletResponse response) {

        if (password == null || password.length() < 6) {
            model.addAttribute("error", "修改失败：新密码长度不能少于6位");
            return "account_reset";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        String encodedPassword = passwordEncoder.encode(password);
        boolean success = accountService.updatePassword(username, encodedPassword);

        if (success) {
            if (auth != null) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }
            model.addAttribute("msg", "密码重置成功，请重新登录！");
            return "login";
        } else {
            model.addAttribute("error", "重置失败，请稍后重试");
            return "account_reset";
        }
    }

    // 管理员查看所有账号
    @GetMapping("/manage")
    @Secured("ROLE_ADMIN")
    public String manageUsers(Model model) {
        List<UserDto> users = accountService.getAllUsers();
        model.addAttribute("users", users);
        return "account_list";
    }

    // 管理员重置指定用户密码
    @PostMapping("/admin/resetPwd")
    @Secured("ROLE_ADMIN")
    public String adminResetPwd(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                RedirectAttributes redirectAttributes) {

        if (password == null || password.length() < 6) {
            redirectAttributes.addFlashAttribute("error", "操作失败：用户 [" + username + "] 的新密码长度不能少于6位");
            return "redirect:/consumer/account/manage";
        }

        try {
            String encodedPassword = passwordEncoder.encode(password);
            accountService.updatePassword(username, encodedPassword);
            redirectAttributes.addFlashAttribute("msg", "操作成功：用户 [" + username + "] 的密码已重置");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "操作失败：系统异常");
        }

        return "redirect:/consumer/account/manage";
    }

    // 管理员修改用户状态
    @PostMapping("/admin/updateStatus")
    @Secured("ROLE_ADMIN")
    public String adminUpdateStatus(@RequestParam("username") String username,
                                    @RequestParam("valid") String valid) {
        accountService.updateStatus(username, valid);
        return "redirect:/consumer/account/manage";
    }

    // [修改] 管理员添加用户 (增加校验逻辑)
    @PostMapping("/admin/add")
    @Secured("ROLE_ADMIN")
    public String adminAddUser(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               RedirectAttributes redirectAttributes) { // 注入 RedirectAttributes

        // 1. 校验用户名是否为空
        if (username == null || username.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "添加失败：用户名不能为空");
            return "redirect:/consumer/account/manage";
        }

        // 2. 校验密码长度
        if (password == null || password.length() < 6) {
            redirectAttributes.addFlashAttribute("error", "添加失败：密码长度不能少于6位");
            return "redirect:/consumer/account/manage";
        }

        try {
            UserDto user = new UserDto();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setValid("1"); // 默认启用

            boolean success = accountService.addUser(user);

            if (success) {
                redirectAttributes.addFlashAttribute("msg", "添加成功：用户 [" + username + "] 已创建");
            } else {
                // 如果 Dao 层检测到重复返回 0，service 返回 false，进入这里
                redirectAttributes.addFlashAttribute("error", "添加失败：用户 [" + username + "] 已存在或系统繁忙");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "添加失败：系统异常");
            e.printStackTrace();
        }

        return "redirect:/consumer/account/manage";
    }

    // 管理员删除用户
    @PostMapping("/admin/delete")
    @Secured("ROLE_ADMIN")
    public String adminDeleteUser(@RequestParam("id") Integer id) {
        accountService.deleteUser(id);
        return "redirect:/consumer/account/manage";
    }
}