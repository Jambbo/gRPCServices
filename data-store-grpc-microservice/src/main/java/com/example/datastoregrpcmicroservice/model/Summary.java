package com.example.datastoregrpcmicroservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.example.datastoregrpcmicroservice.model.Data.MeasurementType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Summary will be returned to user as an answer for his request about consuming statistics
@Getter
@Setter
@ToString
public class Summary {

    private Long sensorId;
    private Map<MeasurementType, List<SummaryEntry>> values;

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class SummaryEntry{
        private SummaryType summaryType;
        private Double value;
        private Long counter; //this field will show how much data came via current SummaryType
    }
    public enum SummaryType {

        MIN,
        MAX,
        AVG,
        SUM

    }
    public Summary(){
        this.values = new HashMap<>(); // to avoid NullPointerException
    }


    public void addValue(MeasurementType type, SummaryEntry value){
        if(values.containsKey(type)){
            List<SummaryEntry> entries = new ArrayList<>(values.get(type));
            entries.add(value);
            values.put(type,entries);
        }else{
            values.put(type,List.of(value));
        }
    }

}
