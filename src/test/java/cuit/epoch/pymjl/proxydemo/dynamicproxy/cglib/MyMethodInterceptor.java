package cuit.epoch.pymjl.proxydemo.dynamicproxy.cglib;

import lombok.extern.log4j.Log4j2;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/19 16:29
 **/
@Log4j2
public class MyMethodInterceptor implements MethodInterceptor {
    /**
     * @param o           proxied object
     * @param method      methods of the proxied object
     * @param args        Arguments to the methods of the proxied object
     * @param methodProxy A proxy for the method of the proxy object, used to call the original method
     * @return the result of the method of the proxied object
     * @throws Throwable if an exception is thrown by the method of the proxied object
     */

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        log.info("before method");
        Object result = methodProxy.invokeSuper(o, args);
        log.info("after method");
        return result;
    }
}
