package linec.monitor.server.config;

import linec.monitor.base.model.MapWithExpiration;
import linec.monitor.base.model.Service;
import linec.monitor.base.model.ServiceTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfig {

    @Bean
    public MapWithExpiration servicePool(){
        return new MapWithExpiration<String , Service>(5000);
    }

    @Bean
    public MapWithExpiration serviceTemplatePool(){
        return new MapWithExpiration<String , ServiceTemplate>(60000 * 60 *24);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
