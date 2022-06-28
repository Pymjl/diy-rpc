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
     * 得到服务的全限定名称
     *
     * @return {@code String}
     */
    public String getServiceName() {
        return this.service.getClass().getInterfaces()[0].getCanonicalName();
    }
}
