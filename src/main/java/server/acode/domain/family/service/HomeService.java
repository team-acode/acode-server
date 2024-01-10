package server.acode.domain.family.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.acode.domain.family.repository.FamilyRepository;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final FamilyRepository familyRepository;
}
