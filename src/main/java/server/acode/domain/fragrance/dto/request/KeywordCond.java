package server.acode.domain.fragrance.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class KeywordCond {
    private List<String> concentration;
    private List<String> season;
    private List<String> mainFamily;
    private List<String> scent;
    private List<String> style;
}