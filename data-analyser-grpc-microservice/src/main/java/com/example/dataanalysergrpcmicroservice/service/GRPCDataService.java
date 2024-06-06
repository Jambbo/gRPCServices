package com.example.dataanalysergrpcmicroservice.service;

import com.example.dataanalysergrpcmicroservice.model.Data;
import com.example.grpccommon.DataServerGrpc.DataServerImplBase;
import com.example.grpccommon.GRPCData;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Value;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class GRPCDataService extends DataServerImplBase {

    private final DataService dataService;

    @Override
    public void addData(GRPCData request, StreamObserver<Empty> responseObserver) {
        Data data = new Data(request);
        dataService.handle(data);
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<GRPCData> addStreamOfData(StreamObserver<Empty> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(GRPCData grpcData) {
                Data data = new Data(grpcData);
                dataService.handle(data);
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("Error caused: {} ",throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(Empty.newBuilder().build());
               responseObserver.onCompleted();
            }
        };
    }
}
