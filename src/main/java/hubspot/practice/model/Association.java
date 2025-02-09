package hubspot.practice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Association {
    private String companyId;
    private String contactId;
    private String role;
}
