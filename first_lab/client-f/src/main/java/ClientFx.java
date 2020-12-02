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
    private int variant;

    /**
     * Constructor
     */
    public ClientFx(int variant) {
        try {
            this.variant = variant;
            InetSocketAddress address = new InetSocketAddress("localhost", port);
            socket = SocketChannel.open(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void run() throws IOException, InterruptedException {

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int value = IntOps.funcF(Integer.parseInt(String.valueOf(variant)));
        socket = SocketChannel.open(new InetSocketAddress("localhost", 9000));

        String result = value + " f(x)";
        buffer.put(result.getBytes());
        buffer.flip();
        socket.write(buffer);
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        int variant = Integer.parseInt(args[0]);
        new ClientFx(variant).run();
    }
}
