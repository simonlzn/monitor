package linec.monitor.server.controller;

import linec.monitor.base.model.MapWithExpiration;
import linec.monitor.base.model.Registration;
import linec.monitor.base.model.Service;
import linec.monitor.base.model.ServiceTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    private MapWithExpiration servicePool;

    @Autowired
    private MapWithExpiration serviceTemplatePool;


    @RequestMapping(value = "", method = RequestMethod.PUT)
    public void register(@RequestBody Registration registration){
        Service service = new Service(registration.getPid(), registration.getIp(), registration.getPort(), registration.getName());
        servicePool.put(registration.getIp()+":"+registration.getPid(), service);
        ServiceTemplate serviceTemplate = new ServiceTemplate(registration.getName(), registration.getPort(), registration.getSearchKey(), registration.getStartScript(), registration.getLogFilePath());
        serviceTemplatePool.put(registration.getName(), serviceTemplate);
    }
}
