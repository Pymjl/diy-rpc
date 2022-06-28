package cuit.epoch.pymjl.serialize.kryodemo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import cuit.epoch.pymjl.serialize.entity.Student;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/18 18:02
 **/
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        //序列化
        Kryo kryo = new Kryo();
        //关闭注册功能
        kryo.setRegistrationRequired(false);
        //注册类
        Output output = new Output(new FileOutputStream("C:\\Users\\Admin\\JavaProjects\\diy-rpc-test\\test1.txt"));
        Student student = new Student("Pymjl", "男", 20);
        kryo.writeObject(output, student);
        output.close();
        //反序列化
        Input input = new Input(new FileInputStream("C:\\Users\\Admin\\JavaProjects\\diy-rpc-test\\test1.txt"));
        Student readStudent = kryo.readObject(input, Student.class);
        input.close();
        System.out.println(readStudent);
    }
}
