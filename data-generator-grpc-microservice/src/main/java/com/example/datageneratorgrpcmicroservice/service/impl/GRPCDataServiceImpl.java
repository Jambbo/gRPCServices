package com.example.datageneratorgrpcmicroservice.service.impl;

import com.example.datageneratorgrpcmicroservice.model.Data;
import com.example.datageneratorgrpcmicroservice.service.GRPCDataService;
import com.example.datageneratorgrpcmicroservice.web.mapper.GRPCDataMapper;
import com.example.grpccommon.DataServerGrpc.DataServerBlockingStub;
import com.example.grpccommon.DataServerGrpc.DataServerStub;
import com.example.grpccommon.GRPCData;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GRPCDataServiceImpl implements GRPCDataService {

    private final GRPCDataMapper grpcDataMapper;

    @GrpcClient(value = "data-generator-blocking") //here is given the name for client
    private DataServerBlockingStub blockingStub;

    @GrpcClient(value = "data-generator-async")
    private DataServerStub asyncStub;

    @Override
    public void sendData(Data data) {
        GRPCData request = grpcDataMapper.toGRPCData(data);
        log.debug("Sending data: {}",data);
        asyncStub.addData(request, getEmptyStreamObserver());
    }

    @Override
    public void sendData(List<Data> data) {
        StreamObserver<GRPCData> requestObserver = asyncStub.addStreamOfData(getEmptyStreamObserver());
        data.forEach(d -> {
                    GRPCData request = grpcDataMapper.toGRPCData(d);
                    log.debug("Sending data: {}",data);
                    requestObserver.onNext(request);
                }
        );
        //telling to server that I am finished with request and I will not send anything else
        requestObserver.onCompleted();
    }

    //method describes how I want to process the server responses. I need it to use in asyncStub
    private StreamObserver<Empty> getEmptyStreamObserver() {
        return new StreamObserver<>() {
            @Override
            public void onNext(Empty empty) {
                log.info("Received response from server.");
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("Error occurred: {}", throwable.getMessage(), throwable);
            }

            @Override
            public void onCompleted() {
                log.info("Stream completed.");
            }
        };
    }
}
