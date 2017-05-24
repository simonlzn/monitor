package linec.monitor.server.controller;

import linec.monitor.base.model.MapWithExpiration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/list")
public class ListController {

    @Autowired
    private MapWithExpiration servicePool;

    @Autowired
    private MapWithExpiration serviceTemplatePool;

    @RequestMapping("/service")
    public List serviceList(){
        return servicePool.list();
    }

    @RequestMapping("/serviceTemplate")
    public List serviceTemplateList(){
        return serviceTemplatePool.list();
    }
}
