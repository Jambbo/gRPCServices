package com.example.datageneratorgrpcmicroservice.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.datageneratorgrpcmicroservice.model.Data.MeasurementType;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class DataDto {

    private Long sensorId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") //indicating the data transfer format
    private LocalDateTime timestamp;

    private Double measuerement;
    private MeasurementType measurementType;

}
