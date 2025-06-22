package com.ibra.projecttracker.controller;

import com.ibra.projecttracker.dto.DeveloperDTO;
import com.ibra.projecttracker.dto.response.DeveloperSuccessResponse;
import com.ibra.projecttracker.service.DeveloperService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private DeveloperSuccessResponse buildDeveloperResponse(
            String message,
            HttpStatus status,
            DeveloperDTO developer,
            List<DeveloperDTO> developers,
            Page<DeveloperDTO> developerPage) {
        return DeveloperSuccessResponse.builder()
                .message(message)
                .statusCode(String.valueOf(status.value()))
                .developer(developer)
                .developers(developers)
                .developerPage(developerPage)
                .build();
    }


    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<DeveloperSuccessResponse> getAllDevelopers() {
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
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<DeveloperSuccessResponse> getDeveloperById(@Valid @PathVariable("id") Long id) {
        DeveloperDTO developerDTO = developerService.getDeveloperById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildDeveloperResponse(
                        "Developer retrieved successfully",
                        HttpStatus.OK,
                        developerDTO,
                        null,
                        null));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DeveloperSuccessResponse> deleteDeveloper(@PathVariable("id") Long id) {
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
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<DeveloperSuccessResponse> getDeveloperPageable(
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



    @GetMapping("/top5")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<DeveloperSuccessResponse> getTop5Developers() {
        List<DeveloperDTO> developerResponseDTOS = developerService.findTop5DevelopersWithMostTasksAssigned();
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildDeveloperResponse(
                        "Top 5 developers retrieved successfully",
                        HttpStatus.OK,
                        null,
                        developerResponseDTOS,
                        null));
    }
}