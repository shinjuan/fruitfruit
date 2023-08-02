package com.example.project_03.admin;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface AdminMapper {


    void insertProduct(HashMap<String, Object> requestData);

    void insertPhoto(HashMap<String, Object> requestData);

    List<HashMap<String, Object>> selectProductList(HashMap<String, Object> requestData);
}
