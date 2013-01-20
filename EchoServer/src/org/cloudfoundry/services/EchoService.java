package org.cloudfoundry.services;
 
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
 
public class EchoService {
        private ServerSocket serverSocket;
 
        public EchoService(String ipAddress, int port) throws Exception {
                serverSocket = new ServerSocket(port, 0, InetAddress.getByName(ipAddress));
                while (true) {
                        Socket clientSocket = serverSocket.accept();
                        RequestHandler service = new RequestHandler(clientSocket);
                        service.start();
                }
        }
 
        public void destroy() throws Exception {
                serverSocket.close();
        }
 
        class RequestHandler extends Thread {
                Socket clientSocket;
 
                public RequestHandler(Socket socket) {
                        this.clientSocket = socket;
                }
 
                @Override
                public void run() {
                        try {
                                InputStream is = this.clientSocket.getInputStream();
                                OutputStream os = this.clientSocket.getOutputStream();
                                int bytesRead = 0;
                                byte[] byteArray = new byte[1024];
                                System.out.print("\nEcho message: ");
                                while ((bytesRead = is.read(byteArray, 0, byteArray.length)) != -1) {
                                        os.write(byteArray, 0, bytesRead);
                                        System.out.print(new String(Arrays.copyOfRange(byteArray,
                                                        0, bytesRead), Charset.forName("UTF-8")));
 
                                }
                                os.flush();
                                is.close();
                                os.close();
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                }
        }
 
        private static Map<String, String> getArguments(String[] args) {
                Map<String, String> argsMap = new HashMap<String, String>();
 
                int argsCount = args.length;
                for (int i = 0; i < argsCount; i++) {
                        if (args[i].startsWith("-")) {
                                if (i != argsCount - 1) {
                                        argsMap.put(args[i], args[i + 1]);
                                }
                        }
                }
                return argsMap;
        }
 
        enum ConsoleArguments {
                port,
                ipaddress
        };
 
        private static void getHelp() {
                System.out.println("\n##### HELP SECTION #####"
                                + "\nStart echo service with arguments:\n\n"
                                + "-ipaddress ipaddress [-port] <port number>");
        }
 
        public static void main(String[] args) throws Exception {
                Map<String, String> argsMap = getArguments(args);
                String portValue = argsMap.get("-" + ConsoleArguments.port.toString());
                String ipaddressValue = argsMap.get("-" + ConsoleArguments.ipaddress.toString());
 
                if (portValue != null) {
                        try {
                                new EchoService(ipaddressValue, Integer.parseInt(portValue));
                        } catch (NumberFormatException e) {
                                getHelp();
                        }
                } else {
                        getHelp();
                }
        }
}
