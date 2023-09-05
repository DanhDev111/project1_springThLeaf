package com.example.testspring.entity;

import lombok.Data;
import lombok.Generated;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

//ORM framework: Object - Record table
// JPA - Hibernate
// mình sẽ không dùng JDBC SQL nữa
@Data //mình dùng cái này để có thể muốn generate constructor với get and set method
@Table (name = "user") //map to table SQL
@Entity //BEAN new project
public class User {
    //MAP
    //1.Att
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
//  Many users to one department
    // Khi de many to one thi mac dinh se tu gen ra department_id
    @ManyToOne //(cascade = CascadeType.ALL) // bat buoc 1 kieu du lieu la 1 entity khác
//    @JoinColumn(name ="department_id")
    private Department department;
    private String name;
    //luu ten filePath
    private String avatarURL;

    private int age;
    //vi username canf them rang buoc la unique
    //Neu trong db dat khac thi quy tac dat phai nhu nay _
    //@Column(name="u_name")
    @Column(unique = true)
    private String username;

    private String password;
//    Gia su mac dinh name trong table sql se la (home_address)
// KHONG CAN DAT NAME
    private String homeAddress;

    @Temporal(TemporalType.DATE)
    private Date birthDate;
    
}
