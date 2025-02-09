package hubspot.practice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvalidAssociation extends Association {
    private String failureReason;

    public InvalidAssociation(String companyId, String contactId, String role, String failureReason) {
        super(companyId, contactId, role);
        this.failureReason = failureReason;
    }
}
