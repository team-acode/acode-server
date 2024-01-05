package server.acode.domain.family.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class HomeFragranceResponse {
    private int fragranceId;
    private String fragranceName;
    private String brandName;
    private String poster;
}
