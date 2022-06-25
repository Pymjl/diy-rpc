package cuit.epoch.pymjl.remote.transport.netty.client;

import cuit.epoch.pymjl.remote.entity.RpcResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用来存放服务端未处理的客户端请求
 *
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/26 0:03
 **/
public class UnprocessedRequests {
    private static final Map<String, CompletableFuture<RpcResponse<Object>>> UNPROCESSED_RESPONSE_FUTURES = new ConcurrentHashMap<>();

    /**
     * 将异步添加到请求map中去，等待服务器处理，响应
     *
     * @param requestId 请求id
     * @param future    异步线程
     */
    public void put(String requestId, CompletableFuture<RpcResponse<Object>> future) {
        UNPROCESSED_RESPONSE_FUTURES.put(requestId, future);
    }

    /**
     * 通过请求ID移除请求队列
     *
     * @param rpcResponse rpc反应
     */
    public void complete(RpcResponse<Object> rpcResponse) {
        CompletableFuture<RpcResponse<Object>> future = UNPROCESSED_RESPONSE_FUTURES.remove(rpcResponse.getRequestId());
        if (null != future) {
            future.complete(rpcResponse);
        } else {
            throw new IllegalStateException();
        }
    }
}
