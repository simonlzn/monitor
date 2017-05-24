package linec.monitor.client.controller;

import linec.monitor.base.model.Registration;
import linec.monitor.base.model.Service;
import linec.monitor.client.util.ProcessUtil;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

@RestController
@RequestMapping("/action")
public class ActionController {
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void create(@RequestBody Registration registration){
        try {
            Process p = Runtime.getRuntime().exec(registration.getStartScript());
            long pid = ProcessUtil.getPidOfProcess(p);
            registration.setPid(pid);

            Service service = new Service(pid, registration);
            service.heartbeat();

//            Application.servicePool.put(ProcessUtil.getPidOfProcess(p), service);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "/{pid}/kill", method = RequestMethod.POST)
    public void kill(@PathVariable Long pid) throws IOException {
        Runtime.getRuntime().exec("kill -9 " + pid);
    }

    @RequestMapping("/top")
    public String top(@RequestParam String pids){
        String ret = "";

        try {
            String[] split = pids.split(",");
            String pidComm="";
            for (int i = 0; i < split.length; i++) {
                pidComm += " -p " + split[i];
            }
            Process process = Runtime.getRuntime().exec("top -b -n 1" + pidComm);
            process.waitFor();
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = input.readLine()) != null) {
                ret += line + "\n";
            }

            input = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = input.readLine()) != null) {
                ret += line;
            }

            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }
}
