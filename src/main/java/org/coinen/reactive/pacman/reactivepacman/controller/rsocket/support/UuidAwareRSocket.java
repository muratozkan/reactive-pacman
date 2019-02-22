package org.coinen.reactive.pacman.reactivepacman.controller.rsocket.support;

import java.util.UUID;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.util.RSocketProxy;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

public class UuidAwareRSocket extends RSocketProxy {

    private final UUID uuid;

    public UuidAwareRSocket(RSocket source, UUID uuid) {
        super(source);
        this.uuid = uuid;
    }

    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        return super.fireAndForget(payload).subscriberContext(Context.of("uuid", uuid));
    }

    @Override
    public Mono<Payload> requestResponse(Payload payload) {
        return super.requestResponse(payload).subscriberContext(Context.of("uuid", uuid));
    }

    @Override
    public Flux<Payload> requestStream(Payload payload) {
        return super.requestStream(payload).subscriberContext(Context.of("uuid", uuid));
    }

    @Override
    public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
        return super.requestChannel(payloads).subscriberContext(Context.of("uuid", uuid));
    }
}