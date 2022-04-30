package cuit.epoch.pymjl.annotations;

/**
 * 这是一个容器注解，用来存放其他的注解，
 * 默认需要一个与要存放的注解类型匹配的数组value属性值
 *
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/30 15:08
 **/
public @interface Animals {
    Animal[] value();
}
