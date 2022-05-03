package cuit.epoch.pymjl.spi;

/**
 * 持有人
 *
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/3 15:11
 **/
public class Holder<T> {
    private volatile T value;

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
