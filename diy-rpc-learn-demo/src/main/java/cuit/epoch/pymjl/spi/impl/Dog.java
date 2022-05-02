package cuit.epoch.pymjl.spi.impl;

import cuit.epoch.pymjl.spi.Animal;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/2 17:45
 **/
public class Dog implements Animal {
    @Override
    public void call() {
        System.out.println("狗叫：汪汪汪~");
    }
}
