package ru.gb.gbshopmart.web;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.gb.gbshopmart.service.UserService;
import ru.gb.gbshopmart.web.dto.UserDto;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "auth/login-form";
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "auth/registration-form";
    }

    @PostMapping("/register")
    public String handleRegistration(@Valid UserDto userDto, BindingResult bindingResult, Model model) {
        String username = userDto.getUsername();
        log.debug("Process registration username: {}", username);
        if (bindingResult.hasErrors()) {
            return "auth/registration-form";
        }
        try {
            userService.findByUsername(username);
            model.addAttribute("user", userDto);
            model.addAttribute("registrationError", "Пользователь с таким логином уже существует");
            return "auth/registration-form";
        } catch (UsernameNotFoundException ignored) {} // проверка если пользователя нет
        userService.register(userDto);
        model.addAttribute("username", username);
        return "auth/registration-confirmation";

    }
}
