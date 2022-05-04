package cuit.epoch.pymjl.remote.transport.socket;

import cuit.epoch.pymjl.exception.RpcException;
import cuit.epoch.pymjl.registry.ServiceDiscovery;
import cuit.epoch.pymjl.remote.entity.RpcRequest;
import cuit.epoch.pymjl.remote.transport.RpcRequestTransport;
import cuit.epoch.pymjl.spi.ExtensionLoader;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/4 11:58
 **/
public class SocketClient implements RpcRequestTransport {
    private final ServiceDiscovery serviceDiscovery;

    public SocketClient() {
        this.serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("zookeeper");
    }

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        InetSocketAddress inetSocketAddress = serviceDiscovery.findService(rpcRequest);
        try (Socket socket = new Socket()) {
            socket.connect(inetSocketAddress);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // 通过输出流向服务器发送数据
            objectOutputStream.writeObject(rpcRequest);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            // 服务端返回的响应
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RpcException("调用服务失败:", e);
        }
    }
}
