package com.github.linyuzai.concept.sample;

import com.github.linyuzai.concept.sample.sync.MapBlockingQueue;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.*;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@EnableSwagger2
@SpringBootApplication(scanBasePackages = "com.github.linyuzai.concept.sample.event")
public class ConceptSampleApplication {

    private final MapBlockingQueue<String> queue = new MapBlockingQueue<>();

    public static void main(String[] args) {
        SpringApplication.run(ConceptSampleApplication.class, args);
    }

    @SneakyThrows
    public static void test() {
        long sleep = 100;
        ConceptSampleApplication application = new ConceptSampleApplication();
        application.startPut("A");
        Thread.sleep(sleep);
        application.startPut("B");
        Thread.sleep(sleep);
        application.startPut("C");
        Thread.sleep(sleep);
        application.startPut("D");
        Thread.sleep(sleep);
        application.startTake();
    }

    @SneakyThrows
    public void startTake() {
        while (true) {
            String s = queue.take();
            System.out.println(s);
            System.out.println(queue.size() + ":" + queue.getMap());
            Thread.sleep(1000);
        }
    }

    public void startPut(String s) {
        new Thread() {

            int i;

            @SneakyThrows
            @Override
            public void run() {
                while (true) {
                    queue.put(s, s + i++);
                    Thread.sleep(100);
                }
            }
        }.start();

    }

    public class StringArrayList extends ArrayList<String> {

    }

    public class ClassArrayList extends ArrayList<Class<? extends Serializable>> {

    }

    public class ClassesArrayList extends ArrayList<Class<? extends Serializable>[]> {

    }

    public class Custom<T extends String & Serializable & Cloneable> {
    }

    public interface CustomEx extends Serializable, Cloneable {

    }
}
