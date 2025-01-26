package com.learn.mapstruct.mapper;

import com.learn.mapstruct.model.SimpleDestination;
import com.learn.mapstruct.model.SimpleSource;
import org.mapstruct.Mapper;

@Mapper
public interface SimpleSourceDestinationMapper {
  SimpleDestination sourceToDestination(SimpleSource source);
  SimpleSource destinationToSource(SimpleDestination destination);
}
