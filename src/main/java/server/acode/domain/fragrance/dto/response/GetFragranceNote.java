package server.acode.domain.fragrance.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.acode.domain.fragrance.entity.Fragrance;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetFragranceNote {
    private Long fragranceId;
    private boolean isSingle;
    private List<NoteInfo> topNote;
    private List<NoteInfo> middleNote;
    private List<NoteInfo> baseNote;

    public GetFragranceNote(Fragrance fragrance, List<NoteInfo> topNote, List<NoteInfo> middleNote, List<NoteInfo> baseNote) {
        this.fragranceId = fragrance.getId();
        this.isSingle = fragrance.isSingle();
        this.topNote = topNote;
        this.middleNote = middleNote;
        this.baseNote = baseNote;
    }
}
