package dev.oz.vertx.eb;

import io.vertx.core.buffer.Buffer;

public class  MessageCodec<S,R extends S> implements io.vertx.core.eventbus.MessageCodec<S, S> {
  @Override
  public void encodeToWire(Buffer buffer, S s) {
    System.out.println("encoding");
  }

  @Override
  public S decodeFromWire(int pos, Buffer buffer) {
    System.out.println("decoding");
    return null;
  }

  @Override
  public S transform(S s) {
    System.out.println("transform");
    return s;
  }

  @Override
  public String name() {
    System.out.println("message codec");
    return "message codec";
  }

  @Override
  public byte systemCodecID() {
    System.out.println("codec id");
    return -1;
  }
}
