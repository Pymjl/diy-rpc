package cuit.epoch.pymjl.remote.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/23 22:26
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RpcMessage {
    /**
     * rpc message type
     */
    private byte messageType;
    /**
     * serialization type
     */
    private byte codec;
    /**
     * compress type
     */
    private byte compress;
    /**
     * request id
     */
    private int requestId;
    /**
     * request data
     */
    private Object data;
}
