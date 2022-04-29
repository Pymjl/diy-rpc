package cuit.epoch.pymjl.annotations.entity;

import cuit.epoch.pymjl.annotations.FieldsAnnotation;
import cuit.epoch.pymjl.annotations.TableAnnotation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/29 16:16
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@TableAnnotation("t_student")
public class Student {
    @FieldsAnnotation(value = "db_name",length = 255)
    private String name;
    @FieldsAnnotation(value = "db_password",length = 255)
    private String password;
    @FieldsAnnotation(value = "db_age",type = "int",length = 10)
    private Integer age;
    @FieldsAnnotation(value = "db_name",type = "char",length = 2)
    private String gender;

    private void sayHello() {
        System.out.println(this.name + "say: Hello world");
    }
}
