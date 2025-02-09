package hubspot.practice.controller;

import hubspot.practice.model.Association;
import hubspot.practice.service.HubspotAssociationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/hubspot")
public class HubspotAssociationController {

    @Autowired
    private HubspotAssociationService hubspotAssociationService;

    @GetMapping("/validate-associations")
    public ResponseEntity<Map<String, List<Association>>> validateAssociations() {
        Map<String, List<Association>> validationResult = hubspotAssociationService.validateAssociations();
        return new ResponseEntity<>(validationResult, HttpStatus.OK);
    }
}
