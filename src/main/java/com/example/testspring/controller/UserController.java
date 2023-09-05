package com.example.testspring.controller;

import com.example.testspring.dto.DepartmentDTO;
import com.example.testspring.dto.PageDTO;
import com.example.testspring.dto.SearchDTO;
import com.example.testspring.dto.UserDTO;
import com.example.testspring.services.DepartmentServices;
import com.example.testspring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import com.example.testspring.entity.User;
@Controller
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    DepartmentServices departmentServices;

    @GetMapping("/user/list")
    public String listUser(
            Model model
    ){
        List<UserDTO> usersDTO = userService.getAll();
        model.addAttribute("userList",usersDTO);
        model.addAttribute("searchDTO",new SearchDTO());
        return "user-list";
    }

    @GetMapping("/user/search")// copy lai userlist
    //require = false tuc la khong bat buoc cung duoc
    public String search(Model model,
                         @ModelAttribute  SearchDTO searchDTO,
                         BindingResult bindingResult
    )

    {

        if (bindingResult.hasErrors()){
            return "user-list.html"; //khi co loi xay ra return view
        }
        //TODO : KHi reqest quá nhiều param
        // thì tạo 1 class mới mình request cho nhanh


        PageDTO<List<UserDTO>> pageUser =
                userService.searchName(searchDTO);
        // get content no se tra ve listUser trong cai trang day
        model.addAttribute("userList",pageUser.getData());
        model.addAttribute("totalPages",pageUser.getTotalPages());
        //Lay tong so ban ghi
        model.addAttribute("totalElements",pageUser.getTotalElements());
        model.addAttribute("searchDTO",searchDTO);


        return "user-list.html";
    }

//    @GetMapping("/user/download")
//    public void download(
//            @RequestParam("fileName") String fileName,
//            HttpServletResponse response
//    ) throws IOException {
//        File file = new File("F:/"+fileName);
//        Files.copy(file.toPath(),response.getOutputStream());
//    }

    @GetMapping(value = "/user/new")
    public String newUser(Model model){
        PageDTO<List<DepartmentDTO>> pageDTO =
                departmentServices.search(new SearchDTO());
        model.addAttribute("user",new UserDTO());// day thong tin user qua view
        model.addAttribute("departmentList",pageDTO.getData());// day thong tin  qua view
        return "user-new.html";
    }

    @PostMapping(value = "/user/new")
//    Map như thế này khi mà tên thuộc tính của user trùng với name trong form
    public String newUser(
            @ModelAttribute("user") @Valid UserDTO userDTO,
            BindingResult bindingResult,
            Model model
    )throws IOException {
        if (bindingResult.hasErrors()){
            PageDTO<List<DepartmentDTO>> pageDTO =
                    departmentServices.search(new SearchDTO());
            model.addAttribute("departmentList",pageDTO.getData());// day thong tin  qua view

            return "user-new.html";
        }

//        if (!userDTO.getFile().isEmpty()){
//            String filename = userDTO.getFile().getOriginalFilename();
//            // Luu lai file vào ở cùng máy chủ
//            File saveFile = new File("F:/"+ filename);
//            userDTO.getFile().transferTo(saveFile);
//            //lay ten file luu xuong DATABASE
//            userDTO.setAvatarURL(filename);
//
//        }
        userService.create(userDTO);
        return "redirect:/user/list";
    }
    @GetMapping("/user/delete")
    public String delete(@RequestParam("id") int id){
        userService.delete(id);
        return "redirect:/user/list";
    }
    @GetMapping(value = "/user/update")
    public String update(@RequestParam("id") int id ,Model model){
       UserDTO userDTO = userService.getById(id);
       model.addAttribute("user",userDTO);// day thong tin user qua view
        PageDTO<List<DepartmentDTO>> pageDTO =
                departmentServices.search(new SearchDTO());
        model.addAttribute("departmentList",pageDTO.getData());
        return "user-update.html";
    }
    @PostMapping(value = "/user/update")
    public String update(@ModelAttribute("user") @Valid UserDTO userDTO,
                         BindingResult bindingResult,
                         Model model){
        if (bindingResult.hasErrors()){
            PageDTO<List<DepartmentDTO>> pageDTO =
                    departmentServices.search(new SearchDTO());
            model.addAttribute("departmentList",pageDTO.getData());// day thong tin  qua view
            return "user-update.html";
        }
        //Cach debug
        System.out.println(userDTO.getName());

        userService.update(userDTO);
        System.out.println(userDTO);
        return "redirect:/user/list";
    }

}
