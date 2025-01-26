package com.learn.mapstruct.mapper;

import com.learn.mapstruct.model.SimpleDestination;
import com.learn.mapstruct.model.SimpleSource;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface SimpleSourceDestinationMapper {
  SimpleDestination sourceToDestination(SimpleSource source);
  SimpleSource destinationToSource(SimpleDestination destination);
  List<SimpleDestination> sourceListToDestinationList(List<SimpleSource> sources);
  List<SimpleSource> destinationListToSourceList(List<SimpleDestination> destinations);
}
