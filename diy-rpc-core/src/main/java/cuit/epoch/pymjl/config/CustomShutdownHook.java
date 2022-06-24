package cuit.epoch.pymjl.config;

import cuit.epoch.pymjl.remote.transport.netty.server.NettyServer;
import cuit.epoch.pymjl.utils.CuratorUtils;
import cuit.epoch.pymjl.utils.concurrent.threadpool.ThreadPoolFactoryUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/4 11:51
 **/
@Slf4j
public class CustomShutdownHook {
    private static final CustomShutdownHook CUSTOM_SHUTDOWN_HOOK = new CustomShutdownHook();

    public static CustomShutdownHook getCustomShutdownHook() {
        return CUSTOM_SHUTDOWN_HOOK;
    }

    public void clearAll() {
        log.info("清理服务端节点下的所有子节点");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                //FIXME 这里的端口号暂时写死为8080
                InetSocketAddress inetSocketAddress =
                        new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), NettyServer.PORT);
                CuratorUtils.clearRegistry(CuratorUtils.getZkClient(), inetSocketAddress);
            } catch (UnknownHostException ignored) {
            }
            ThreadPoolFactoryUtil.shutDownAllThreadPool();
        }));
    }
}
