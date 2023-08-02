package com.example.project_03;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
public class MainController {

    @GetMapping("")
    public String hello() {
        System.out.println("test");
        return "index";
    }



    /* admin page */
    @GetMapping("/admin")
    public String adminIndex() {


        return "admin/index";
    }





}
