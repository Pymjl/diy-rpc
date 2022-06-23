package cuit.epoch.pymjl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/23 20:43
 **/
@AllArgsConstructor
@Getter
public enum CompressTypeEnum {

    /**
     * gzip
     */
    GZIP((byte) 0x01, "gzip");

    private final byte code;
    private final String name;

    public static String getName(byte code) {
        for (CompressTypeEnum c : CompressTypeEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return null;
    }

}
