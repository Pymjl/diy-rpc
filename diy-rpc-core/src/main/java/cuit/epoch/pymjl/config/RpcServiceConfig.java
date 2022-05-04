package cuit.epoch.pymjl.config;

import lombok.*;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/4 9:16
 **/
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class RpcServiceConfig {
    /**
     * 版本
     */
    private String version;
    /**
     * 当接口有多个实现类的时候，按组区分
     */
    private String group;
    /**
     * 目标服务
     */
    private Object service;

    public String getRpcServiceName() {
        return this.getServiceName() + "#" + this.getGroup() + "#" + this.getVersion();
    }

    /**
     * FIXME 这里我不是很理解，如果服务实现了多个接口的化就无法获取了？
     */
    public String getServiceName() {
        return this.service.getClass().getInterfaces()[0].getCanonicalName();
    }
}
