package com.example.datageneratorgrpcmicroservice.model.test;

import com.example.datageneratorgrpcmicroservice.model.Data.MeasurementType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class DataTestOptions {

    private Integer delayInSeconds;
    private MeasurementType[] measurementTypes;

}
