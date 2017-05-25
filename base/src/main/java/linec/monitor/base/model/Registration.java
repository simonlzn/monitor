package linec.monitor.base.model;

import java.io.Serializable;

public class Registration implements Serializable{
    private Long pid;
    private String ip;
    private String port;
    private String searchKey;
    private String startScript;
    private String name;
    private String logFilePath;

    public Registration() {
    }

    public Registration(String ip,Long pid) {
        this.pid = pid;
        this.ip = ip;
    }

    public Registration(Long pid, String ip, String port, String searchKey, String startScript, String name) {
        this.pid = pid;
        this.ip = ip;
        this.port = port;
        this.searchKey = searchKey;
        this.startScript = startScript;
        this.name = name;
        this.logFilePath = "`pwd`/log/current.log";
    }

    public Registration(Long pid, String ip, String port, String searchKey, String startScript, String name, String logFilePath) {
        this.pid = pid;
        this.ip = ip;
        this.port = port;
        this.searchKey = searchKey;
        this.startScript = startScript;
        this.name = name;
        this.logFilePath = logFilePath;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogFilePath() {
        return logFilePath;
    }

    public void setLogFilePath(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Registration that = (Registration) o;

        if (!ip.equals(that.ip)) return false;
        return pid.equals(that.pid);

    }

    @Override
    public int hashCode() {
        int result = ip.hashCode();
        result = 31 * result + pid.hashCode();
        return result;
    }
}
