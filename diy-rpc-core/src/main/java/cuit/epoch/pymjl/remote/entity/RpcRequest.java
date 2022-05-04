package cuit.epoch.pymjl.remote.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/2 10:36
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RpcRequest implements Serializable {
    private static long serialVersionUID = 1651465223590L;
    /**
     * 请求id
     */
    private String requestId;

    /**
     * 调用的服务版本，兼容服务端的升级
     */
    private String version;

    /**
     * 请求调用的接口名称
     */
    private String interfaceName;

    /**
     * 请求调用的方法名称
     */
    private String methodName;

    /**
     * 用于处理一个接口有多个实现类的情况
     */
    private String group;

    /**
     * 调用方法传入的参数
     */
    private Object[] parameters;

    /**
     * 参数类型
     */
    private Class<?>[] paramTypes;

    public String getRpcServiceName() {
        return this.getInterfaceName() + "#" + this.getGroup() + "#" + this.getVersion();
    }
}
