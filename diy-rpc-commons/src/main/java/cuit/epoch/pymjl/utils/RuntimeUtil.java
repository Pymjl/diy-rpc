package cuit.epoch.pymjl.utils;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/24 21:10
 **/
public class RuntimeUtil {
    /**
     * 获取CPU的核心数
     *
     * @return cpu的核心数
     */
    public static int cpus() {
        return Runtime.getRuntime().availableProcessors();
    }
}
