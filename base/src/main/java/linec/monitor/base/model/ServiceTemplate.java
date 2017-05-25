package linec.monitor.base.model;

import java.io.Serializable;

public class ServiceTemplate implements Serializable{
    private String name;
    private String port;
    private String searchKey;
    private String startScript;
    private String logFilePath;

    public ServiceTemplate() {
    }

    public ServiceTemplate(String name, String port, String searchKey, String startScript, String logFilePath) {
        this.name = name;
        this.port = port;
        this.searchKey = searchKey;
        this.startScript = startScript;
        this.logFilePath = logFilePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getStartScript() {
        return startScript;
    }

    public void setStartScript(String startScript) {
        this.startScript = startScript;
    }

    public String getLogFilePath() {
        return logFilePath;
    }

    public void setLogFilePath(String logFilePath) {
        this.logFilePath = logFilePath;
    }
}
