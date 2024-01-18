package server.acode.domain.fragrance.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class ExtractResponse {
    private List<ExtractFamily> families;
    private List<ExtractFragrance> fragrances;

    public ExtractResponse(List<ExtractFamily> families, List<ExtractFragrance> fragrances){
        this.families = families;
        this.fragrances =fragrances;
    }
}
