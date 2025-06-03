package com.ibra.projecttracker.controller;

import com.ibra.projecttracker.dto.Response;
import com.ibra.projecttracker.dto.DeveloperDTO;
import com.ibra.projecttracker.service.DeveloperService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/developer")
public class DeveloperController {
    private final DeveloperService developerService;

    public DeveloperController(DeveloperService developerService) {
        this.developerService = developerService;
    }


    @PostMapping("/create-developer")
    public ResponseEntity<Response> createDeveloper(@RequestBody DeveloperDTO developerDTO) {
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
    public ResponseEntity<Response> getDeveloperById(@PathVariable("id") Long id) {
        DeveloperDTO developerDTO = developerService.getDeveloperById(id);
        Response response = Response.builder()
                .message("Developer retrieved successfully")
                .statusCode(HttpStatus.OK.toString())
                .developer(developerDTO)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateDeveloper(@PathVariable("id") Long id, @RequestBody DeveloperDTO developerDTO) {
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

}
