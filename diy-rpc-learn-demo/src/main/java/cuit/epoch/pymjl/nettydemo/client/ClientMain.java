package cuit.epoch.pymjl.nettydemo.client;

import cuit.epoch.pymjl.nettydemo.entity.RpcRequest;
import cuit.epoch.pymjl.nettydemo.entity.RpcResponse;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/18 22:48
 **/
public class ClientMain {
    public static void main(String[] args) {
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName("interface")
                .methodName("hello").build();
        NettyClient nettyClient = new NettyClient("127.0.0.1", 8080);
        for (int i = 0; i < 3; i++) {
            nettyClient.sendMessage(rpcRequest);
        }
        RpcResponse rpcResponse = nettyClient.sendMessage(rpcRequest);
        System.out.println(rpcResponse.toString());
    }
}
