package linec.monitor.client.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ProcessUtil {
    public static synchronized long getPidOfProcess(Process p) {
        long pid = -1;

        try {
            if (p.getClass().getName().equals("java.lang.UNIXProcess")) {
                Field f = p.getClass().getDeclaredField("pid");
                f.setAccessible(true);
                pid = f.getLong(p);
                f.setAccessible(false);
            }
        } catch (Exception e) {
            pid = -1;
        }
        return pid;
    }

    public static synchronized List<Long> getPidsByCommand(String command) throws InterruptedException, IOException {
        Vector<String> commands=new Vector<String>();
        commands.add("/bin/bash");
        commands.add("-c");
        commands.add("ps -eo pid,command | grep " + "[" + command.charAt(0) + "]" + command.substring(1));
        ProcessBuilder pb=new ProcessBuilder(commands);

        Process pr=pb.start();
        pr.waitFor();
        BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        List<Long> pids = new ArrayList<>();
        String line;
        while ((line = input.readLine()) != null) {
            while(line.startsWith(" "))
                line = line.substring(1);
            pids.add(Long.parseLong(line.substring(0, line.indexOf(" "))));
        }
        input.close();

        return pids;
    }
}
