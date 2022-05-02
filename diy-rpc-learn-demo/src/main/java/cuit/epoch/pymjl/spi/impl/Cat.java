package cuit.epoch.pymjl.spi.impl;

import cuit.epoch.pymjl.spi.Animal;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/2 17:44
 **/
public class Cat implements Animal {
    @Override
    public void call() {
        System.out.println("猫叫：喵喵喵~");
    }
}
