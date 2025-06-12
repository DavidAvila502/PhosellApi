package com.dev.phosell.user.infrastructure.adapter.in;

import com.dev.phosell.user.application.dto.UserFiltersDto;
import com.dev.phosell.user.application.service.FindUsersByFiltersService;
import com.dev.phosell.user.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final FindUsersByFiltersService findUsersByFiltersService;

    public  UserController(FindUsersByFiltersService findUsersByFiltersService){
        this.findUsersByFiltersService = findUsersByFiltersService;
    }

    @GetMapping
    public ResponseEntity<Page<User>> findByFilters(
            @ModelAttribute UserFiltersDto userFiltersDto,
            Pageable pageable
    ){
        Page<User> usersFound = findUsersByFiltersService.findByFilters(userFiltersDto,pageable);

        return ResponseEntity.ok().body(usersFound);
    }
}
