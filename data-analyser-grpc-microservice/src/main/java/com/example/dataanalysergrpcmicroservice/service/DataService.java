package com.example.dataanalysergrpcmicroservice.service;

import com.example.dataanalysergrpcmicroservice.model.Data;

import java.util.List;

public interface DataService {

    void handle(Data data);

    List<Data> getWithBatch(Long batchSize);

}
