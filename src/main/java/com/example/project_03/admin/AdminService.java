package com.example.project_03.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService implements AdminMapper {

    private final AdminMapper adminMapper;


    @Override
    public void insertProduct(HashMap<String, Object> requestData) {}

    @Override
    public void insertPhoto(HashMap<String, Object> requestData) {}

    @Override
    public List<HashMap<String, Object>> selectProductList(HashMap<String, Object> requestData) {
        return adminMapper.selectProductList(requestData);
    }

    @Transactional
    public void insertProductAll(HashMap<String, Object> requestData) {
        adminMapper.insertProduct(requestData);
        adminMapper.insertPhoto(requestData);
    }
}
