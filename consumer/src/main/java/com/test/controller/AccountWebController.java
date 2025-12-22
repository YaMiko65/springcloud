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

    // [新增] 管理员重置指定用户密码
    @PostMapping("/admin/resetPwd")
    @Secured("ROLE_ADMIN")
    public String adminResetPwd(@RequestParam("username") String username,
                                @RequestParam("password") String password) {
        // 管理员修改他人密码，加密后直接调用服务
        String encodedPassword = passwordEncoder.encode(password);
        accountService.updatePassword(username, encodedPassword);
        // 修改完重定向回列表页
        return "redirect:/consumer/account/manage";
    }

    // [新增] 管理员修改用户状态 (1:正常, 0:禁用)
    @PostMapping("/admin/updateStatus")
    @Secured("ROLE_ADMIN")
    public String adminUpdateStatus(@RequestParam("username") String username,
                                    @RequestParam("valid") String valid) {
        accountService.updateStatus(username, valid);
        return "redirect:/consumer/account/manage";
    }
}