package org.dabs.front.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.dabs.back.entity.User;
import org.dabs.back.model.bind.ChangePasswordModel;
import org.dabs.back.service.UserService;
import org.dabs.back.validation.Errors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLoginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", Errors.INVALID_CREDENTIALS);
        }
        return "login";
    }

    @GetMapping("/user/change-password")
    public String getRegisterPage(@ModelAttribute ChangePasswordModel changePasswordModel) {
        return "change-password";
    }

    @PostMapping("/user/change-password")
    public String registerUser(@Valid @ModelAttribute ChangePasswordModel changePasswordModel, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "change-password";
        }

        long userId = ((User)((Authentication) principal).getPrincipal()).getId();
        changePasswordModel.setUserId(userId);

        boolean oldPasswordEqualsNew = this.userService.updatePassword(changePasswordModel);
        if (!oldPasswordEqualsNew) {
            FieldError fieldError = new FieldError("changePasswordModel", "oldPassword", "Old password is invalid");

            bindingResult.addError(fieldError);

            return "change-password";
        }

        return "redirect:/";
    }
}
