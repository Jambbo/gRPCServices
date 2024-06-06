package com.example.dataanalysergrpcmicroservice.service.impl;

import com.example.dataanalysergrpcmicroservice.model.Data;
import com.example.dataanalysergrpcmicroservice.repository.DataRepository;
import com.example.dataanalysergrpcmicroservice.service.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {

    private final DataRepository dataRepository;

    @Override
    public void handle(Data data) {
        log.info("Data object {} was saved",data);
        dataRepository.save(data);
    }

    @Override
    @Transactional
    public List<Data> getWithBatch(Long batchSize) {
        List<Data> data = dataRepository.findAllWithOffset(batchSize);
        if(data.size()>0){
            dataRepository.incrementOffset(Long.min(batchSize,data.size()));
        }
        return data;
    }
}
