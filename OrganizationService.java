package com.titikkoma.taska.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.titikkoma.taska.model.Organization;
import com.titikkoma.taska.repository.OrganizationRepository;

@Service
public class OrganizationService {
    private OrganizationRepository repository;
    public OrganizationService(
        OrganizationRepository repository
    ){
        this.repository = repository;
    }
    public List<Organization> findAllOrganizations(){
        Map<String , Object> orgCond = new HashMap<>();
        return repository.findAll(orgCond);
    }
    public Organization findById(String id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "organization_not_found"));
    }
    @Transactional
    public Organization createOrganization(Organization org) {
        return repository.create(org);
    }
    @Transactional
    public Organization updateOrganization(String id, Organization updatedOrg) {
        updatedOrg.setId(id); // pastikan ID konsisten
        int updated = repository.updateOrganization(updatedOrg);

        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "update_failed");
        }

        return findById(id); // return versi terbaru
    }

    @Transactional
    public void deleteOrganization(String id) {
        int deleted = repository.deleteOrganization(id);
        if (deleted == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "delete_failed");
        }
    }
}
