package com.example.testspring.services;

import com.example.testspring.dto.*;
import com.example.testspring.entity.Ticket;
import com.example.testspring.repository.DepartmentRepo;
import com.example.testspring.repository.TicketRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface TicketService {
    void create(TicketDTO ticketDTO);

    void update(TicketDTO ticketDTO);

    void delete(int id);

    List<TicketDTO> readAll();

    TicketDTO getById(int id);

    PageDTO<List<TicketDTO>> search(SearchTicketDTO searchDTO);
}
@Service
class TicketServiceImpl implements TicketService {
    @Autowired
    TicketRepo ticketRepo;

    @Autowired
    DepartmentRepo departmentRepo;

    @Transactional
    @Override
    public void create(TicketDTO ticketDTO) {
        Ticket ticket = new ModelMapper().map(ticketDTO, Ticket.class);
        ticketRepo.save(ticket);
    }

    @Override
    @Transactional
    public void update(TicketDTO ticketDTO) {
        Ticket currentTicket = ticketRepo.findById(ticketDTO.getId()).orElse(null);
        if (currentTicket != null) {
//            currentTicket.setContent(ticketDTO.getContent());
            currentTicket = new ModelMapper().map(ticketDTO, Ticket.class);
            currentTicket.setCreatedAt(ticketDTO.getCreatedAt());
            currentTicket.setDepartment(departmentRepo.findById(ticketDTO.getDepartment().getId()).orElse(null));
        }
        ticketRepo.save(currentTicket);
    }

    @Override
    @Transactional
    public void delete(int id) {
        ticketRepo.deleteById(id);
    }

    @Override
    public List<TicketDTO> readAll() {
        List<Ticket> ticketList = ticketRepo.findAll();
        List<TicketDTO> ticketDTOList = new ArrayList<>();
        for (Ticket ticket : ticketList) {
            ticketDTOList.add(convert(ticket));
        }
        return ticketDTOList;
    }

    @Override
    public TicketDTO getById(int id) {
        Ticket ticket = ticketRepo.findById(id).orElse(null);
        if (ticket != null) {
            return convert(ticket);
        }
        return null;
    }

    public TicketDTO convert(Ticket ticket) {
        return new ModelMapper().map(ticket, TicketDTO.class);
    }

    @Override
    public PageDTO<List<TicketDTO>> search(SearchTicketDTO searchDTO) {
        Sort sortBy = Sort.by("clientName").ascending().and(Sort.by("createdAt")).descending();

        // StringUtils.hasText để kiểm tra xem người ta có nhập khoảng trắng hay không
        if (StringUtils.hasText(searchDTO.getSortedField())) {
            sortBy = Sort.by(searchDTO.getSortedField()).descending();
        }
        if (searchDTO.getCurrentPage() == null) {
            searchDTO.setCurrentPage(0);
        }
        if (searchDTO.getSize() == null) {
            searchDTO.setSize(5);
        }
        if(searchDTO.getKeyword()== null){
            searchDTO.setKeyword("");
        }
        PageRequest pageRequest = PageRequest.of(searchDTO.getCurrentPage(), searchDTO.getSize(), sortBy);
        Page<Ticket> page = ticketRepo.findAll(pageRequest);
        // Chú ý chỗ này nếu if nhiều nó page nó sẽ ghi đè lại hết
        // Cần else if
        if (searchDTO.getStart() != null && searchDTO.getEnd() != null) {
            page = ticketRepo.searchByDate(searchDTO.getStart(), searchDTO.getEnd(), pageRequest);
        }
        else if (searchDTO.getDepartmentId()!=null){
            page = ticketRepo.searchByDepartmentId(searchDTO.getDepartmentId(),pageRequest);
        }
        else if (searchDTO.getKeyword() !=null){
            page = ticketRepo.searchByName("%"+searchDTO.getKeyword()+"%",pageRequest);
        }


        PageDTO<List<TicketDTO>> pageDTO = new PageDTO<>();
        pageDTO.setTotalPages(page.getTotalPages());
        pageDTO.setTotalElements(page.getTotalElements());

        //Convert sang từ Entity sang DTO
        List<TicketDTO> ticketDTO = page.get().map(u -> convert(u)).collect(Collectors.toList());

        pageDTO.setData(ticketDTO);
        return pageDTO;
    }
}
