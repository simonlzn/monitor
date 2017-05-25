package linec.monitor.client;

import linec.monitor.base.model.Registration;
import linec.monitor.base.model.Service;
import linec.monitor.base.model.ServiceTemplate;
import linec.monitor.base.util.IpUtil;
import linec.monitor.client.util.ProcessUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.SocketException;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        register();
    }

    private static void register() {
        RestTemplate restTemplate = new RestTemplate();
        String monitorServerUrl = "http://localhost:8090";
        try {
            String currentIp = IpUtil.getCurrentIp();
            ServiceTemplate[] list = restTemplate.postForObject(monitorServerUrl + "/client/" + currentIp + "/register", null, ServiceTemplate[].class);
            List<ServiceTemplate> serviceTemplates = Arrays.asList(list);

            for (int i = 0; i < serviceTemplates.size(); i++) {
                try {
                    List<Long> pids = ProcessUtil.getPidsByCommand(serviceTemplates.get(i).getSearchKey());
                    for (int j = 0; j < pids.size(); j++) {
                        Registration registration = new Registration(pids.get(j), currentIp, serviceTemplates.get(i).getPort(), serviceTemplates.get(i).getSearchKey(), serviceTemplates.get(i).getStartScript(), serviceTemplates.get(i).getName());
                        restTemplate.put(monitorServerUrl + "/register", registration);
                        Service service = new Service(pids.get(j), registration);
                        service.heartbeat();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}

