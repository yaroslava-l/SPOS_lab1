
import spos.lab1.demo.IntOps;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Client class
 */
public class ClientFx {
    private static final int port = 2020;
    private static SocketChannel socket;

    /**
     * Constructor
     */
    public ClientFx() {
        try {
            InetSocketAddress address = new InetSocketAddress("localhost", port);
            socket = SocketChannel.open(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void run() throws IOException, InterruptedException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        socket = SocketChannel.open(new InetSocketAddress("localhost", 9000));
        int readLenth = socket.read(buffer);
        buffer.flip();
        byte[] bytes = new byte[readLenth];
        buffer.get(bytes);
        String result = new String(bytes);

        buffer.clear();
        socket.close();
        int value =  IntOps.funcF(Integer.parseInt(result));
        socket = SocketChannel.open(new InetSocketAddress("localhost", 9000));

        result = value + " f(x)";
        buffer.put(result.getBytes());
        buffer.flip();
        socket.write(buffer);
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        new ClientFx().run();
    }
}
