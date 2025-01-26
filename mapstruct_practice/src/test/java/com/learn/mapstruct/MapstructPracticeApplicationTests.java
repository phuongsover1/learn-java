package com.learn.mapstruct;

import com.learn.mapstruct.mapper.SimpleSourceDestinationMapper;
import com.learn.mapstruct.mapper.SimpleSourceDestinationMapperImpl;
import com.learn.mapstruct.model.SimpleDestination;
import com.learn.mapstruct.model.SimpleSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapstructPracticeApplicationTests {
  SimpleSourceDestinationMapper simpleSourceDestinationMapper = new SimpleSourceDestinationMapperImpl();

  @Test
  void givenSourceToDestination_whenMapStruct_thenSuccess() {
    SimpleSource simpleSource = new SimpleSource();
    simpleSource.setName("SourceName");
    simpleSource.setDescription("SourceDescription");

    SimpleDestination simpleDestination = simpleSourceDestinationMapper.sourceToDestination(simpleSource);

    assertEquals(simpleSource.getName(), simpleDestination.getName());
    assertEquals(simpleSource.getDescription(), simpleDestination.getDescription());
  }

  @Test
  void givenDestinationToSource_whenMapStruct_thenSuccess() {
      SimpleDestination simpleDestination = new SimpleDestination();
      simpleDestination.setName("DestinationName");
      simpleDestination.setDescription("DestinationDescription");

      SimpleSource simpleSource = simpleSourceDestinationMapper.destinationToSource(simpleDestination);
      assertEquals(simpleDestination.getName(), simpleSource.getName());
      assertEquals(simpleDestination.getDescription(), simpleSource.getDescription());
  }

  @Test
  void givenListSourceToDestination_whenMapStruct_thenSuccess() {
    SimpleSource simpleSource = new SimpleSource();
    simpleSource.setName("SourceName");
    simpleSource.setDescription("SourceDescription");

    List<SimpleSource> simpleSources = List.of(simpleSource);
    List<SimpleDestination> simpleDestinations = simpleSourceDestinationMapper.sourceListToDestinationList(simpleSources);

    assertEquals(1, simpleDestinations.size());
    assertEquals(simpleSource.getName(), simpleDestinations.get(0).getName());
    assertEquals(simpleSource.getDescription(), simpleDestinations.get(0).getDescription());
  }

  @Test
  void givenListDestinationToSource_whenMapStruct_thenSuccess() {
    SimpleDestination simpleDestination = new SimpleDestination();
    simpleDestination.setName("DestinationName");
    simpleDestination.setDescription("DestinationDescription");

    List<SimpleDestination> simpleDestinations = List.of(simpleDestination);
    List<SimpleSource> simpleSources = simpleSourceDestinationMapper.destinationListToSourceList(simpleDestinations);
    assertEquals(1, simpleSources.size());
    assertEquals(simpleDestination.getName(), simpleSources.get(0).getName());
    assertEquals(simpleDestination.getDescription(), simpleSources.get(0).getDescription());
  }

}
