package linec.monitor.server.controller;

import linec.monitor.base.model.MapWithExpiration;
import linec.monitor.base.model.ServiceTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private MapWithExpiration serviceTemplatePool;

    @Autowired
    private RestTemplate template;

    @RequestMapping("/{ip}/register")
    public List<ServiceTemplate> register(@PathVariable String ip) {
        return serviceTemplatePool.list();
    }

    @RequestMapping("/{ip}/top")
    public String top(@PathVariable String ip, @RequestParam String pids) {
        return template.getForObject("http://" + ip + ":8091/action/top?pids=" + pids, String.class);
    }
}
