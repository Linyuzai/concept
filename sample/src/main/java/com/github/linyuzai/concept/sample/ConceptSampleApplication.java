package com.github.linyuzai.concept.sample;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;

@SpringBootApplication
public class ConceptSampleApplication {

    @SneakyThrows
    public static void main(String[] args) {
        File file = new File("/Users/tanghanzheng/Downloads/java虚拟机3/README.txt");
        File out = new File("/Users/tanghanzheng/Downloads/java虚拟机3/README_GBK.txt");
        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis);
             FileOutputStream fos = new FileOutputStream(out);
             OutputStreamWriter osw = new OutputStreamWriter(fos);
             ) {
            //isr.
        }
        //SpringApplication.run(ConceptSampleApplication.class, args);
    }
}
