package com.github.linyuzai.chain.reactor;

import com.github.linyuzai.chain.core.Chain;
import com.github.linyuzai.chain.core.Context;
import com.github.linyuzai.chain.core.Invoker;
import reactor.core.publisher.Mono;

public interface ReactorInvoker<CTX extends Context, CH extends Chain<Mono<Void>, CTX>> extends Invoker<Mono<Void>, CTX, CH> {

}
