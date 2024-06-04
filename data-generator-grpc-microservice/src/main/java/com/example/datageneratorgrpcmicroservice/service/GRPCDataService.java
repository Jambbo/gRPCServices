package com.example.datageneratorgrpcmicroservice.service;

import com.example.datageneratorgrpcmicroservice.model.Data;

import java.util.List;

public interface GRPCDataService {
    void sendData(Data data);
    void sendData(List<Data> data);
}
