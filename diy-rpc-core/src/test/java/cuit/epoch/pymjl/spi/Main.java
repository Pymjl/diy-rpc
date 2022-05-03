package cuit.epoch.pymjl.spi;

import cuit.epoch.pymjl.loadbalance.LoadBalance;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/3 19:22
 **/
public class Main {
    @Test
    void testLoadDir() {
        String fileName = "META-INF/diy-rpc/cuit.epoch.pymjl.loadbalance.LoadBalance";
        try {
            Enumeration<URL> urls;
            ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
            urls = classLoader.getResources(fileName);
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    URL resourceUrl = urls.nextElement();
                    System.out.println(resourceUrl);
                }
            }
        } catch (IOException e) {
            System.out.println("wewewew");
        }

    }
}
