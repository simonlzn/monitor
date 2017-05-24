package linec.monitor.client.controller;

import linec.monitor.base.model.Registration;
import linec.monitor.base.model.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/register")
public class RegisterController {
    private RestTemplate template = new RestTemplate();

    private final String monitorServerUrl = "http://localhost:8090";

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public void register(@RequestBody Registration registration){
        template.put(monitorServerUrl + "/register", registration);
        Service service = new Service(registration.getPid(), registration);
        service.heartbeat();
    }
}
