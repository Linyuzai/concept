package com.github.linyuzai.chain.blocking;

import com.github.linyuzai.chain.core.Chain;
import com.github.linyuzai.chain.core.Context;
import com.github.linyuzai.chain.core.Invoker;

public interface BlockingInvoker<CTX extends Context, CH extends Chain<Void, CTX>> extends Invoker<Void, CTX, CH> {

}
