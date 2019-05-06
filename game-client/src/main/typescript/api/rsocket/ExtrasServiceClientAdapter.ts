import { ReactiveSocket } from "rsocket-types";
import { RSocketRPCServices } from "game-idl";
import ExtrasService from "../ExtrasService";
import { Extra } from "game-idl";
import { Flux } from "reactor-core-js/flux";
import { Empty } from "google-protobuf/google/protobuf/empty_pb";
import FlowableAdapter from "../FlowableAdapter";
import {IMeterRegistry} from "rsocket-rpc-metrics";

export default class ExtrasServiceClientAdapter implements ExtrasService {

    private service: RSocketRPCServices.ExtrasService;

    constructor(rSocket: ReactiveSocket<any, any>, meterRegistry?: IMeterRegistry) {
        this.service = new RSocketRPCServices.ExtrasServiceClient(rSocket, undefined, meterRegistry);
    }

    extras(): Flux<Extra.AsObject> {
        const uuid = localStorage.getItem("uuid");
        const metadata = uuid ? Buffer.alloc(Buffer.byteLength(uuid), uuid, "utf8") : undefined;
        return Flux.from<Extra>(FlowableAdapter.wrap(this.service.extras(new Empty(), metadata) as any))
            .map(extra => extra.toObject());
    }
}