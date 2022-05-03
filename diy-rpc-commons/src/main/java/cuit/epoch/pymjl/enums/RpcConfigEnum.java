package cuit.epoch.pymjl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/2 20:08
 **/
@AllArgsConstructor
@Getter
public enum RpcConfigEnum {

    RPC_CONFIG_PATH("META-INF/rpc.properties"),
    ZK_ADDRESS("rpc.zookeeper.address");

    private final String propertyValue;

}
