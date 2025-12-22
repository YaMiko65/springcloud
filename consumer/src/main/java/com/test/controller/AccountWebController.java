package com.test.controller;

import com.test.pojo.UserDto;
import com.test.service.RemoteAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication; // [新增] 导入 Authentication
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler; // [新增] 导入 LogoutHandler
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest; // [新增] 导入 Request
import javax.servlet.http.HttpServletResponse; // [新增] 导入 Response
import java.util.List;

@Controller
@RequestMapping("/consumer/account")
public class AccountWebController {

    @Autowired
    private RemoteAccountService accountService;

    // [2] 注入 PasswordEncoder (由 LoginSecurityConfig 配置的 BCryptPasswordEncoder)
    @Autowired
    private PasswordEncoder passwordEncoder;

    // 跳转重置密码页面
    @GetMapping("/reset")
    public String toResetPage() {
        return "account_reset";
    }

    // 执行重置密码
    @PostMapping("/doReset")
    public String doReset(@RequestParam("password") String password,
                          Model model,
                          HttpServletRequest request,
                          HttpServletResponse response) { // [修改] 增加 request 和 response 参数

        // 获取当前登录认证信息（需要在注销前获取）
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // [3] 关键修改：在更新前先对密码进行加密
        String encodedPassword = passwordEncoder.encode(password);

        // 发送加密后的密码给后端服务
        boolean success = accountService.updatePassword(username, encodedPassword);

        if (success) {
            // [新增] 密码修改成功后，手动执行注销操作
            if (auth != null) {
                // SecurityContextLogoutHandler 会清除 SecurityContext 并使 Session 失效
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }

            model.addAttribute("msg", "密码重置成功，请重新登录！");
            return "login"; // 返回登录页面视图
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
}