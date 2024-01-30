package server.acode.domain.fragrance.dto.response;

import lombok.*;
import server.acode.domain.fragrance.entity.Fragrance;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GetFragrancePurchase {
    private boolean isSoldOut;
    private String fragranceName;
    private String brandName;
    private List<PurchaseInfo> purchaseList;
}
