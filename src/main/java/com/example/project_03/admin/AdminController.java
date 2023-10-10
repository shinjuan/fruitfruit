package com.example.project_03.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import com.example.project_03.firebase.FireBaseService;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


import java.time.LocalDate;
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
    public String product(Model model, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {


        HashMap<String, Object> test = new HashMap<>();

        test.put("pageNum", pageNum);
        test.put("pageSize", pageSize);

//        PageHelper.startPage(pageNum, pageSize);


        HashMap<String, Object> count = adminService.countStatus();
        HashMap<String, Object> search_result = adminService.countProductAll();
        List<HashMap<String, Object>> list = adminService.selectProductListAll(test);



        PageInfo<HashMap<String, Object>> pageInfo = new PageInfo<>(list);

        log.info("카운트 : " + count);

        log.info("써치리절트 : " + search_result);

        log.info("페이지인포확인 : " + pageInfo);
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
    public HashMap<String, Object> product(@RequestBody HashMap<String, Object> requestData) {

        System.out.println("테스트" + requestData);

        int pageNum = (int) requestData.get("currentPage");
        int pageSize = (int) requestData.get("selectedTab");

        PageHelper.startPage(pageNum, pageSize);

        List<HashMap<String, Object>> data = adminService.selectProductList(requestData);
        PageInfo<HashMap<String, Object>> pageInfo = new PageInfo<>(data);
        int count = adminService.countProducts(requestData);

        HashMap<String, Object> data_count = new HashMap<>();

        data_count.put("data", data);
        data_count.put("count", count);
        data_count.put("pageInfo", pageInfo);

        log.info("페이지인포" + pageInfo);

        return data_count;
    }



        @GetMapping("/excel")
        public void downloadExcel(HttpServletResponse response) throws IOException {



            List<HashMap<String, Object>> list = adminService.selectProductListAll_excel(); // 여기에 데이터를 가져오세요

            // 엑셀 워크북 생성
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Product Data");

            // 열 헤더 생성
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("번호");
            headerRow.createCell(1).setCellValue("상태");
            headerRow.createCell(2).setCellValue("분류");
            headerRow.createCell(3).setCellValue("상품명");
            headerRow.createCell(4).setCellValue("상품금액");
            headerRow.createCell(5).setCellValue("할인율");
            //headerRow.createCell(6).setCellValue("찜 수");
            //headerRow.createCell(7).setCellValue("결제횟수");
            //headerRow.createCell(8).setCellValue("리뷰수");
            headerRow.createCell(9).setCellValue("등록일");
            headerRow.createCell(10).setCellValue("상품수정");
            //headerRow.createCell(11).setCellValue("판매중지");

            // 데이터 행 생성
            int rowNum = 1;
            for (HashMap<String, Object> rowData : list) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(String.valueOf(rowData.get("번호")));
                row.createCell(1).setCellValue(String.valueOf(rowData.get("상태")));
                row.createCell(2).setCellValue(String.valueOf(rowData.get("분류")));
                row.createCell(3).setCellValue(String.valueOf(rowData.get("상품명")));
                row.createCell(4).setCellValue(String.valueOf(rowData.get("상품금액")));
                row.createCell(5).setCellValue(String.valueOf(rowData.get("할인율")));
                //row.createCell(6).setCellValue(String.valueOf(rowData.get("찜 수")));
               // row.createCell(7).setCellValue(String.valueOf(rowData.get("결제횟수")));
                //row.createCell(8).setCellValue(String.valueOf(rowData.get("리뷰수")));
                row.createCell(9).setCellValue(String.valueOf(rowData.get("등록일")));
                row.createCell(10).setCellValue(String.valueOf(rowData.get("상품수정")));
                //row.createCell(11).setCellValue(String.valueOf(rowData.get("판매중지")));
            }

            // 엑셀 파일을 응답으로 전송
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=product_data.xlsx");
            workbook.write(response.getOutputStream());
            workbook.close();
        }

        @PostMapping("/admin/product_insert")
        public String product_insert(@RequestParam HashMap<String, Object> requestData,
                                     @RequestParam("productPicture") MultipartFile file) {


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

            return "redirect:/admin/product";

        }

        @ResponseBody
        @PostMapping("/admin/product_status")
        public int product_status(@RequestBody HashMap<String, Object> requestData) {


            if (requestData.get("selectedIds") != null) {

                adminService.saleStopList(requestData);
                return 1;
            }

            if (requestData.get("selectedIds2") != null) {

                adminService.productDelete(requestData);
                return 1;
            }

            if (requestData.get("selectedIds3") != null) {


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


    @GetMapping("/admin/review")
    public String review(Model model, @RequestParam HashMap<String,Object> reviewData) {




        LocalDate currentDate = LocalDate.now();

        // yyyy-MM-dd 형식의 문자열로 변환
        String formattedDate = currentDate.toString();

        // 모델에 날짜 문자열 추가
        model.addAttribute("date1", formattedDate);
        model.addAttribute("date2", formattedDate);


        List<HashMap<String,Object>> ReviewList = adminService.selectReviewListAll();

        model.addAttribute("ReviewList",ReviewList);

        return "admin/review";
    }

    @PostMapping("/review_serach")
    public String mypage_serach(HttpSession session, Model model,
                                @RequestParam("date1") String date1,
                                @RequestParam("date2") String date2,
                                @RequestParam("reviews") String searchType,
                                @RequestParam("howmanys") String howmanys,
                                @RequestParam("searchInput") String searchInput,
                                @RequestParam("buttonStatus") String buttonStatus
    ) {




        model.addAttribute("date1",date1);
        model.addAttribute("date2",date2);
        model.addAttribute("searchType",searchType);

//         날짜에 '00:00:00'와 '23:59:59' 추가
        date1 += " 00:00:00";
        date2 += " 23:59:59";

        System.out.println("date1="+date1);
        System.out.println("date2="+date2);
        System.out.println("reviews="+searchType);
        System.out.println("howmanys="+howmanys);
        System.out.println("buttonStatus="+buttonStatus);
        System.out.println("searchInput="+searchInput);

        HashMap<String,Object> review_search = new HashMap<>();

        review_search.put("date1",date1);
        review_search.put("date2",date2);
        review_search.put("howmanys",howmanys);
        review_search.put("searchInput",searchInput);
        review_search.put("reviews",searchType);
        review_search.put("buttonStatus",buttonStatus);
//
        List<HashMap<String,Object>> review_list = adminService.selectSearchReviewList(review_search);
//
//        PageInfo<HashMap<String, Object>> pageInfo = new PageInfo<>(payment_list);
//
//        System.out.println("마이페이지검색테스트:"+pageInfo);
//
//
        model.addAttribute("review_list", review_list);


        return "admin/review_search";
    }


    @RequestMapping("/admin/showReview")
    public String showReview(@RequestBody HashMap<String, Object> reviewId, Model model) {


        System.out.println("리뷰보기확인:"+reviewId);

        HashMap<String,Object> showReview = adminService.selectOneReview(reviewId);

        model.addAttribute("showReview", showReview);


        return "modal/admin/showReview";
    }

    @RequestMapping("/update/reviewReply")
    public String update_reviewReply(@RequestParam HashMap<String, Object> reviewData, Model model) {


        System.out.println("리뷰업데이트보기확인:"+reviewData);

        adminService.updateReviewReply(reviewData);
//
//        model.addAttribute("showReview", showReview);


        return "redirect:/admin/review";
    }


    }

