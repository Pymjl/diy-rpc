package cuit.epoch.pymjl.remote.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/2 12:13
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RpcResponse<T> implements Serializable {
    private static long serialVersionUID = 1651465139976L;
    /**
     * 请求id
     */
    private String requestId;
    /**
     * 代码
     */
    private Integer code;
    /**
     * 消息
     */
    private String message;
    /**
     * 返回的body
     */
    private T body;

}
