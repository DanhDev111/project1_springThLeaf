package com.example.testspring.controller;

import com.example.testspring.dto.*;
import com.example.testspring.entity.Ticket;
import com.example.testspring.repository.TicketRepo;
import com.example.testspring.services.DepartmentServices;
import com.example.testspring.services.TicketService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
public class TicketController {

    @Autowired
    TicketRepo ticketRepo;

    @Autowired
    TicketService ticketService;

    @Autowired
    DepartmentServices departmentServices;

//    @GetMapping("/ticket/list")
//    public String listAll(Model model){
//        List<TicketDTO> ticketDTOList = ticketService.readAll();
//        model.addAttribute("ticketList",ticketDTOList);
//
//        PageDTO<List<DepartmentDTO>> page = departmentServices.search(new SearchDTO());
//        model.addAttribute("departmentList",page.getData());
//        model.addAttribute("searchDTO",new SearchDTO());
//        return "ticket/ticket-list";
//    }
    @GetMapping("/ticket/search")
    public String search(
            Model model,
            @ModelAttribute SearchTicketDTO searchDTO,
            BindingResult bindingResult
    ){
        List<TicketDTO> ticketDTOList = ticketService.readAll();
        model.addAttribute("ticketList",ticketDTOList);

        PageDTO<List<DepartmentDTO>> page = departmentServices.search(new SearchDTO());
        model.addAttribute("departmentList",page.getData());
        model.addAttribute("searchDTO",new SearchDTO());
        if (bindingResult.hasErrors()){
            return "ticket/ticket-list";
        }
        PageDTO<List<TicketDTO>> pageTicket = ticketService.search(searchDTO);

        model.addAttribute("ticketList",pageTicket.getData());
        model.addAttribute("totalPages",pageTicket.getTotalPages());
        model.addAttribute("totalElements",pageTicket.getTotalElements());
        model.addAttribute("searchDTO",searchDTO);
        return "ticket/ticket-list";
    }
    @GetMapping(value = "/ticket/new")
    public String ticketNew(
            Model model
    ){
        PageDTO<List<DepartmentDTO>> page = departmentServices.search(new SearchDTO());
        model.addAttribute("ticket",new TicketDTO());
        model.addAttribute("departmentList",page.getData());
        return "ticket/ticket-new.html";
    }
    @PostMapping(value = "/ticket/new")
    public String ticketNew(
            @ModelAttribute("ticket") @Valid TicketDTO ticketDTO,
            Model model,
            BindingResult bindingResult
    ){
//        System.out.println(ticketDTO.getClientName());
        if (bindingResult.hasErrors()){
            // Lấy theo page để có thể số lượng phân trang
            PageDTO<List<DepartmentDTO>> pageDTO =
                    departmentServices.search(new SearchDTO());
            model.addAttribute("departmentList",pageDTO.getData());// day thong tin  qua view
            return "ticket/ticket-new.html";
        }
        ticketService.create(ticketDTO);
        System.out.println(ticketDTO.getClientName());
        System.out.println(ticketDTO);
        return "redirect:/ticket/search";
    }
    @GetMapping("/ticket/delete")
    public String delete(@RequestParam("id") int id){
        ticketService.delete(id);
        return "redirect:/ticket/search";
    }

    @GetMapping("/ticket/update")
    public String update(Model model,
                         @RequestParam("id") int id
    ){
        TicketDTO ticketDTO = ticketService.getById(id);
        model.addAttribute("ticket",ticketDTO);

        PageDTO<List<DepartmentDTO>> pageDTO = departmentServices.search(new SearchDTO());
        model.addAttribute("departmentList",pageDTO.getData());
        return "ticket/ticket-update";
    }
    @PostMapping("/ticket/update")
    public String update(Model model,
                         @ModelAttribute("ticket") @Valid TicketDTO ticketDTO,
                         BindingResult bindingResult
                         )
    {
        if (bindingResult.hasErrors()){
            PageDTO<List<DepartmentDTO>> pageDTO =
                    departmentServices.search(new SearchDTO());
            model.addAttribute("departmentList",pageDTO.getData());// day thong tin  qua view
            return "ticket/ticket-update";
        }
        ticketService.update(ticketDTO);
        return "redirect:/ticket/search";
    }
}
