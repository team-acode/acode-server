package server.acode.domain.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import server.acode.domain.user.dto.response.DisplayScrap;
import server.acode.domain.user.dto.response.PreviewScrap;

import java.util.List;

@Repository
public interface ScrapRepositoryCustom {

    List<PreviewScrap> getScrapPreview(Long userId);

    Page<DisplayScrap> getScrap(Long userId, Pageable pageable);
}
