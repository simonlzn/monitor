package linec.monitor.base;

import linec.monitor.base.model.Registration;
import linec.monitor.base.util.IpUtil;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

@Component
public class RegistrationHandler implements EnvironmentAware{
    private static RestTemplate template = new RestTemplate();
    private static Environment environment;

    public RegistrationHandler() {
    }

    private static String monitorServerUrl = "http://localhost:8091";

    public static void register(String name, String startScript, String logDir) throws UnknownHostException, SocketException {
        String currentIp = IpUtil.getCurrentIp();
        String port = environment.getProperty("server.port");
        String description = environment.getProperty("spring.application.name");
        String logFilePath = logDir + "/" + environment.getProperty("logging.file");

        Registration registration = new Registration(getPID(), currentIp, port, name, startScript, description, logFilePath);
        template.put(monitorServerUrl + "/register", registration);
    }

    private static long getPID() {
        String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        if (processName != null && processName.length() > 0) {
            try {
                return Long.parseLong(processName.split("@")[0]);
            }
            catch (Exception e) {
                return 0;
            }
        }

        return 0;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
