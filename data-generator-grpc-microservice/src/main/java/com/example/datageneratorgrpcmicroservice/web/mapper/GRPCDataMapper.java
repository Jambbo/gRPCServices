package com.example.datageneratorgrpcmicroservice.web.mapper;

import com.example.datageneratorgrpcmicroservice.model.Data;
import com.example.grpccommon.GRPCData;
import com.example.grpccommon.MeasurementType;
import com.google.protobuf.Timestamp;
import org.mapstruct.Mapper;

import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface GRPCDataMapper {

    default GRPCData toGRPCData(Data data){
       return  GRPCData.newBuilder()
                .setSensorId(data.getSensorId())
                .setTimestamp(
                        Timestamp.newBuilder()
                                .setSeconds(data.getTimestamp().toEpochSecond(ZoneOffset.UTC))
                                .build()
                )
                .setMeasurement(data.getMeasuerement())
                .setMeasurementType(MeasurementType.valueOf(data.getMeasurementType().name()))
                .build();
    }

}
