package br.edu.infnet.raphaelbgr.lightcontrol.util;

public class Util {

    public static boolean isReachableByPing(String host) {
        try {
            String cmd = "";
            if (System.getProperty("os.name").startsWith("Windows")) {
                // For Windows
                cmd = "ping -n 1 " + host;
            } else {
                // For Linux and OSX
                cmd = "ping -c 1 " + host;
            }

            Process myProcess = Runtime.getRuntime().exec(cmd);
            myProcess.waitFor();

            if (myProcess.exitValue() == 0) {
//                System.out.println("SERVER> Ping to " + host + " SUCCESS");
                return true;
            } else {
                System.out.println("SERVER> Ping to " + host + " FAILURE");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
