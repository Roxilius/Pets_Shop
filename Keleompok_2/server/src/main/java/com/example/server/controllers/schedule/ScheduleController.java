package com.example.server.controllers.schedule;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.server.data_transfer_object.GenericResponse;
import com.example.server.services.schedule.ScheduleService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/schedule")
@Tag(name = "schedule")
@CrossOrigin(origins = "http://localhost:5173/")
@Slf4j
public class ScheduleController {
    @Autowired
    ScheduleService scheduleService;
    @GetMapping("/daily")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> daily(){
        try {
            
            return ResponseEntity.ok().body(GenericResponse.success(scheduleService.daily(), "Successfully"));
        } catch (ResponseStatusException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Internal Server Error!"));
        }
    }
    @GetMapping("/monthly")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> monthly(){
        try {
            return ResponseEntity.ok().body(GenericResponse.success(scheduleService.monthly(), "Successfully"));
        } catch (ResponseStatusException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Internal Server Error!"));
        }
    }
    @GetMapping("/annual")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> annual(){
        try {
            
            return ResponseEntity.ok().body(GenericResponse.success(scheduleService.annual(), "Successfully"));
        } catch (ResponseStatusException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Internal Server Error!"));
        }
    }

    @GetMapping("/devition")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> devition(LocalDate startDate, LocalDate endDate){
        try {
            return ResponseEntity.ok().body(GenericResponse.success(scheduleService.deviation(startDate, endDate), "Successfully"));
        } catch (ResponseStatusException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Internal Server Error!"));
        }
    }

    @GetMapping("/income-expenses")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> incomeExpenses(LocalDate startDate, LocalDate endDate){
        try {
            return ResponseEntity.ok().body(GenericResponse.success(scheduleService.incomeExpenses(startDate, endDate), "Successfully"));
        } catch (ResponseStatusException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Internal Server Error!"));
        }
    }


}
