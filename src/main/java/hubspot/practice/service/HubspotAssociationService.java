package hubspot.practice.service;


import hubspot.practice.dao.HubspotAssociationDao;
import hubspot.practice.model.Association;
import hubspot.practice.model.InvalidAssociation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HubspotAssociationService {

    @Autowired
    private HubspotAssociationDao hubspotAssociationDao;

    private static final int MAX_CONTACTS_PER_ROLE = 5;
    private static final int MAX_ROLES_PER_CONTACT = 2;

    public Map<String, List<Association>> validateAssociations() {
        List<Association> existingAssociations = hubspotAssociationDao.getExistingAssociations();

        List<Association> newAssociations = hubspotAssociationDao.getNewAssociations();

        Map<String, Map<String, Integer>> companyRoleCount = new HashMap<>();
        Map<String, Map<String, Integer>> contactRoleCount = new HashMap<>();
        Set<Association> existingSet = new HashSet<>(existingAssociations);

        List<Association> validAssociations = new ArrayList<>();
        List<InvalidAssociation> invalidAssociations = new ArrayList<>();

        existingAssociations.forEach(assoc -> {
            companyRoleCount.computeIfAbsent(assoc.getCompanyId(), k -> new HashMap<>())
                    .merge(assoc.getRole(), 1, Integer::sum);
            contactRoleCount.computeIfAbsent(assoc.getContactId(), k -> new HashMap<>())
                    .merge(assoc.getCompanyId(), 1, Integer::sum);
        });

        for (Association newAssoc : newAssociations) {
            if (existingSet.stream().anyMatch(assoc -> assoc.getCompanyId().equals(newAssoc.getCompanyId()) && assoc.getContactId().equals(newAssoc.getContactId()) && assoc.getRole().equals(newAssoc.getRole()))) {
                invalidAssociations.add(new InvalidAssociation(newAssoc.getCompanyId(), newAssoc.getContactId(), newAssoc.getRole(), "ALREADY_EXISTS"));
                continue;
            }

            int companyRoleUsage = companyRoleCount.getOrDefault(newAssoc.getCompanyId(), Collections.emptyMap())
                    .getOrDefault(newAssoc.getRole(), 0);
            int contactRoleUsage = contactRoleCount.getOrDefault(newAssoc.getContactId(), Collections.emptyMap())
                    .getOrDefault(newAssoc.getCompanyId(), 0);

            if ((companyRoleUsage + newAssociations.stream().filter(a -> a.getCompanyId().equals(newAssoc.getCompanyId()) && a.getRole().equals(newAssoc.getRole())).count() > MAX_CONTACTS_PER_ROLE) || (contactRoleCount.getOrDefault(newAssoc.getContactId(), Collections.emptyMap()).values().stream().mapToInt(Integer::intValue).sum() + newAssociations.stream().filter(a -> a.getContactId().equals(newAssoc.getContactId()) && a.getCompanyId().equals(newAssoc.getCompanyId())).count() > MAX_ROLES_PER_CONTACT)) {
                invalidAssociations.add(new InvalidAssociation(newAssoc.getCompanyId(), newAssoc.getContactId(), newAssoc.getRole(), "WOULD_EXCEED_LIMIT"));
            } else {
                validAssociations.add(newAssoc);
                companyRoleCount.computeIfAbsent(newAssoc.getCompanyId(), k -> new HashMap<>())
                        .merge(newAssoc.getRole(), 1, Integer::sum);
                contactRoleCount.computeIfAbsent(newAssoc.getContactId(), k -> new HashMap<>())
                        .merge(newAssoc.getCompanyId(), 1, Integer::sum);
            }
        }

        Map<String, List<Association>> result = Map.of(
                "validAssociations", validAssociations,
                "invalidAssociations", new ArrayList<>(invalidAssociations)
        );

        hubspotAssociationDao.postValidationResults(result);

        return result;
    }
}
