package com.example.project_03.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import com.example.project_03.firebase.FireBaseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@Log4j2
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
    public String product(Model model,@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {


        HashMap<String,Object> test = new HashMap<>();

        test.put("pageNum",pageNum);
        test.put("pageSize",pageSize);

//        PageHelper.startPage(pageNum, pageSize);


        HashMap<String,Object> count = adminService.countStatus();
        HashMap<String,Object> search_result = adminService.countProductAll();
        List<HashMap<String, Object>> list = adminService.selectProductListAll(test);




        PageInfo<HashMap<String, Object>> pageInfo = new PageInfo<>(list);

        log.info("카운트 : "+count);

        log.info("써치리절트 : "+search_result);

        log.info("페이지인포확인 : "+pageInfo);
        //System.out.println("페이지인포?:"+pageInfo);

        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("search_result", search_result);
        model.addAttribute("count", count);
        model.addAttribute("list", list);

       // System.out.println("카운트="+search_result);

        return "admin/product";
    }



    @ResponseBody
    @PostMapping("/admin/product")
    public HashMap<String, Object> product(@RequestBody HashMap<String, Object> requestData){

        System.out.println("테스트"+requestData);

        int pageNum = (int)requestData.get("currentPage");
        int pageSize = (int)requestData.get("selectedTab");

        PageHelper.startPage(pageNum,pageSize);

        List<HashMap<String, Object>> data = adminService.selectProductList(requestData);
        PageInfo<HashMap<String, Object>> pageInfo = new PageInfo<>(data);
        int count = adminService.countProducts(requestData);

        HashMap<String,Object> data_count = new HashMap<>();

        data_count.put("data",data);
        data_count.put("count",count);
        data_count.put("pageInfo",pageInfo);

        log.info("페이지인포"+pageInfo);

        return data_count;
    }

    @PostMapping("/admin/product_insert")
    public String product_insert(@RequestParam HashMap<String, Object> requestData,
                                 @RequestParam("productPicture") MultipartFile file){



        String path = "fruit"; // 원하는 이미지 저장 경로를 입력하세요 (예: "products")
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

        return "/admin/product";
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


    @PostMapping("/upload_image_to_firebase")
    public ResponseEntity<Map<String, String>> uploadImageToFirebase(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        try {
            String imageUrl = fireBaseService.uploadFiles(file, "tinymce_images", file.getOriginalFilename());
            response.put("location", imageUrl); // 이미지 URL을 "location" 키로 반환합니다.
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }










}