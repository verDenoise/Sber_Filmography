package com.example.filmography.MVC.controller;

import com.example.filmography.dto.RememberPasswordDto;
import com.example.filmography.dto.UserDto;
import com.example.filmography.mapper.UserMapper;
import com.example.filmography.model.User;
import com.example.filmography.service.UserService;
import com.example.filmography.service.userDetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Objects;


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
        if (service.isUniqueLoggin(userDto.getLogin())) {
            service.create(mapper.toEntity(userDto));
            return "redirect:/login";
        }
        return "redirect:/login?error=Loggin is not unique";
    }

    @GetMapping("/profile/{id}")
    public String getProfile(@PathVariable Integer id, Model model) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!Objects.isNull(customUserDetails.getUserId())) {
            if (!id.equals(customUserDetails.getUserId())) {
                return HttpStatus.FORBIDDEN.toString();
            }
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

    @GetMapping("/list")
    public String userFilms(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int pageSize,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<User> userPage = service.listAllPaginated(pageRequest);
        List<UserDto> userDtos = userPage
                .stream()
                .map(mapper::toDto)
                .toList();
        model.addAttribute("users", new PageImpl<>(userDtos, pageRequest, userPage.getTotalElements()));
        return "users/viewAllUsers";
    }

    @GetMapping("/ban/{userId}")
    public String banUser(@PathVariable Long userId) {
        service.banUser(userId);
        return "redirect:/users/list";
    }

    @GetMapping("/unban/{userId}")
    public String unbanUser(@PathVariable Long userId) {
        service.unbanUser(userId);
        return "redirect:/users/list";
    }

    @GetMapping("/add-admin")
    public String createAdmin(@ModelAttribute("userForm") UserDto userDto) {
        return "users/addAdmin";
    }

    @PostMapping("add-admin")
    public String createAdmin(@ModelAttribute("userForm") @Valid UserDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            return "/users/addAdmin";
        } else {
            service.createAdmin(mapper.toEntity(userDto));
            return "redirect:/users/list";
        }
    }

    @GetMapping("/remember-password")
    public String rememberPassword() {
        return "rememberPassword";
    }

    @PostMapping("/remember-password")
    public String rememberPassword(@ModelAttribute("email") RememberPasswordDto rememberPasswordDto) {
        UserDto userDto = mapper.toDto(service.getUserByEmail(rememberPasswordDto.getEmail()));
        if (Objects.isNull(userDto)) {
            return "redirect:/login?error=User with email "+rememberPasswordDto.getEmail()+" not found";
        } else {
            service.sendChangePasswordEmail(userDto.getEmail());
            return "redirect:/login";
        }
    }

    @GetMapping("/change-password")
    public String changePassword(@PathParam(value = "uuid") String uuid, Model model) {
        model.addAttribute("uuid", uuid);
        return "changePassword";
    }


    @PostMapping("/change-password")
    public String changePassword(@PathParam(value = "uuid") String uuid, @ModelAttribute("changePasswordForm") UserDto userDto) {
        service.changePassword(uuid, userDto.getPassword());
        return "redirect:/login";
    }

}
