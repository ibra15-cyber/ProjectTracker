package com.ibra.projecttracker.controller;

import com.ibra.projecttracker.dto.DeveloperResponse;
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
@RequestMapping("/api/v1/developers")
public class DeveloperController {
    private final DeveloperService developerService;

    public DeveloperController(DeveloperService developerService) {
        this.developerService = developerService;
    }


    @PostMapping
    public ResponseEntity<DeveloperResponse> createDeveloper(@Valid @RequestBody DeveloperDTO developerDTO) {
        DeveloperDTO newDeveloper = developerService.createDeveloper(developerDTO);
        DeveloperResponse response = DeveloperResponse.builder()
                .message("Developer created successfully")
                .statusCode(String.valueOf(HttpStatus.CREATED))
                .developer(newDeveloper)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<DeveloperResponse> getAllDevelopers() {
        List<DeveloperDTO> developerDTOs = developerService.getAllDevelopers();
        DeveloperResponse response = DeveloperResponse.builder()
                .message("All developers retrieved successfully")
                .statusCode(String.valueOf(HttpStatus.OK))
                .developers(developerDTOs)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeveloperResponse> getDeveloperById(@Valid @PathVariable("id") Long id) {
        DeveloperDTO developerDTO = developerService.getDeveloperById(id);
        DeveloperResponse response = DeveloperResponse.builder()
                .message("Developer retrieved successfully")
                .statusCode(HttpStatus.OK.toString())
                .developer(developerDTO)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeveloperResponse> updateDeveloper(@PathVariable("id") Long id, @Valid @RequestBody DeveloperDTO developerDTO) {
        DeveloperDTO updateDeveloper = developerService.updateDeveloper(id, developerDTO);
        DeveloperResponse response = DeveloperResponse.builder()
                .message("Developer updated successfully")
                .statusCode(HttpStatus.NO_CONTENT.toString())
                .developer(updateDeveloper)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeveloperResponse> deleteDeveloper(@PathVariable("id") Long id) {
        developerService.deleteDeveloper(id);
        DeveloperResponse response = DeveloperResponse.builder()
                .message("Developer deleted successfully")
                .statusCode(HttpStatus.NO_CONTENT.toString())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/paginated")
    public ResponseEntity<DeveloperResponse> getDeveloperPageable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "developerId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Page<DeveloperDTO> developersPage = developerService.getDevelopersPageable(page, size, sortBy, sortDirection);
        DeveloperResponse response = DeveloperResponse.builder()
                .message("All developers retrieved successfully")
                .statusCode(String.valueOf(HttpStatus.OK))
                .developerPage(developersPage)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/developers/top5")
    public ResponseEntity<DeveloperResponse> getTop5Developers() {
        List<DeveloperDTO> developerDTOs = developerService.findTop5DevelopersWithMostTasksAssigned();
        DeveloperResponse response = DeveloperResponse.builder()
                .message("All developers retrieved successfully")
                .statusCode(String.valueOf(HttpStatus.OK))
                .developers(developerDTOs)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
