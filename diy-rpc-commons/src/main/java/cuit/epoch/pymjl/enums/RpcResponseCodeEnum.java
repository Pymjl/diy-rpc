package cuit.epoch.pymjl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/4 11:44
 **/
@AllArgsConstructor
@Getter
@ToString
public enum RpcResponseCodeEnum {
    SUCCESS(200, "The remote call is successful"),
    FAIL(500, "The remote call is fail");
    private final int code;

    private final String message;
}
