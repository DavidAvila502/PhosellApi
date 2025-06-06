package com.dev.phosell.sessionpackage.infrastructure.adapter.in;


import com.dev.phosell.sessionpackage.application.service.FindAllSessionsPackagesService;
import com.dev.phosell.sessionpackage.domain.model.SessionPackage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/packages")
public class SessionPackageController {

    private final FindAllSessionsPackagesService findAllSessionsPackagesService;

    public SessionPackageController(
            FindAllSessionsPackagesService findAllSessionsPackagesService
    ){
        this.findAllSessionsPackagesService = findAllSessionsPackagesService;
    }

    @GetMapping
    public List<SessionPackage> findAll(){
        return  findAllSessionsPackagesService.findAll();
    }
}
