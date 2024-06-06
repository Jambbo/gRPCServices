package com.example.datastoregrpcmicroservice.service;

import com.example.datastoregrpcmicroservice.model.Data;
import com.example.datastoregrpcmicroservice.model.Data.MeasurementType;
import com.example.datastoregrpcmicroservice.model.Summary.SummaryType;
import com.example.datastoregrpcmicroservice.model.Summary;

import java.util.Set;

public interface SummaryService {

    Summary get(Long sensorId, Set<MeasurementType> mt, Set<SummaryType> st);
    void handle(Data data);
}
