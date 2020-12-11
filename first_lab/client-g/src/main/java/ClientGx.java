import spos.lab1.demo.IntOps;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Client class
 */
public class ClientGx {
    private static final int port = 9000;
    private static SocketChannel socket;

    /**
     * Constructor
     */
    public ClientGx() {
        try {
            InetSocketAddress address = new InetSocketAddress("localhost", port);
            socket = SocketChannel.open(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void run() throws IOException, InterruptedException {

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while(socket.read(buffer) == 0) {}

        buffer.rewind();

        int variant = buffer.getInt();
        buffer.clear();

        int value = IntOps.funcG(variant);

        String result = value + " g(x)";
        buffer.put(result.getBytes());
        buffer.rewind();
        socket.write(buffer);
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        new ClientGx().run();

    }
}