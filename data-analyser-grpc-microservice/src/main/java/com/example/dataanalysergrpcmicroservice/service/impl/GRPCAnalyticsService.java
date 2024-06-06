package com.example.dataanalysergrpcmicroservice.service.impl;


import com.example.dataanalysergrpcmicroservice.model.Data;
import com.example.dataanalysergrpcmicroservice.service.DataService;
import com.example.grpccommon.AnalyticsServerGrpc.AnalyticsServerImplBase;
import com.example.grpccommon.GRPCAnalyticsRequest;
import com.example.grpccommon.GRPCData;
import com.example.grpccommon.MeasurementType;
import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.ZoneOffset;
import java.util.List;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class GRPCAnalyticsService extends AnalyticsServerImplBase {

    private final DataService dataService;

    @Override
    public void askForData(GRPCAnalyticsRequest request, StreamObserver<GRPCData> responseObserver) {
        List<Data> data = dataService.getWithBatch(request.getBatchSize());
        data.forEach(d -> {
            GRPCData dataRequest = GRPCData.newBuilder()
                    .setId(d.getId())
                    .setSensorId(d.getSensorId())
                    .setTimestamp(
                            Timestamp.newBuilder()
                                    .setSeconds(d.getTimestamp()
                                            .toEpochSecond(ZoneOffset.UTC))
                                    .build()
                    )
                    .setMeasurement(d.getMeasuerement())
                    .setMeasurementType(MeasurementType.valueOf(d.getMeasurementType().name()))
                    .build();
            responseObserver.onNext(dataRequest);
        });
        log.info("Batch was sent.");
        responseObserver.onCompleted();
    }
}
