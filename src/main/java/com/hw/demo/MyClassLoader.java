package com.hw.demo;

import org.apache.commons.io.FileUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.lang.reflect.Method;

public class MyClassLoader extends ClassLoader {
    @Override
    protected Class<?> findClass(String defName) {
        byte[] bytes;
        try {
            File file = ResourceUtils.getFile("classpath:Hello.xlass");
            bytes = FileUtils.readFileToByteArray(file);
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) (255 - bytes[i]);
            }
            return defineClass(defName, bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("findClass error!");
        }
    }

    public static void main(String[] args) {
        try {
            Class<?> helloClass = new MyClassLoader().findClass("Hello");
            Method helloMethod = helloClass.getMethod("hello");
            helloMethod.invoke(helloClass.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
