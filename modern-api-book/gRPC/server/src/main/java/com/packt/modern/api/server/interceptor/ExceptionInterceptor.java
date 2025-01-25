package com.packt.modern.api.server.interceptor;

import com.packt.modern.api.server.exception.ExceptionUtils;
import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ExceptionInterceptor implements ServerInterceptor {
  private final Logger LOG = LoggerFactory.getLogger(getClass());

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
    ServerCall.Listener<ReqT> listener = serverCallHandler.startCall(serverCall, metadata);
    return new ExceptionHandlingServerCallListener<>(listener, serverCall, metadata);
  }

  private class ExceptionHandlingServerCallListener<ReqT, RespT>
  extends ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT> {
    private final ServerCall<ReqT, RespT> serverCall;
    private final Metadata metadata;

    private ExceptionHandlingServerCallListener(ServerCall.Listener<ReqT> listener, ServerCall<ReqT, RespT> serverCall, Metadata metadata) {
      super(listener);
      this.serverCall = serverCall;
      this.metadata = metadata;
    }

    @Override
    public void onHalfClose() {
      try {
        super.onHalfClose();
      } catch (RuntimeException e) {
        handleException(e, serverCall, metadata);
        throw e;
      }
    }

    @Override
    public void onReady() {
      try {
        super.onReady();
      } catch (RuntimeException e) {
        handleException(e, serverCall, metadata) ;
        throw e;
      }
    }

    private void handleException(RuntimeException e, ServerCall<ReqT, RespT> serverCall, Metadata metadata) {
      StatusRuntimeException status = ExceptionUtils.traceException(e);
      serverCall.close(status.getStatus(), metadata);
    }
  }
}
