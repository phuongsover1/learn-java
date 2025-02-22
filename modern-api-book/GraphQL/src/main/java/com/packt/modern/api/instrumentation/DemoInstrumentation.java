package com.packt.modern.api.instrumentation;

import com.netflix.graphql.dgs.DgsExecutionResult;
import graphql.ExecutionResult;
import graphql.execution.instrumentation.InstrumentationState;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

// Support metrics, tracing and logging
@Component
public class DemoInstrumentation extends SimpleInstrumentation {
  @Override
  public @NotNull CompletableFuture<ExecutionResult> instrumentExecutionResult(
      ExecutionResult executionResult,
      InstrumentationExecutionParameters parameters,
      InstrumentationState state) {
    HttpHeaders responseHeaders = new HttpHeaders();

    // Added custom headers in the response
    responseHeaders.add("myHeader", "hello");
    return super.instrumentExecutionResult(
        DgsExecutionResult.builder()
            .executionResult(executionResult)
            .headers(responseHeaders)
            .build(),
        parameters,
        state);
  }
}
