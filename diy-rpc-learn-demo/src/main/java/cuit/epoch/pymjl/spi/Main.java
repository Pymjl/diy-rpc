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
        log.info("这是Java原生的SPI机制");
        ServiceLoader<Animal> serviceLoader = ServiceLoader.load(Animal.class);
        for (Animal animal : serviceLoader) {
            animal.call();
        }

    }
}
