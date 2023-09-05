package com.example.testspring.controller;

import com.example.testspring.client.dto.dto.LoginUserDTO;
import com.example.testspring.client.dto.dto.ResponseDTO;
import com.example.testspring.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@Slf4j
public class LoginController {
    @Autowired
    UserRepo userRepo;

    @GetMapping(value = "/login")
    public String login() {
        return "login.html";
    }

    @PostMapping("/login")
    // Thay vì phải có HttpServletRequest với response giống với cái jsp ta có thể viết như thế này
    public String login(HttpSession session,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password) throws IOException {

        ResponseEntity<ResponseDTO> response = remoteLogin(username, password);
        if (response.getStatusCode() == HttpStatus.OK) {
//            HttpSession session = request.getSession();
            ResponseDTO responseDTO = response.getBody();// cai nay ko phai string nhe
            String token = (String) responseDTO.getData();
            /* là cái này này
            {
    "status": 200,
    "msg": "OK",
    "data": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzeXNhZG1pbiIsImlhdCI6MTY5MzMxOTg2NSwiZXhwIjoxNjkzMzIzNDY1fQ.MGa-dIEW14FM8Fp0vz1WqGRKbuLAT1oHB6F456iE2xs"
}
             */
            session.setAttribute("username", username);
            session.setAttribute("token",token);

//            ResponseEntity<LoginUserDTO> resp = getMe(token);
            LoginUserDTO loginUserDTO = getMe(token);
            session.setAttribute("user",loginUserDTO);
            return "redirect:/hello";
        } else {
            return "redirect:/login"; // mình cho nó reload lại trang
        }
    }
    private ResponseEntity<ResponseDTO> remoteLogin(String username, String password) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        LinkedMultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("username", username);
        requestBody.add("password", password);

        HttpEntity<LinkedMultiValueMap<String, String>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange("http://localhost:8000/login", HttpMethod.POST, entity
                , ResponseDTO.class);

        return response;
    }


    private LoginUserDTO getMe(String token){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
//        LinkedMultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<LoginUserDTO> response = restTemplate.exchange("http://localhost:8000/me", HttpMethod.GET,entity,LoginUserDTO.class);
        if (response.getStatusCode()==HttpStatus.OK)

        return response.getBody();
        else throw new NoResultException();

    }

}
