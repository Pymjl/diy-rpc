package cuit.epoch.pymjl;

import cuit.epoch.pymjl.enums.RpcConfigEnum;
import cuit.epoch.pymjl.utils.PropertiesFileUtil;

import java.util.Properties;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/3 21:18
 **/
public class Test {
    public static void main(String[] args) {
        Properties properties = PropertiesFileUtil.readPropertiesFile(RpcConfigEnum.RPC_CONFIG_PATH.getPropertyValue());
        System.out.println(properties.toString());
    }
}
