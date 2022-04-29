package cuit.epoch.pymjl.nettydemo.entity;

import lombok.*;

/**
 * 服务端响应实体类
 *
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/18 21:48
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
public class RpcResponse {
    private String message;
}
