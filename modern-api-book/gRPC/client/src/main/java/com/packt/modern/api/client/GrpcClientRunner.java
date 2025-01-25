package com.packt.modern.api.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
public class GrpcClientRunner implements CommandLineRunner {
  private static Logger LOG = LoggerFactory.getLogger(GrpcClientRunner.class);
  private final GrpcClient client;

  public GrpcClientRunner(GrpcClient client) {
    this.client = client;
  }

  @Override
  public void run(String... args) throws Exception {
    client.start();
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  try {
                    client.shutdown();
                  } catch (InterruptedException e) {
                    LOG.error("Client stop with error: {}", e.getMessage());
                  }
                }));
  }
}
