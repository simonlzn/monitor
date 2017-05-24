package linec.monitor.base.model;

import linec.monitor.base.util.IpUtil;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.util.Vector;

public class Service {
    private Long pid;
    private Registration registration;
    private String ip;
    private String port;
    private String name;

    public Service() {
    }

    public Service(Long pid, String ip, String port, String name) {
        this.pid = pid;
        this.ip = ip;
        this.port = port;
        this.name = name;
    }

    public Service(Long pid, Registration registration) {
        this.pid = pid;
        this.registration = registration;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Registration getRegistration() {
        return registration;
    }

    public void heartbeat() {
        new Thread(() -> {
            RestTemplate template = new RestTemplate();
            String monitorServerUrl = "http://localhost:8090";
            try {
                String currentIp = IpUtil.getCurrentIp();

                while (true) {
                    try {
                        Vector<String> commands = new Vector<String>();
                        commands.add("/bin/bash");
                        commands.add("-c");
                        commands.add("ps -e | grep " + pid);
                        ProcessBuilder pb = new ProcessBuilder(commands);

                        Process pr = pb.start();
                        pr.waitFor();
                        BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                        if (input.readLine() != null) {
                            Boolean succeeded = template.postForObject(monitorServerUrl + "/service/" + currentIp + ":" + pid + "/heartbeat", null, Boolean.class);
                            if (!succeeded)
                                template.put(monitorServerUrl + "/register", registration);
                        } else {
                            template.postForLocation(monitorServerUrl + "/service/" + currentIp + ":" + pid + "/dead", null);
                            input.close();
                            break;
                        }
                        input.close();
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
