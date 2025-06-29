package com.titikkoma.taska.controller;

import java.util.List;

import com.titikkoma.taska.entity.CustomAuthPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.titikkoma.taska.base.WebResponse;
import com.titikkoma.taska.model.Organization;
import com.titikkoma.taska.service.OrganizationService;

@RestController
@RequestMapping("/v1/organizations")
public class OrganizationController {
    OrganizationService service;
    public OrganizationController(OrganizationService service){
        this.service = service;
    }

    @GetMapping
    public WebResponse<List<Organization>> findAll(){
        List<Organization> results = service.findAllOrganizations();
        return WebResponse.<List<Organization>>builder().data(results).build();
    }

    @GetMapping("/me")
    public WebResponse<Organization> me() {
        CustomAuthPrincipal principal = (CustomAuthPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Organization organization = service.findByCode(principal.getOrganizationCode());
        return WebResponse.<Organization>builder().data(organization).build();
    }

    @GetMapping("/{id}")
    public WebResponse<Organization> findById(@PathVariable String id) {
        Organization organization = service.findById(id);
        return WebResponse.<Organization>builder().data(organization).build();
    }

    @PostMapping
    public WebResponse<Organization> create(@RequestBody Organization organization) {
        Organization created = service.createOrganization(organization);
        return WebResponse.<Organization>builder().data(created).build();
    }
}
