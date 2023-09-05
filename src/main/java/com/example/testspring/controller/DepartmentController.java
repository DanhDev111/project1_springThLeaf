package com.example.testspring.controller;

import com.example.testspring.dto.DepartmentDTO;
import com.example.testspring.dto.PageDTO;
import com.example.testspring.dto.SearchDTO;
import com.example.testspring.services.DepartmentServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DepartmentController {
    @Autowired
    DepartmentServices departmentServices;
//    @GetMapping("/department/list")
//    public String listDepartment(Model model){
//        List<DepartmentDTO> departmentDTOList = departmentServices.readAll();
//        model.addAttribute("departmentList",departmentDTOList);
//        model.addAttribute("searchDTO",new SearchDTO());
//        return "department/department-list";
//    }
    @GetMapping("/department/search")
    public String search(Model model,
                         @ModelAttribute SearchDTO searchDTO,
                         BindingResult bindingResult){
        List<DepartmentDTO> departmentDTOList = departmentServices.readAll();
        model.addAttribute("departmentList",departmentDTOList);
        model.addAttribute("searchDTO",new SearchDTO());
        if (bindingResult.hasErrors()){
            return "department/department-list";
        }
        PageDTO<List<DepartmentDTO>> pageDepartment = departmentServices.search(searchDTO);
        model.addAttribute("departmentList",pageDepartment.getData());
        model.addAttribute("totalPages",pageDepartment.getTotalPages());
        model.addAttribute("totalElements",pageDepartment.getTotalElements());
        model.addAttribute("searchDTO",searchDTO);

        return "department/department-list";
    }
    @GetMapping("/department/new")
    public String newDepartment(Model model){
        model.addAttribute("department", new DepartmentDTO());// Day thong tin department qua view
        return "department/department-new";
    }
    @PostMapping("/department/new")
    public String newDepartment(
            @ModelAttribute("department") @Valid DepartmentDTO departmentDTO,
            BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "department/department-new";
        }
         departmentServices.create(departmentDTO);
        return "redirect:/department/search";
    }
    @GetMapping("/department/delete")
    public String deleteDepartment(@RequestParam("id") int id){
        departmentServices.delete(id);
        return "redirect:/department/search";
    }
    @GetMapping("/department/update")
    public String update(@RequestParam("id") int id ,
                         Model model){
        DepartmentDTO departmentDTO = departmentServices.getById(id);
        model.addAttribute("department",departmentDTO);
        return "department/department-update";
    }
    @PostMapping("/department/update")
    public String update(@ModelAttribute
                          DepartmentDTO departmentDTO){
        departmentServices.update(departmentDTO);
        return "redirect:/department/search";
    }
}
