package cuit.epoch.pymjl.spi;

import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/2 17:47
 **/
@Slf4j
public class Main {
    public static void main(String[] args) {
        log.info("这是Dubbo的SPI机制");
        Animal cat = ExtensionLoader.getExtensionLoader(Animal.class)
                .getExtension("cat");
        Animal dog = ExtensionLoader.getExtensionLoader(Animal.class)
                .getExtension("dog");
        cat.call();
        dog.call();

    }
}
