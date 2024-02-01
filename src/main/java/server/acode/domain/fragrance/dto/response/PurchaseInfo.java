package server.acode.domain.fragrance.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseInfo {
    private String title;
    private String link;
    private String image;
}
