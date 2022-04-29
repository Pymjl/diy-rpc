package cuit.epoch.pymjl.nettydemo.entity;

import lombok.*;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/18 22:06
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class RpcRequest {
    private String interfaceName;
    private String methodName;
}
