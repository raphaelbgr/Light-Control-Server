package br.edu.infnet.raphaelbgr.lightcontrol.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class Util {

    public static boolean isReachableByPing(String host) {
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
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isReachableByHttp(String host) {
        try {
            URL url = new URL(host);
            URLConnection connection = url.openConnection();

            if (connection.getContentLength() == -1) {
                System.err.println("Failed to verify connection");
                return false;
            }
            return true;
        } catch (IOException e) {
            System.err.println("Failed to open a connection");
            e.printStackTrace();
            return false;
        }
    }
}
