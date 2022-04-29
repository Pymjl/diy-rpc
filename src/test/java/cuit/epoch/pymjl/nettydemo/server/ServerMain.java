package cuit.epoch.pymjl.nettydemo.server;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/18 22:48
 **/
public class ServerMain {
    public static void main(String[] args) {
        new NettyServer(8080).run();
    }
}
