package com.example.datastoregrpcmicroservice.service.impl;

import com.example.datastoregrpcmicroservice.model.Data;
import com.example.datastoregrpcmicroservice.service.GRPCDataService;
import com.example.datastoregrpcmicroservice.service.SummaryService;
import com.example.grpccommon.AnalyticsServerGrpc.AnalyticsServerStub;
import com.example.grpccommon.GRPCAnalyticsRequest;
import com.example.grpccommon.GRPCData;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class GRPCDataServiceImpl implements GRPCDataService {

    private final ScheduledExecutorService executorService =
            Executors.newSingleThreadScheduledExecutor();
    private final SummaryService summaryService;

    @GrpcClient("data-store-async")
    private AnalyticsServerStub asyncStub;

    @Value("${fetch.batch-size}")
    private Long batchSize;

    @PostConstruct
    public void init(){
        fetchMessages();
    }

    @Override
    public void fetchMessages() {
        executorService.scheduleAtFixedRate(
                () -> asyncStub.askForData(
                        GRPCAnalyticsRequest.newBuilder()
                                .setBatchSize(batchSize)
                                .build(),
                        new StreamObserver<>() {
                            @Override
                            public void onNext(GRPCData grpcData) {
                                summaryService.handle(new Data(grpcData));
                            }

                            @Override
                            public void onError(Throwable throwable) {
                            }

                            @Override
                            public void onCompleted() {
                                log.info("Batch was handled.");
                            }
                        }
                ),
                0,
                10,
                TimeUnit.SECONDS
        );
    }
}
