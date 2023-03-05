package com.example.filmography.MVC.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class MyErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request) {
        log.error("Error happened: " + request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
        return "error";
    }

    @GetMapping("/error-with-message")
    public String handleError(@RequestParam(value = "message") String message, Model model) {
        model.addAttribute("message", message);
        return "error";
    }
}
