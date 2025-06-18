package com.ibra.projecttracker.controller;

import com.ibra.projecttracker.dto.response.DeveloperResponse;
import com.ibra.projecttracker.dto.DeveloperDTO;
import com.ibra.projecttracker.service.DeveloperService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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

    private DeveloperResponse buildDeveloperResponse(
            String message,
            HttpStatus status,
            DeveloperDTO developer,
            List<DeveloperDTO> developers,
            Page<DeveloperDTO> developerPage) {
        return DeveloperResponse.builder()
                .message(message)
                .statusCode(String.valueOf(status.value()))
                .developer(developer)
                .developers(developers)
                .developerPage(developerPage)
                .build();
    }

    @PostMapping
    public ResponseEntity<DeveloperResponse> createDeveloper(@Valid @RequestBody DeveloperDTO developerDTO) {
        DeveloperDTO newDeveloper = developerService.createDeveloper(developerDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(buildDeveloperResponse(
                        "Developer created successfully",
                        HttpStatus.CREATED,
                        newDeveloper,
                        null,
                        null));
    }

    @GetMapping
    public ResponseEntity<DeveloperResponse> getAllDevelopers() {
        List<DeveloperDTO> developerDTOs = developerService.getAllDevelopers();
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildDeveloperResponse(
                        "All developers retrieved successfully",
                        HttpStatus.OK,
                        null,
                        developerDTOs,
                        null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeveloperResponse> getDeveloperById(@Valid @PathVariable("id") Long id) {
        DeveloperDTO developerDTO = developerService.getDeveloperById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildDeveloperResponse(
                        "Developer retrieved successfully",
                        HttpStatus.OK,
                        developerDTO,
                        null,
                        null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeveloperResponse> updateDeveloper(@PathVariable("id") Long id, @Valid @RequestBody DeveloperDTO developerDTO) {
        DeveloperDTO updateDeveloper = developerService.updateDeveloper(id, developerDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildDeveloperResponse(
                        "Developer updated successfully",
                        HttpStatus.OK,
                        updateDeveloper,
                        null,
                        null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeveloperResponse> deleteDeveloper(@PathVariable("id") Long id) {
        developerService.deleteDeveloper(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildDeveloperResponse(
                        "Developer deleted successfully",
                        HttpStatus.OK,
                        null,
                        null,
                        null));
    }

    @GetMapping("/paginated")
    public ResponseEntity<DeveloperResponse> getDeveloperPageable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "developerId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Page<DeveloperDTO> developersPage = developerService.getDevelopersPageable(page, size, sortBy, sortDirection);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildDeveloperResponse(
                        "All developers retrieved successfully",
                        HttpStatus.OK,
                        null,
                        null,
                        developersPage));
    }

    @GetMapping("/developers/top5")
    public ResponseEntity<DeveloperResponse> getTop5Developers() {
        List<DeveloperDTO> developerDTOs = developerService.findTop5DevelopersWithMostTasksAssigned();
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildDeveloperResponse(
                        "Top 5 developers retrieved successfully",
                        HttpStatus.OK,
                        null,
                        developerDTOs,
                        null));
    }
}