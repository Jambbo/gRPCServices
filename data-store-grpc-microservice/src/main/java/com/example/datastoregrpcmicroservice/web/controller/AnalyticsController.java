package com.example.datastoregrpcmicroservice.web.controller;

import com.example.datastoregrpcmicroservice.model.Data.MeasurementType;
import com.example.datastoregrpcmicroservice.model.Summary;
import com.example.datastoregrpcmicroservice.model.Summary.SummaryType;
import com.example.datastoregrpcmicroservice.service.SummaryService;
import com.example.datastoregrpcmicroservice.web.dto.SummaryDto;
import com.example.datastoregrpcmicroservice.web.mapper.SummaryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final SummaryService summaryService;
    private final SummaryMapper summaryMapper;

    @GetMapping("/summary/{sensorId}")
    public SummaryDto getSummary(
            @PathVariable Long sensorId,
            @RequestParam(value = "mt", required = false)
            Set<MeasurementType> measurementTypes,
            @RequestParam(value="st", required = false)
            Set<SummaryType> summaryTypes
    ){
        Summary summary = summaryService.get(
                sensorId, measurementTypes, summaryTypes
        );
        return summaryMapper.toDto(summary);
    }
}
