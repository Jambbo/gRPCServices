package com.example.datageneratorgrpcmicroservice.web.mapper;

import com.example.datageneratorgrpcmicroservice.model.Data;
import com.example.datageneratorgrpcmicroservice.web.dto.DataDto;
import org.mapstruct.Mapper;

// by using @Mapper spring will automatically create bean of this mapper to use this mapper for own purposes
@Mapper(componentModel = "spring")
public interface DataMapper extends Mappable<Data, DataDto>{
}
