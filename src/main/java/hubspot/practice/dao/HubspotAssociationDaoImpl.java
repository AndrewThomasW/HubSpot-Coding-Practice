package hubspot.practice.dao;

import hubspot.practice.model.Association;
import hubspot.practice.model.AssociationWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class HubspotAssociationDaoImpl implements HubspotAssociationDao {

    @Value("${hubspot.api.get.associations.url}")
    private String getAssociationsUrl;

    @Value("${hubspot.api.post.validation.url}")
    private String postValidationUrl;

    @Override
    public List<Association> getExistingAssociations() {
        RestTemplate restTemplate = new RestTemplate();
        AssociationWrapper response = restTemplate.getForObject(getAssociationsUrl, AssociationWrapper.class);
        return response.getExistingAssociations();
    }

    @Override
    public List<Association> getNewAssociations() {
        RestTemplate restTemplate = new RestTemplate();
        AssociationWrapper response = restTemplate.getForObject(getAssociationsUrl, AssociationWrapper.class);
        return response.getNewAssociations();
    }

    @Override
    public String postValidationResults(Map<String, List<Association>> results) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Map<String, List<Association>>> request = new HttpEntity<>(results);
        ResponseEntity<String> response = restTemplate.postForEntity(postValidationUrl, request, String.class);
        log.info("[Hubspot-Service] Post API response: {}", response);
        return response.getStatusCode().toString();
    }
}
