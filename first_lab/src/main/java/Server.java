
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.Key;
import java.util.*;

public class Server {
    private static ServerSocketChannel socketServer;
    private InetSocketAddress address;//address
    private final int variant;
    private boolean calculateEnable = true;
    private int maxConnections;
    private static final HashMap<Integer, String> results = new HashMap<>();
    private static ArrayList<Process> clientProcesses = new ArrayList<>();


    public Server(String host, int port, int variant, int maxConnections) {
        this.address = new InetSocketAddress(host, port);
        this.variant = variant;
        this.maxConnections = maxConnections;
    }

    public void run() throws IOException {
        new Thread( ( ) -> {
            KeyboardHandler keyboardHandler = new KeyboardHandler();
            keyboardHandler.start();
        } ).start();
        try {
            socketServer = ServerSocketChannel.open();
            socketServer.configureBlocking(false);
            socketServer.socket().bind(address);

            System.out.println("Server started!");

            //starting clients
            startClients(this.variant);

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (calculateEnable) {

                //blocking wait for events
                SocketChannel channel = socketServer.accept(); // getting channel
                if (channel != null) {

                    //server operations (writing and reading from socket)
                    buffer.clear();
                    buffer.put(String.valueOf(variant).getBytes());
                    buffer.flip();
                    channel.write(buffer);
                    buffer.clear();
                    int size = channel.read(buffer);
                    if (size == -1) {
                        channel.close();
                        continue;
                    }
                    byte[] data = new byte[size];
                    buffer.flip();
                    buffer.get(data);
                    buffer.clear();
                    String gotData = new String(data);
                    // handle(channel);
                    System.out.println("Got:" + gotData);
                    analyzeResult(gotData);

                    //checking connections
                    if (this.maxConnections == 0) {
                        this.calculateEnable = false;
                    }
                }

            }

        } catch (IOException | InterruptedException e) {
            close();
        }
    }

    /**
     * The function of starting processes
     *
     */
    private void startProcess(String s) throws InterruptedException{
        try{
            ProcessBuilder builder=null;
            if(s.equals("f"))
                builder=new ProcessBuilder("java", "-jar", "D:\\3курс\\SPOS\\operating_system-master\\first_lab\\out\\artifacts\\first_lab_client_f_jar\\first_lab.client-f.jar");
            else if(s.equals("g"))
                builder=new ProcessBuilder("java", "-jar", "D:\\3курс\\SPOS\\operating_system-master\\first_lab\\out\\artifacts\\first_lab_client_g_jar\\first_lab.client-g.jar");

            Process process = builder.start();
            System.out.println(process.isAlive());
            clientProcesses.add(process);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * The function of working with clients
     *
     *
     */
    private void startClients(int variant) throws IOException, InterruptedException {
        //connect and run our server-clients
        startProcess("f");
        startProcess("g");
    }

    /**
     * Function of sending message to the client
     *
     * @param socket
     */
    private void sendMessage(SocketChannel socket){
        try{
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            String s = String.valueOf(this.variant);
            byteBuffer.put(s.getBytes());
            byteBuffer.flip();
            socket.write(byteBuffer);
            byteBuffer.clear();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Function of working with the socket
     *
     * @param socket
     */
    private void handle(SocketChannel socket) throws IOException {
        try {
            sendMessage(socket);

            read(socket);

        } catch (IOException e) {
            close();
        }
    }

    /**
     * Function that reading message from client
     *
     * @throws IOException
     */
    private void read(SocketChannel socket) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int numRead = socket.read(byteBuffer);
        if (numRead == -1) {
            socket.close();
            return;
        }
        //creating byte array for message
        byte[] data = new byte[numRead];
        byteBuffer.flip();
        byteBuffer.get(data);
        byteBuffer.clear();
        String gotData = new String(data);
        System.out.println("Got:" + gotData);
        analyzeResult(gotData);
    }

    /**
     * Function that analyzing result from client
     *
     * @param result
     */
    private void analyzeResult(String result) {
        String[] args = result.split(" ");
        if (args.length < 2)
            return;
        int value = Integer.parseInt(args[0]);

        if (value == 0) {
            this.calculateEnable = false;
            System.out.println("Another client's calculations were canceled");
        }

        String name = args[1];

        this.results.put(value, name);
        maxConnections--;

    }

    /**
     * Getter for hashmap (my results)
     *
     * @return
     */
    public static HashMap<Integer, String> getResults() {
        return results;
    }

    /**
     * Function that calculate result
     *
     * @return
     */
    public int getMultiplication() {
        int result = 1;
        Set<Integer> keys = results.keySet();
        for (int s : keys) {
            if (s == 0) return 0;
            else result = s * result;
        }
        return result;

    }

    /**
     * Function that closing server
     */
    public static void close()  {
        try{
            for (Process proc : clientProcesses) {
                proc.destroy();
            }
            socketServer.close();
            results.clear();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
