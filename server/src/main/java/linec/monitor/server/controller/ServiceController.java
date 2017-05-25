package linec.monitor.server.controller;

import linec.monitor.base.model.MapWithExpiration;
import linec.monitor.base.model.Registration;
import linec.monitor.base.model.Service;
import linec.monitor.base.model.ServiceTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/service")
public class ServiceController {

    @Autowired
    private MapWithExpiration<String, Service> servicePool;

    @Autowired
    private MapWithExpiration<String, ServiceTemplate> serviceTemplatePool;


    @RequestMapping(value = "/{ipAndPid}/heartbeat", method = RequestMethod.POST)
    public boolean heartbeat(@PathVariable String ipAndPid){
        if (servicePool.contains(ipAndPid)) {
            servicePool.renew(ipAndPid);
            return true;
        }else {
            return false;
        }
    }

    @RequestMapping(value = "/{ipAndPid}/dead", method = RequestMethod.POST)
    public void dead(@PathVariable String ipAndPid){
        servicePool.remove(ipAndPid);
    }

    @RequestMapping(value = "/{ipAndPid}/log")
    public String log(@PathVariable String ipAndPid){
        String[] split = ipAndPid.split(":");

        if (split.length != 2)
            return "";

        String ip = split[0];

        Service service = servicePool.get(ipAndPid);

        ServiceTemplate serviceTemplate = serviceTemplatePool.get(service.getName());
        String logFilePath = serviceTemplate.getLogFilePath();

        RestTemplate template = new RestTemplate();

        String ret = template.getForObject("http://" + ip + ":9090/action/log?logFilePath=" + logFilePath, String.class);

        return ret;
    }

    @RequestMapping(value = "/{ipAndPid}/kill")
    public void kill(@PathVariable String ipAndPid){
        String[] split = ipAndPid.split(":");

        if (split.length != 2)
            return;

        String ip = split[0];
        String pid = split[1];

        RestTemplate template = new RestTemplate();

        template.postForLocation("http://" + ip + ":9090/action/" + pid + "/kill", null);
    }

    @RequestMapping(value = "/{ip}/create", method = RequestMethod.POST)
    public void create(@PathVariable String ip, @RequestParam String name){
        ServiceTemplate serviceTemplate = serviceTemplatePool.get(name);

        Registration registration = new Registration(0L, ip, serviceTemplate.getPort(), serviceTemplate.getSearchKey(), serviceTemplate.getStartScript(), name);
        RestTemplate template = new RestTemplate();
        template.postForLocation("http://" + ip + ":9090/action/create", registration);
    }
}
