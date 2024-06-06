package com.example.datageneratorgrpcmicroservice.service.impl;

import com.example.datageneratorgrpcmicroservice.model.Data;
import com.example.datageneratorgrpcmicroservice.model.Data.MeasurementType;
import com.example.datageneratorgrpcmicroservice.model.test.DataTestOptions;
import com.example.datageneratorgrpcmicroservice.service.GRPCDataService;
import com.example.datageneratorgrpcmicroservice.service.TestDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TestDataServiceImpl implements TestDataService {

    private final ScheduledExecutorService executorService
            = Executors.newSingleThreadScheduledExecutor();

    @Value("${push.batch-size}")
    private int batchSize;

    private final GRPCDataService grpcDataService;

    @Override
    public void sendMessages(DataTestOptions dataTestOptions) {
        List<Data> dataBatch = new ArrayList<>();
        if(dataTestOptions.getMeasurementTypes().length>0){
            executorService.scheduleAtFixedRate(()->{
                Data data = new Data();
                data.setSensorId((long)getRandomNumber(1,10));
                data.setMeasurement(getRandomNumber(15,20));
                data.setMeasurementType(getRandomMeasurementType(dataTestOptions.getMeasurementTypes()));
                data.setTimestamp(LocalDateTime.now());
                dataBatch.add(data);
                if(dataBatch.size()==batchSize){
                    grpcDataService.sendData(dataBatch);
                    dataBatch.clear();
                }
            },
               0,
               dataTestOptions.getDelayInSeconds(),
               TimeUnit.SECONDS
            );
        }

    }

    private double getRandomNumber(double min, double max) {
        return (Math.random()*(max-min))+min;
    }
    private MeasurementType getRandomMeasurementType(MeasurementType[] measurementTypes) {
        int randomTypeId = (int)(Math.random()*measurementTypes.length);
        return measurementTypes[randomTypeId];
    }
}
