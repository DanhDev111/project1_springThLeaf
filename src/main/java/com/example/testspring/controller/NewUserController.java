package com.example.testspring.controller;


import com.example.testspring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class NewUserController {

    // cho nay khong new nua
    // trong truong hop ma nhieu class hoac la doi tuong can phai co ten
    // 1 class nhieu doi tuogn hieu bien can chir ten bien nao can dung
//    @Autowired // DI map cai bean tao
//    UserService userService;
//    @GetMapping(value = "/user/new")
//    public String newUser(){
//        return "ticket-new.html";
//    }
//    // Phải có cái getMapping /user/list nữa nó ms đc
//    //có nghĩa em userservice bên kia find có cái gì lưu đâu mà redirect sang trang khác làm gì có dữ liệu
//    //mà là khi redirect sang url khác nó phải có controller để điều hướng khi browser của em chuyển sang url đó
//    //lưu thành công -> redirect tới /user/list -> ứng dụng spring tìm controller có GetMapping /user/list và trả ra
//    //trường hợp của em, sau khi redirect ->
//    // ứng dụng spring quét toàn bộ controller trong hệ thống ko thấy có cái nào GetMapping với url /user/list -> 404
//    @GetMapping("/user/list")
//    public String listUser(
//            Model model
//    ){
//        List<User> users = userService.getAll();
//        System.out.println(users.size());
//        model.addAttribute("userList",users);
//        return "user-list";
//    }
//    @PostMapping(value = "/user/new")
////    Map như thế này khi mà tên thuộc tính của user trùng với name trong form
//    public String newUser(
//            @ModelAttribute User user
//    ){
//
//        userService.create(user);
//        return "redirect:/user/list";
//    }
}
