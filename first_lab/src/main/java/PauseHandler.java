
import java.util.Scanner;

public class PauseHandler {
    private static final int WAIT_TIME = 10000;

    public static void stop() {

        System.out.println("The user cancelled the calculation of function");
        for (Integer key: Server.getResults().keySet()) {
            if (Server.getResults().get(key).equals("g(x)") && Server.getResults().size() == 1) {
                System.out.println("The user cancelled the calculation because function F hangs");
            }
            else if (Server.getResults().get(key).equals("f(x)") && Server.getResults().size() == 1) {
                System.out.println("The user cancelled the calculation because function G hangs");
            }
            System.out.println("Result of function " + Server.getResults().get(key) + " : " + key);
        }
        Server.close();
        System.exit(0);
    }

    public static void startPrompt() {
        long promptStartTime = System.currentTimeMillis();
        new Thread(() -> {
            Thread threadTime = new Thread(() -> {
                while(System.currentTimeMillis() - promptStartTime < WAIT_TIME);
                if (!Thread.currentThread().isInterrupted()) {
                    stop();
                }
            });
            threadTime.start();
            boolean check = true;
            while (check) {
                System.out.println("Cancellation Prompt:");
                System.out.println("1 stop the program");
                System.out.println("2 continue");
                System.out.println("System will shut down automatically in 15 seconds");
                Scanner scanner = new Scanner(System.in);

                String userInput = scanner.nextLine();
                if (userInput.equals("1") || userInput.equals("stop")) {
                    check = false;
                    stop();
                    return;
                } else if (userInput.equals("2") || userInput.equals("continue")) {
                    threadTime.interrupt();
                    return;
                } else {
                    System.out.println("You entered wrong action");
                }
            }
        }).start();

    }
}