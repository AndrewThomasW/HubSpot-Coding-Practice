package hubspot.practice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssociationWrapper {
    private List<Association> existingAssociations;
    private List<Association> newAssociations;
}
