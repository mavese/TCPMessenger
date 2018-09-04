import java.net.*;
import java.io.*;



public class Talk
{
    public static void main(String[] args)
    {
        int mode = 0;
        String ip = "127.0.0.1";
        int portNumber = 2048;

        if (args.length > 0)
        {
            switch (args[0])
            {
                case "-h":
                    mode = 1;
                    break;
                case "-s":
                    mode = 2;
                    break;
                case "-a":
                    mode = 3;
                    break;
                case "-help":
                    mode = 4;
                    break;
            }
        }
        if (mode == 1)
        {
            try
            {
                String hostSendMessage;
                String hostReceiveMessage;
                Socket host = new Socket(ip, portNumber);
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
                            System.out.println(Inet4Address.getLocalHost().getAddress());
                        }
                    }
                    if (hostReceiver.ready())
                    {
                        hostReceiveMessage = hostReceiver.readLine();
                        System.out.println("[REMOTE]" + hostReceiveMessage);
                    }
                }
            }
            catch (IOException e)
            {
                System.out.println(e.getMessage());
            }
        }
        if (mode == 2)
        {
            try
            {
                ServerSocket server = new ServerSocket(portNumber);
                Socket connectedServer = server.accept();
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
                        System.out.println("[REMOTE]" + serverReceiveMessage);
                    }
                    if (serverIn.ready())
                    {
                        serverSendMessage = serverIn.readLine();
                        serverOut.println(serverSendMessage);
                    }

                }
            }
            catch (IOException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
}
