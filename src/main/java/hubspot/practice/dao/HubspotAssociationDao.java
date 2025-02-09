package hubspot.practice.dao;

import hubspot.practice.model.Association;

import java.util.List;
import java.util.Map;

public interface HubspotAssociationDao {

    List<Association> getExistingAssociations();

    List<Association> getNewAssociations();

    String postValidationResults(Map<String, List<Association>> results);
}
