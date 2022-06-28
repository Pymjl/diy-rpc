package cuit.epoch.pymjl.remote.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/23 22:26
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RpcMessage implements Serializable {
    private static long serialVersionUID = 1623465139976L;
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
