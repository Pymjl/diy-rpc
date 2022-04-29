package cuit.epoch.pymjl.proxydemo.dynamicproxy.cglib;

import net.sf.cglib.proxy.Enhancer;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/19 16:36
 **/
public class CglibProxyFactory {
    public static Object getProxy(Class<?> clazz) {
        // 1.Create enhancers for creating dynamic proxy classes
        Enhancer enhancer = new Enhancer();
        // 2.set classloader
        enhancer.setClassLoader(clazz.getClassLoader());
        // 3.set superclass
        enhancer.setSuperclass(clazz);
        //4.Set up method interceptors
        enhancer.setCallback(new MyMethodInterceptor());
        //5.Create a proxy class object
        return enhancer.create();
    }
}
