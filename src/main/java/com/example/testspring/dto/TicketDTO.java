package com.example.testspring.dto;

import com.example.testspring.entity.Department;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class TicketDTO {
    // để là Integer có thể check null được

    private Integer id;

    @NotBlank(message = "{not.blank}")
    private String clientName;
    @Size(min = 10,max = 18,message ="{size.password}" )
    private String clientPhone;

    // .....

    private String content;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date createdAt;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date processDate;

    private boolean status;

    private DepartmentDTO department;


}
