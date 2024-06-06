package com.example.datastoregrpcmicroservice.web.mapper;

import com.example.datastoregrpcmicroservice.model.Summary;
import com.example.datastoregrpcmicroservice.web.dto.SummaryDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SummaryMapper extends Mappable<Summary, SummaryDto> {
}
