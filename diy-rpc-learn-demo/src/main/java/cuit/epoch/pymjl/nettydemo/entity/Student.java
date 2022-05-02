package cuit.epoch.pymjl.nettydemo.entity;

import lombok.*;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/18 21:47
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
public class Student {
    private String name;
    private Integer age;
    private String gender;
    private String email;
    private String phone;
}
