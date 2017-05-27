package linec.monitor.server;

import linec.monitor.base.model.MapWithExpiration;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableAutoConfiguration
public class Application implements ApplicationContextAware {
    private static ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        MapWithExpiration servicePool = (MapWithExpiration)context.getBean("servicePool");
        servicePool.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}

