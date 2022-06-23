package cuit.epoch.pymjl.compress;

import cuit.epoch.pymjl.compress.gzip.GzipCompress;
import cuit.epoch.pymjl.enums.CompressTypeEnum;
import cuit.epoch.pymjl.spi.ExtensionLoader;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/23 23:14
 **/
public class CompressTest {
    @Test
    void getName() {
        System.out.println(Compress.class.getCanonicalName());
        System.out.println(GzipCompress.class.getCanonicalName());
    }

    @Test
    void testSPI() {
        Compress compress = ExtensionLoader.getExtensionLoader(Compress.class)
                .getExtension("gzip");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100000; i++) {
            sb.append("hello world gzip ");
        }
        System.out.println(sb.toString().getBytes(StandardCharsets.UTF_8).length);
        System.out.println(compress.compress(sb.toString().getBytes(StandardCharsets.UTF_8)).length);
    }
}
