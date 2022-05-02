package cuit.epoch.pymjl.proxydemo.dynamicproxy.jdk;


import java.lang.reflect.Proxy;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/19 16:10
 **/
public class JdkProxyFactory {
    public static Object getProxy(Object target) {
                //1.Get the classloader of the proxy class
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                //2.Get the interface of the proxy class, you can specify multiple interface
                target.getClass().getInterfaces(),
                //3.Get the InvocationHandler of the proxy class
                new MyInvocationHandler(target));
    }
}
