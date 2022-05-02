package cuit.epoch.pymjl.kryodemo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/18 18:01
 **/
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class Student implements Serializable {
    private static final long serialVersionUID = -91809837793898L;
    private String name;
    private String gender;
    private Integer age;
}
