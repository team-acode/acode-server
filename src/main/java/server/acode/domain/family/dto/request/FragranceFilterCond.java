package server.acode.domain.family.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class FragranceFilterCond {
    private String brand;
    private String family;
    private String additionalFamily;
    private String ingredient;
}
