package server.acode.domain.fragrance.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.acode.domain.fragrance.entity.Capacity;

@Getter
@NoArgsConstructor
public class CapacityInfo {
    private String capacity;
    private int price;

    @Builder
    public CapacityInfo(Capacity capacity) {
        this.capacity = capacity.getCapacity();
        this.price = capacity.getPrice();
    }
}
