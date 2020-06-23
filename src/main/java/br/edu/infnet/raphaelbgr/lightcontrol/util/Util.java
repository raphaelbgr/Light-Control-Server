package br.edu.infnet.raphaelbgr.lightcontrol.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class Util {

    public static boolean isReachableByPing(String host) {
        for (int i = 0; i <= 5; i++) {
            try {
                String cmd;
                // For Linux and OSX
                if (System.getProperty("os.name").startsWith("Windows")) {
                    // For Windows
                    cmd = "ping -n 1 " + host;
                } else cmd = "ping -c 1 " + host;

                Process myProcess = Runtime.getRuntime().exec(cmd);
                myProcess.waitFor();

                if (myProcess.exitValue() == 0) {
//                System.out.println("SERVER> Ping to " + host + " SUCCESS");
                    return true;
                } else {
                    System.err.println("SERVER> Ping to " + host + " FAILURE");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            waitOneSec();
        }
        return false;
    }

    public static boolean isReachableByHttp(String host) {
        for (int i = 0; i <= 5; i++) {
            try {
                URL url = new URL(host);
                URLConnection connection = url.openConnection();

                if (connection.getContentLength() == -1) {
                    System.err.println("SERVER> URLConnection failed to host: " + host);
                    return false;
                }
                return true;
            } catch (IOException e) {
                System.err.println("SERVER> IOException to host: " + host);
                e.printStackTrace();
            }
            waitOneSec();
        }
        return false;
    }

    private static void waitOneSec() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
