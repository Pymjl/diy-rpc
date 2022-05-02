package cuit.epoch.pymjl.remote.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

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
    /**
     * 请求id
     */
    private Long requestId;

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
    private String targetImpl;

    /**
     * 调用方法传入的参数
     */
    private Object[] parameters;

    /**
     * 参数类型
     */
    private Class<?>[] paramTypes;

    public String getServiceName() {
        return this.getInterfaceName() + this.getTargetImpl() + this.getVersion();
    }
}
