package com.ibra.projecttracker.controller;

import com.ibra.projecttracker.dto.Response;
import com.ibra.projecttracker.dto.DeveloperDTO;
import com.ibra.projecttracker.entity.Developer;
import com.ibra.projecttracker.repository.DeveloperRepository;
import com.ibra.projecttracker.service.DeveloperService;
import com.ibra.projecttracker.service.TaskService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/developer")
public class DeveloperController {
    private final DeveloperService developerService;
    private final DeveloperRepository developerRepository;
    private final TaskService taskService;

    public DeveloperController(DeveloperService developerService, DeveloperRepository developerRepository, TaskService taskService) {
        this.developerService = developerService;
        this.developerRepository = developerRepository;
        this.taskService = taskService;
    }


    @PostMapping("/create-developer")
    public ResponseEntity<Response> createDeveloper(@Valid @RequestBody DeveloperDTO developerDTO) {
        DeveloperDTO newDeveloper = developerService.createDeveloper(developerDTO);
        Response response = Response.builder()
                .message("Developer created successfully")
                .statusCode(String.valueOf(HttpStatus.CREATED))
                .developer(newDeveloper)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/get-all")
    public ResponseEntity<Response> getAllDevelopers() {
        List<DeveloperDTO> developerDTOs = developerService.getAllDevelopers();
        Response response = Response.builder()
                .message("All developers retrieved successfully")
                .statusCode(String.valueOf(HttpStatus.OK))
                .developers(developerDTOs)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getDeveloperById(@Valid @PathVariable("id") Long id) {
        DeveloperDTO developerDTO = developerService.getDeveloperById(id);
        Response response = Response.builder()
                .message("Developer retrieved successfully")
                .statusCode(HttpStatus.OK.toString())
                .developer(developerDTO)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateDeveloper(@PathVariable("id") Long id, @Valid @RequestBody DeveloperDTO developerDTO) {
        DeveloperDTO updateDeveloper = developerService.updateDeveloper(id, developerDTO);
        Response response = Response.builder()
                .message("Developer updated successfully")
                .statusCode(HttpStatus.NO_CONTENT.toString())
                .developer(updateDeveloper)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteDeveloper(@PathVariable("id") Long id) {
        developerService.deleteDeveloper(id);
        Response response = Response.builder()
                .message("Developer deleted successfully")
                .statusCode(HttpStatus.NO_CONTENT.toString())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Response> getDeveloperPageable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "developerId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Page<DeveloperDTO> developersPage = developerService.getDevelopersPageable(page, size, sortBy, sortDirection);
        Response response = Response.builder()
                .message("All developers retrieved successfully")
                .statusCode(String.valueOf(HttpStatus.OK))
                .developerPage(developersPage)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

     @GetMapping("/developers/top5")
     public ResponseEntity<Response> getTop5Developers() {
         List<DeveloperDTO> developerDTOs = developerService.findTop5DevelopersWithMostTasksAssigned();
         Response response = Response.builder()
                 .message("All developers retrieved successfully")
                 .statusCode(String.valueOf(HttpStatus.OK))
                 .developers(developerDTOs)
                 .build();

         return ResponseEntity.status(HttpStatus.OK).body(response);
     }

   }
