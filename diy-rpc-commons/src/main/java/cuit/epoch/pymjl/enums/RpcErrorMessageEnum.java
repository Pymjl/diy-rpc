package cuit.epoch.pymjl.enums;

import lombok.*;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/3 20:30
 **/
@Getter
@AllArgsConstructor
@ToString
public enum RpcErrorMessageEnum {
    CLIENT_CONNECT_SERVER_FAILURE("客户端连接服务端失败"),

    SERVICE_INVOCATION_FAILURE("服务调用失败"),

    SERVICE_CAN_NOT_BE_FOUND("没有找到指定的服务"),

    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("注册的服务没有实现任何接口"),

    REQUEST_NOT_MATCH_RESPONSE("返回结果错误！请求和返回的相应不匹配");

    /**
     * RPC异常信息
     */
    private final String message;
}
