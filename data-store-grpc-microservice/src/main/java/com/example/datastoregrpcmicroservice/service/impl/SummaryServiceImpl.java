package com.example.datastoregrpcmicroservice.service.impl;

import com.example.datastoregrpcmicroservice.model.Data;
import com.example.datastoregrpcmicroservice.model.Data.MeasurementType;
import com.example.datastoregrpcmicroservice.model.Summary;
import com.example.datastoregrpcmicroservice.model.Summary.SummaryType;
import com.example.datastoregrpcmicroservice.model.exception.SensorNotFoundException;
import com.example.datastoregrpcmicroservice.repository.SummaryRepository;
import com.example.datastoregrpcmicroservice.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class SummaryServiceImpl implements SummaryService {

    private final SummaryRepository summaryRepository;

    @Override
    public Summary get(Long sensorId, Set<MeasurementType> mt, Set<SummaryType> st) {
        return summaryRepository.findBySensorId(
                        sensorId,
                        mt == null ? Set.of(MeasurementType.values()) : mt,
                        st == null ? Set.of(SummaryType.values()) : st
                )
                .orElseThrow(SensorNotFoundException::new);
    }

    @Override
    public void handle(Data data) {
        summaryRepository.handle(data);
    }
}
