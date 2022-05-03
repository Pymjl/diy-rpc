package cuit.epoch.pymjl.exception;

import cuit.epoch.pymjl.enums.RpcErrorMessageEnum;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/3 20:28
 **/
public class RpcException extends RuntimeException {
    public RpcException(RpcErrorMessageEnum errorMessageEnum, String msg) {
        super(errorMessageEnum.getMessage() + ":" + msg);
    }

    public RpcException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RpcException(RpcErrorMessageEnum errorMessageEnum) {
        super(errorMessageEnum.getMessage());
    }
}
