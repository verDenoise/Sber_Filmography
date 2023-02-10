package com.example.filmography.MVC.controller;

import com.example.filmography.dto.UserDto;
import com.example.filmography.mapper.UserMapper;
import com.example.filmography.model.User;
import com.example.filmography.service.UserService;
import com.example.filmography.service.userDetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Hidden
@Controller
@RequestMapping("/users")
public class MVCUserController {

    private final UserService service;
    private final UserMapper mapper;

    public MVCUserController(UserService service, UserMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") UserDto userDto) {
        System.out.println(userDto.getId());
        service.create(mapper.toEntity(userDto));
        return "redirect:login";

    }

    @GetMapping("/profile/{id}")
    public String getProfile(@PathVariable Integer id, Model model) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!id.equals(customUserDetails.getUserId())) {
            return HttpStatus.FORBIDDEN.toString();
        }

        model.addAttribute("user", mapper.toDto(service.getOne(Long.valueOf(id))));
        return "profile/viewProfile";
    }

    @GetMapping("/profile/update/{id}")
    public String update(@PathVariable Integer id, Model model) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!id.equals(customUserDetails.getUserId())) {
            return HttpStatus.FORBIDDEN.toString();
        }
        model.addAttribute("user", mapper.toDto(service.getOne(Long.valueOf(id))));
        return "profile/updateProfile";
    }

    @PostMapping("/profile/update")
    public String update(@ModelAttribute("userForm") UserDto userDto) {
        User foundedUser = service.getOne(userDto.getId());
        foundedUser.setLogin(userDto.getLogin());
        foundedUser.setFirstName(userDto.getFirstName());
        foundedUser.setLastName(userDto.getLastName());
        foundedUser.setMiddleName(userDto.getMiddleName());
        foundedUser.setEmail(userDto.getEmail());
        foundedUser.setBirthDate(userDto.getBirthDate());
        foundedUser.setPhone(userDto.getPhone());
        foundedUser.setAddress(userDto.getAddress());
        userDto = mapper.toDto(foundedUser);
        service.update(mapper.toEntity(userDto));
        return "redirect:/users/profile/" + userDto.getId();
    }
}
