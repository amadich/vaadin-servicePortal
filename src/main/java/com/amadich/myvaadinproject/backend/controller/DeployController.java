package com.amadich.myvaadinproject.backend.controller;

import com.amadich.myvaadinproject.backend.model.DeployRequest;
import com.amadich.myvaadinproject.backend.service.KubernetesService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deploy")
public class DeployController {

    private final KubernetesService kubernetesService;

    public DeployController(KubernetesService kubernetesService) {
        this.kubernetesService = kubernetesService;
    }

    @PostMapping
    public String deploy(@RequestBody DeployRequest request) {

        kubernetesService.deployService(request);

        return "Service deployed";
    }
}
