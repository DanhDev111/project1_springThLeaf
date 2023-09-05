package com.example.testspring.services;

import com.example.testspring.dto.DepartmentDTO;
import com.example.testspring.dto.PageDTO;
import com.example.testspring.dto.SearchDTO;
import com.example.testspring.dto.UserDTO;
import com.example.testspring.entity.Department;
import com.example.testspring.entity.User;
import com.example.testspring.repository.DepartmentRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface DepartmentServices {
    void create(DepartmentDTO departmentDTO);

    void update(DepartmentDTO departmentDTO);

    void delete(int id);

    List<DepartmentDTO> readAll();

    DepartmentDTO getById(int id);

    PageDTO<List<DepartmentDTO>> search(SearchDTO searchDTO);
}
@Service
class DepartmentServicesImpl implements DepartmentServices{
    @Autowired
    DepartmentRepo departmentRepo;
    @Override
    @Transactional
    public void create(DepartmentDTO departmentDTO) {
        //Viet thế này đỡ phải set
        Department department = new ModelMapper().map(departmentDTO,Department.class);
        departmentRepo.save(department);
    }

    @Override
    @Transactional
    public void update(DepartmentDTO departmentDTO) {
        Department currentDepartment = departmentRepo.findById(departmentDTO.getId()).orElse(null);
        if (currentDepartment!=null){
            currentDepartment.setName(departmentDTO.getName());
            departmentRepo.save(currentDepartment);
        }

    }

    @Override
    public void delete(int id) {
        departmentRepo.deleteById(id);
    }

    @Override
    public List<DepartmentDTO> readAll() {
        List<Department> departments = departmentRepo.findAll();
        List<DepartmentDTO> departmentDTOList = new ArrayList<>();
        for (Department department : departments){
            departmentDTOList.add(convert(department));
        }
        return departmentDTOList;
    }

    @Override
    public DepartmentDTO getById(int id) {
        Department department = departmentRepo.findById(id)
                        .orElseThrow(NoResultException::new);

        return convert(department);
    }
    public DepartmentDTO convert(Department department){
        return new ModelMapper().map(department, DepartmentDTO.class);
    }

    @Override
    public PageDTO<List<DepartmentDTO>> search(SearchDTO searchDTO) {

        Sort sortBy = Sort.by("name").ascending();

//        if (sortBy !=null && !sortBy.isEmpty()){
//
//        }
        if (StringUtils.hasText(searchDTO.getSortedField())){
            sortBy=  Sort.by(searchDTO.getSortedField()).descending();
        }
        if (searchDTO.getCurrentPage()==null){
            searchDTO.setCurrentPage(0);
        }
        if (searchDTO.getSize()==null){
            searchDTO.setSize(5);
        }
        if (searchDTO.getKeyword()==null){
            searchDTO.setKeyword("");
        }
        PageRequest pageRequest =
                PageRequest.of(searchDTO.getCurrentPage(),searchDTO.getSize(),sortBy);
        Page<Department> page = departmentRepo.searchName("%"+searchDTO.getKeyword()+"%", pageRequest);

        PageDTO<List<DepartmentDTO>> pageDTO = new PageDTO<>();
        pageDTO.setTotalPages(page.getTotalPages());
        pageDTO.setTotalElements(page.getTotalElements());

        List<DepartmentDTO> departmentDTOS =
                page.get().map(u ->convert(u)).collect(Collectors.toList());
        pageDTO.setData(departmentDTOS);
//        List<User> users =  page.getContent();
        // hoac la viet the nay
//        return userRepo.searchByName("%"+name+"%");
        return pageDTO;
    }
}
