package cuit.epoch.pymjl.proxydemo.dynamicproxy.jdk;

import lombok.extern.log4j.Log4j2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 自定义一个InvocationHandler，实现InvocationHandler接口，并实现invoke方法
 *
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/19 16:04
 **/
@Log4j2
public class MyInvocationHandler implements InvocationHandler {
    /**
     * This is the target object that we are invoking all methods on.
     */
    private final Object target;

    /**
     * Constructor for MyInvocationHandler.
     *
     * @param target the target object that we are invoking all methods on.
     */
    public MyInvocationHandler(Object target) {
        this.target = target;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //Before method invocation, we can do some pre-processing.
        log.info("Before invoking " + method.getName());
        //Invoke the method on the target object.
        Object result = method.invoke(target, args);
        //After method invocation, we can do some post-processing.
        log.info("After invoking " + method.getName());
        return result;
    }
}
