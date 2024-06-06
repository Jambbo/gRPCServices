package com.example.datastoregrpcmicroservice.web.dto;

import com.example.datastoregrpcmicroservice.model.Data.MeasurementType;
import com.example.datastoregrpcmicroservice.model.Summary.SummaryEntry;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Summary will be returned to user as an answer for his request about consuming statistics
@Getter
@Setter
@ToString
@NoArgsConstructor
public class SummaryDto {

    private Long sensorId;
    private Map<MeasurementType, List<SummaryEntry>> values;

}
