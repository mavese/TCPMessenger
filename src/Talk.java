import java.net.*;
import java.io.*;



public class Talk
{
    public static void main(String[] args)
    {
        int mode = 0;
        String ip = "127.0.0.1";
        int portNumber = 12987;

        if (args.length > 0)
        {
            switch (args[0])
            {
                case "-h":
                    if (args.length > 3)
                    {
                        break;
                    }
                    mode = 1;
                    if (args.length > 1)
                    {
                        ip = args[1];
                        if (args.length > 2)
                        {
                            portNumber = Integer.parseInt(args[2]);
                        }
                    }
                    break;
                case "-s":
                    if (args.length > 2)
                    {
                        break;
                    }
                    mode = 2;
                    if (args.length > 1)
                    {
                        portNumber = Integer.parseInt(args[1]);
                    }
                    break;
                case "-a":
                    if (args.length > 3)
                    {
                        break;
                    }
                    mode = 3;
                    if (args.length > 1)
                    {
                        ip = args[1];
                        if (args.length > 2)
                        {
                            portNumber = Integer.parseInt(args[2]);
                        }
                    }
                    break;
                case "-help":
                    mode = 4;
                    break;
            }
        }
        if (mode == 0)
        {
            System.out.println("Invalid command line arguments given. Type java Talk -help for more information");
        }
        else if (mode == 1)
        {
            if (!initHost(ip, portNumber))
            {
                System.out.println("Client unable to communicate with server.");
            }
        }
        else if (mode == 2)
        {
            if (!initServer(portNumber))
            {
                System.out.println("Server unable to listen on specified port.");
            }
        }
        else if (mode == 3)
        {
            if (!initHost(ip, portNumber))
            {
                System.out.println("Could not connect to server switching to server mode.");
                if (!initServer(portNumber))
                {
                    System.out.println("Server unable to listen on specified port.");
                }
            }
        }
        else
        {
            String helpMessage = "Author: Matteo Mantese\nThis program is a bidirectional messaging application that uses the socket API.\n" +
                    "If you want to run in server mode type: java Talk -s [portnumber]\n" +
                    "If you want to connect to a server type: java Talk -h [hostname|ipaddress] [portnumber].\n" +
                    "If you would like to run in automode type: java Talk -a [hostname|ipaddress] [portnumber]. When running in this mode we will try to connect to the server" +
                    " if it does not exist we will set you as the listening server.\n" +
                    "All arguments are optional if not used we will set the ip address as 127.0.0.1 (loopback address) and the port number to 12987.";
            System.out.println(helpMessage);
        }
    }
    public static boolean initHost(String ip, int portNumber)
    {
        try
        {
            String hostSendMessage;
            String hostReceiveMessage;
            System.out.println("Attempting to connect to " + ip + " on port " + portNumber);
            Socket host = new Socket(ip, portNumber);
            System.out.println("Successfully connected!");
            BufferedReader hostIn = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter hostOut = new PrintWriter(host.getOutputStream(), true);
            BufferedReader hostReceiver = new BufferedReader(new InputStreamReader(host.getInputStream()));
            while (true)
            {
                if (hostIn.ready())
                {
                    hostSendMessage = hostIn.readLine();
                    if (!hostSendMessage.equals("STATUS"))
                    {
                        hostOut.println(hostSendMessage);
                    }
                    else
                    {
                        InetAddress inet = InetAddress.getLocalHost();
                        String statusMsg = String.format("Local IP address: %s\nHost name: %s", inet.getHostAddress(), inet.getHostName());
                        System.out.println(statusMsg);
                    }
                }
                if (hostReceiver.ready())
                {
                    hostReceiveMessage = hostReceiver.readLine();
                    System.out.println("\r[REMOTE]" + hostReceiveMessage);
                }
            }
        }
        catch (IOException e)
        {
            return false;
        }
        catch (Exception ex)
        {
            return false;
        }
    }
    public static boolean initServer(int portNumber)
    {
        try
        {
            ServerSocket server = new ServerSocket(portNumber);
            System.out.println("Listening on port: " + portNumber);
            Socket connectedServer = server.accept();
            System.out.println("Connected");
            BufferedReader serverReceiver = new BufferedReader(new InputStreamReader(connectedServer.getInputStream()));
            BufferedReader serverIn = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter serverOut = new PrintWriter(connectedServer.getOutputStream(), true);

            String serverReceiveMessage;
            String serverSendMessage;
            while (true)
            {
                if (serverReceiver.ready())
                {
                    serverReceiveMessage = serverReceiver.readLine();
                    System.out.println("\r[REMOTE]" + serverReceiveMessage);
                }
                if (serverIn.ready())
                {
                    serverSendMessage = serverIn.readLine();
                    if (!serverSendMessage.equals("STATUS"))
                    {
                        serverOut.println(serverSendMessage);
                    }
                    else
                    {
                        InetAddress inet = InetAddress.getLocalHost();
                        String statusMsg = String.format("Local IP address: %s\nHost name: %s", inet.getHostAddress(), inet.getHostName());
                        System.out.println(statusMsg);
                    }
                }

            }
        }
        catch (IOException e)
        {
            return false;
        }
        catch (Exception ex)
        {
            return false;
        }
    }
}
