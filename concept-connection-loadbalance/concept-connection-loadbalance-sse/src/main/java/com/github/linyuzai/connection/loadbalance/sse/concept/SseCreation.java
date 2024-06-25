package com.github.linyuzai.connection.loadbalance.sse.concept;

/**
 * 提供 SSE 的 id 和 path。
 * <p>
 * Provide id and path for SSE.
 */
public interface SseCreation {

    Object getId();

    String getPath();
}
