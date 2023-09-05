package com.example.testspring.client.dto.service;

import com.example.testspring.client.dto.dto.CategoryDTO;
import com.example.testspring.client.dto.dto.ResponseDTO;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.NoResultException;

@Service
public class WSCategoryService {
    public ResponseDTO<CategoryDTO> createCategory(CategoryDTO categoryDTO, String token) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CategoryDTO> entity = new HttpEntity<>(categoryDTO, headers);

        ResponseEntity<ResponseDTO> response =
                restTemplate.exchange("http://localhost:8000/admin/category/",
                        HttpMethod.POST, entity,
                        ResponseDTO.class);

        if (response.getStatusCode() == HttpStatus.OK)
            return response.getBody();
        else throw new NoResultException();


    }
}
