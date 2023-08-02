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


        return "admin/" + pageName;
    }

    @GetMapping("/admin/product")
    public String count(Model model) {

        HashMap<String,Object> count = adminService.countStatus();
        List<HashMap<String, Object>> list = adminService.selectProductListAll();

        model.addAttribute("count", count);
        model.addAttribute("list", list);

        System.out.println("카운트="+count);

        return "admin/product";
    }


    @ResponseBody
    @PostMapping("/admin/product")
    public List<HashMap<String, Object>> product(@RequestBody HashMap<String, Object> requestData, Model model) {



        List<HashMap<String, Object>> data = adminService.selectProductList(requestData);




        return data;
    }

    @PostMapping("/admin/product_insert")
    public String product_insert(@RequestParam HashMap<String, Object> requestData,
                                 @RequestParam("productPicture") MultipartFile file) {


        String path = "friut"; // 원하는 이미지 저장 경로를 입력하세요 (예: "products")
        String fileName = requestData.get("productName") + "_" + file.getOriginalFilename();
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


        return "admin/product";
    }
    @ResponseBody
    @PostMapping("/admin/product_status")
    public int product_status(@RequestBody HashMap<String, Object> requestData) {


        if(requestData.get("selectedIds")!=null) {

            adminService.saleStopList(requestData);
            return 1;
        }

        if(requestData.get("selectedIds2")!=null) {

            adminService.productDelete(requestData);
            return 1;
        }

        if(requestData.get("selectedIds3")!=null) {


            adminService.saleStop(requestData);
            return 1;
        }


        return 0;
    }

}