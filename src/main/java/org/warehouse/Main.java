package org.warehouse;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Main.class, args);
//        for(String beanName : ctx.getBeanDefinitionNames()) {
//            System.out.println(ctx.getBean(beanName));
//        }
    }
}
