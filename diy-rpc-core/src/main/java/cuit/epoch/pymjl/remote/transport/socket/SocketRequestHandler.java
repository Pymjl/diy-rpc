package cuit.epoch.pymjl.remote.transport.socket;

import cuit.epoch.pymjl.factory.SingletonFactory;
import cuit.epoch.pymjl.remote.entity.RpcRequest;
import cuit.epoch.pymjl.remote.entity.RpcResponse;
import cuit.epoch.pymjl.remote.handler.RpcRequestHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/4 11:27
 **/
@Slf4j
public class SocketRequestHandler implements Runnable {
    private final Socket socket;
    private final RpcRequestHandler rpcRequestHandler;

    public SocketRequestHandler(Socket socket) {
        this.socket = socket;
        this.rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
    }

    @Override
    public void run() {
        log.info("服务端通过线程[{}]处理客户端的消息", Thread.currentThread().getName());
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            //通过Socket获取流读写数据
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            Object result = rpcRequestHandler.handle(rpcRequest);
            objectOutputStream.writeObject(RpcResponse.success(result, rpcRequest.getRequestId()));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            log.error(e.getLocalizedMessage());
        }

    }
}
