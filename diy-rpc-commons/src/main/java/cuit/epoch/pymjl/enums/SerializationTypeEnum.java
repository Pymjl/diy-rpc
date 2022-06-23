package cuit.epoch.pymjl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/23 22:30
 **/
@AllArgsConstructor
@Getter
public enum SerializationTypeEnum {
    KYRO((byte) 0x01, "kryo"),
    PROTOSTUFF((byte) 0x02, "protostuff"),
    HESSIAN((byte) 0X03, "hessian");

    private final byte code;
    private final String name;

    public static String getName(byte code) {
        for (SerializationTypeEnum c : SerializationTypeEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return null;
    }
}
