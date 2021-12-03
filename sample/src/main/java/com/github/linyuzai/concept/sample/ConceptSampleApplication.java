package com.github.linyuzai.concept.sample;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class ConceptSampleApplication {

    @SneakyThrows
    public static void main(String[] args) {
        /*File file = new File("/Users/tanghanzheng/Downloads/java虚拟机3/README.txt");
        File out = new File("/Users/tanghanzheng/Downloads/java虚拟机3/README_GBK.txt");
        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             FileOutputStream fos = new FileOutputStream(out);
             OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK")) {
            int len;
            char[] chars = new char[1024];
            while ((len = isr.read(chars)) > 0) {
                osw.write(chars, 0, len);
            }
        }*/
        SpringApplication.run(ConceptSampleApplication.class, args);
    }
}
