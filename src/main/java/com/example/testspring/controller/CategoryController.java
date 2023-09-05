package com.example.testspring.controller;


import com.example.testspring.client.dto.dto.CategoryDTO;
import com.example.testspring.client.dto.service.WSCategoryService;


import com.example.testspring.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;

@Controller
public class CategoryController {
    @Autowired
    WSCategoryService categoryService;

    @GetMapping(value = "/category/new")
    public String newUser(Model model){
        model.addAttribute("category",new CategoryDTO());// day thong tin category qua view
        return "category-new.html";
    }

    @PostMapping(value = "/category/new")
    public String newUser(
            @ModelAttribute("category") @Valid CategoryDTO categoryDTO,
            HttpSession session,
            BindingResult bindingResult
            )throws IOException
    {
        if (bindingResult.hasErrors()){
            return "category-new.html";
        }
        String token = (String) session.getAttribute("token");

        categoryService.createCategory(categoryDTO,token);

        return "redirect:/category/search";
    }

//    @GetMapping("/user/delete")
//    public String delete(@RequestParam("id") int id){
//        userService.delete(id);
//        return "redirect:/user/list";
//    }
//    @GetMapping(value = "/user/update")
//    public String update(@RequestParam("id") int id ,Model model){
//        UserDTO userDTO = userService.getById(id);
//        model.addAttribute("user",userDTO);// day thong tin user qua view
//        PageDTO<List<DepartmentDTO>> pageDTO =
//                departmentServices.search(new SearchDTO());
//        model.addAttribute("departmentList",pageDTO.getData());
//        return "user-update.html";
//    }
//    @PostMapping(value = "/user/update")
//    public String update(@ModelAttribute("user") @Valid UserDTO userDTO,
//                         BindingResult bindingResult,
//                         Model model){
//        if (bindingResult.hasErrors()){
//            PageDTO<List<DepartmentDTO>> pageDTO =
//                    departmentServices.search(new SearchDTO());
//            model.addAttribute("departmentList",pageDTO.getData());// day thong tin  qua view
//            return "user-update.html";
//        }
//        //Cach debug
//        System.out.println(userDTO.getName());
//
//        userService.update(userDTO);
//        System.out.println(userDTO);
//        return "redirect:/user/list";
//    }
}
