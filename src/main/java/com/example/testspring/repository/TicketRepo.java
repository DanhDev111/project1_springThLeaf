package com.example.testspring.repository;

import com.example.testspring.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface TicketRepo extends JpaRepository<Ticket,Integer> {
    //SearchByClientName
    @Query("SELECT t FROM Ticket t WHERE t.clientName LIKE :x")
    Page<Ticket> searchByName(@Param("x") String s, Pageable pageable);

    //SearchByDate
    @Query("SELECT t FROM Ticket t WHERE t.createdAt >= :start AND t.createdAt <= :end")
    Page<Ticket> searchByDate(@Param("start") Date start,@Param("end") Date end, Pageable pageable);

    //SearchBydepartmentId
//    @Query("SELECT t FROM Ticket t JOIN t.department d " +
//            "WHERE d.id = :x OR t.department.name = :x")

    // ---------------------------------------------

//    @Query("SELECT t FROM Ticket t  WHERE t.department.id = :x")
   @Query("SELECT t FROM Ticket t JOIN t.department d WHERE d.id = :departmentId")
    Page<Ticket> searchByDepartmentId(@Param("departmentId") int departmentId,Pageable pageable);

    //findBystatus
    Page<Ticket> findByStatus(boolean status, Pageable pageable);
}
