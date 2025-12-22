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
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // [新增] 引入重定向属性类

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
        // 注意：FlashAttribute 中的 error/msg 会自动合并到 Model 中，
        // 所以这里不需要额外处理，页面可以直接使用 ${error}
        return "account_list";
    }

    // [修改] 管理员重置指定用户密码
    @PostMapping("/admin/resetPwd")
    @Secured("ROLE_ADMIN")
    public String adminResetPwd(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                RedirectAttributes redirectAttributes) { // [新增] 注入 RedirectAttributes

        // [新增] 1. 校验密码长度
        if (password == null || password.length() < 6) {
            // 使用 addFlashAttribute 在重定向后传递数据
            redirectAttributes.addFlashAttribute("error", "操作失败：用户 [" + username + "] 的新密码长度不能少于6位");
            return "redirect:/consumer/account/manage";
        }

        try {
            String encodedPassword = passwordEncoder.encode(password);
            accountService.updatePassword(username, encodedPassword);
            // [新增] 2. 添加成功提示
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

    // 管理员添加用户
    @PostMapping("/admin/add")
    @Secured("ROLE_ADMIN")
    public String adminAddUser(@RequestParam("username") String username,
                               @RequestParam("password") String password) {
        UserDto user = new UserDto();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setValid("1");
        accountService.addUser(user);
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