package com.example.datastoregrpcmicroservice.repository;

import com.example.datastoregrpcmicroservice.model.Data;
import com.example.datastoregrpcmicroservice.model.Data.MeasurementType;
import com.example.datastoregrpcmicroservice.model.Summary;
import com.example.datastoregrpcmicroservice.model.Summary.SummaryType;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

public interface SummaryRepository {

    Optional<Summary> findBySensorId(Long sensorId, Set<MeasurementType> mt, Set<SummaryType> st);
    void handle(Data data);
}
