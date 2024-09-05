package dev.hospitalinformation.service;

import dev.hospitalinformation.repository.CommuneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommuneService {
    private final CommuneRepository communeRepository;
}