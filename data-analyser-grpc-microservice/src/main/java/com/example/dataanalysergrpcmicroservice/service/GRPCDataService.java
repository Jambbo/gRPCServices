package com.example.dataanalysergrpcmicroservice.service;

import com.example.dataanalysergrpcmicroservice.model.Data;
import com.example.grpccommon.DataServerGrpc.DataServerImplBase;
import com.example.grpccommon.GRPCData;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
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
        return super.addStreamOfData(responseObserver);
    }
}
