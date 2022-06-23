package cuit.epoch.pymjl.compress;

import cuit.epoch.pymjl.annotations.MySpi;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/23 22:29
 **/
@MySpi
public interface Compress {
    /**
     * 压缩
     *
     * @param bytes 字节
     * @return {@code byte[]}
     */
    byte[] compress(byte[] bytes);


    /**
     * 解压缩
     *
     * @param bytes 字节
     * @return {@code byte[]}
     */
    byte[] decompress(byte[] bytes);
}
