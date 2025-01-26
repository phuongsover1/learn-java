package com.learn.mapstruct;

import com.learn.mapstruct.mapper.SimpleSourceDestinationMapper;
import com.learn.mapstruct.mapper.SimpleSourceDestinationMapperImpl;
import com.learn.mapstruct.model.SimpleDestination;
import com.learn.mapstruct.model.SimpleSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

}
