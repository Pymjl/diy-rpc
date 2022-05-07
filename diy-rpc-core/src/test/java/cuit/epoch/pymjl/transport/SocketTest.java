package cuit.epoch.pymjl.transport;

import cuit.epoch.pymjl.remote.transport.socket.SocketServer;
import org.junit.jupiter.api.Test;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/4 12:08
 **/
public class SocketTest {
    @Test
    void testSocket() {
        SocketServer server = new SocketServer();
        server.start();

    }
}
