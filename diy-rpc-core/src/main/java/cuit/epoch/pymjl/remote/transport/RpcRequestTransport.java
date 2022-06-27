package cuit.epoch.pymjl.remote.transport;

import cuit.epoch.pymjl.annotations.MySpi;
import cuit.epoch.pymjl.remote.entity.RpcRequest;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/4 12:02
 **/
@MySpi
public interface RpcRequestTransport {
    /**
     * 发送rpc请求
     *
     * @param rpcRequest rpc请求
     * @return {@code Object} 服务端返回的信息
     */
    Object sendRpcRequest(RpcRequest rpcRequest);
}
