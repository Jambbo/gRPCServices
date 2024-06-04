package com.example.datageneratorgrpcmicroservice.web.dto;

import com.example.datageneratorgrpcmicroservice.model.Data.MeasurementType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DataTestOptionsDto {

    private Integer delayInSeconds;
    private MeasurementType[] measurementTypes;

}
