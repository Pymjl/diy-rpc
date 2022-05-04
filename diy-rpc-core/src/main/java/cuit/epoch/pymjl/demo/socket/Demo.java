package cuit.epoch.pymjl.demo.socket;

import cuit.epoch.pymjl.remote.transport.socket.SocketServer;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/4 12:15
 **/
public class Demo {
    public static void main(String[] args) {
        SocketServer server = new SocketServer();
        server.start();

    }
}
