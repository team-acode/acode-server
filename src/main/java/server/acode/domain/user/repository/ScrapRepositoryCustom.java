package server.acode.domain.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import server.acode.domain.user.dto.response.ScrapDto;
import server.acode.domain.user.dto.response.ScrapPreviewDto;

import java.util.List;

@Repository
public interface ScrapRepositoryCustom {

    List<ScrapPreviewDto> getScrapPreview(Long userId);

    Page<ScrapDto> getScrap(Long userId, Pageable pageable);
}
