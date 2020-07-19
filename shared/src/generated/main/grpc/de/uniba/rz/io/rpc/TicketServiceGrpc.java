package de.uniba.rz.io.rpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.20.0)",
    comments = "Source: ticketManagement.proto")
public final class TicketServiceGrpc {

  private TicketServiceGrpc() {}

  public static final String SERVICE_NAME = "TicketService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<de.uniba.rz.io.rpc.TicketRequest,
      de.uniba.rz.io.rpc.TicketResponse> getServerrequestMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "serverrequest",
      requestType = de.uniba.rz.io.rpc.TicketRequest.class,
      responseType = de.uniba.rz.io.rpc.TicketResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.uniba.rz.io.rpc.TicketRequest,
      de.uniba.rz.io.rpc.TicketResponse> getServerrequestMethod() {
    io.grpc.MethodDescriptor<de.uniba.rz.io.rpc.TicketRequest, de.uniba.rz.io.rpc.TicketResponse> getServerrequestMethod;
    if ((getServerrequestMethod = TicketServiceGrpc.getServerrequestMethod) == null) {
      synchronized (TicketServiceGrpc.class) {
        if ((getServerrequestMethod = TicketServiceGrpc.getServerrequestMethod) == null) {
          TicketServiceGrpc.getServerrequestMethod = getServerrequestMethod = 
              io.grpc.MethodDescriptor.<de.uniba.rz.io.rpc.TicketRequest, de.uniba.rz.io.rpc.TicketResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "TicketService", "serverrequest"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.uniba.rz.io.rpc.TicketRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.uniba.rz.io.rpc.TicketResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new TicketServiceMethodDescriptorSupplier("serverrequest"))
                  .build();
          }
        }
     }
     return getServerrequestMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TicketServiceStub newStub(io.grpc.Channel channel) {
    return new TicketServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TicketServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new TicketServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TicketServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new TicketServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class TicketServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void serverrequest(de.uniba.rz.io.rpc.TicketRequest request,
        io.grpc.stub.StreamObserver<de.uniba.rz.io.rpc.TicketResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getServerrequestMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getServerrequestMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                de.uniba.rz.io.rpc.TicketRequest,
                de.uniba.rz.io.rpc.TicketResponse>(
                  this, METHODID_SERVERREQUEST)))
          .build();
    }
  }

  /**
   */
  public static final class TicketServiceStub extends io.grpc.stub.AbstractStub<TicketServiceStub> {
    private TicketServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TicketServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TicketServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TicketServiceStub(channel, callOptions);
    }

    /**
     */
    public void serverrequest(de.uniba.rz.io.rpc.TicketRequest request,
        io.grpc.stub.StreamObserver<de.uniba.rz.io.rpc.TicketResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getServerrequestMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class TicketServiceBlockingStub extends io.grpc.stub.AbstractStub<TicketServiceBlockingStub> {
    private TicketServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TicketServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TicketServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TicketServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public de.uniba.rz.io.rpc.TicketResponse serverrequest(de.uniba.rz.io.rpc.TicketRequest request) {
      return blockingUnaryCall(
          getChannel(), getServerrequestMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class TicketServiceFutureStub extends io.grpc.stub.AbstractStub<TicketServiceFutureStub> {
    private TicketServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TicketServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TicketServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TicketServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<de.uniba.rz.io.rpc.TicketResponse> serverrequest(
        de.uniba.rz.io.rpc.TicketRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getServerrequestMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SERVERREQUEST = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final TicketServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(TicketServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SERVERREQUEST:
          serviceImpl.serverrequest((de.uniba.rz.io.rpc.TicketRequest) request,
              (io.grpc.stub.StreamObserver<de.uniba.rz.io.rpc.TicketResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class TicketServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TicketServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return de.uniba.rz.io.rpc.TicketManagement.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TicketService");
    }
  }

  private static final class TicketServiceFileDescriptorSupplier
      extends TicketServiceBaseDescriptorSupplier {
    TicketServiceFileDescriptorSupplier() {}
  }

  private static final class TicketServiceMethodDescriptorSupplier
      extends TicketServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    TicketServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (TicketServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TicketServiceFileDescriptorSupplier())
              .addMethod(getServerrequestMethod())
              .build();
        }
      }
    }
    return result;
  }
}
