package com.example.project_03.admin;

import lombok.RequiredArgsConstructor;
import com.example.project_03.firebase.FireBaseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final FireBaseService fireBaseService;


    @GetMapping("admin/favicon.ico")
    @ResponseBody
    void noFavicon() {
    }


    @GetMapping("/admin/{pageName}")
    public String login(@PathVariable String pageName) {


        return  "admin/"+pageName;
    }

    @ResponseBody
    @PostMapping("/admin/product")
    public List<HashMap<String,Object>> product(@RequestBody HashMap<String,Object> requestData, Model model) {

        System.out.println("상태="+requestData.get("selectedStatus"));
        System.out.println("카테고리="+requestData.get("selectedCategory"));
        System.out.println("검색어="+requestData.get("searchKeyword"));
        System.out.println("갯수보기="+requestData.get("selectedTab"));

        List<HashMap<String,Object>> data = adminService.selectProductList(requestData);

        //model.addAttribute("product",data);


        return data;
    }

    @PostMapping("/admin/product_insert")
    public String product_insert(@RequestParam HashMap<String,Object> requestData,
                                 @RequestParam("productPicture") MultipartFile file) {



        String path = "friut"; // 원하는 이미지 저장 경로를 입력하세요 (예: "products")
        String fileName = requestData.get("productName")+"_"+file.getOriginalFilename();
        String imageUrl;
        try {
            imageUrl = fireBaseService.uploadFiles(file, path, fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }

        // origin_name과 file_name에 이미지 정보 저장
        String originName = fileName; // 로컬경로 대신 원래 파일 이름을 저장 (파일명 중복 방지)
        String firebaseUrl = imageUrl; // Firebase에서 받아온 이미지 URL
        String fileSize = String.valueOf(file.getSize());
        // 여기서부터 데이터베이스에 저장하는 로직 작성


        requestData.put("originName", originName);
        requestData.put("firebaseUrl", firebaseUrl);
        requestData.put("fileSize", fileSize);



        adminService.insertProductAll(requestData);




        return "admin/index";
    }





}
