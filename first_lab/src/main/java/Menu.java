
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Menu {
    public void menu() {
       while (true){
            int select;
            Scanner selector = new Scanner(System.in);

            System.out.println("Testing: ");
            System.out.println("0. f finishes before g with non value");
            System.out.println("1. g finishes before f with non value");
            System.out.println("2. f finishes zero  g hangs");
            System.out.println("3. g finishes zero f hangs");
            System.out.println("4. f finishes non zero value g hangs");
            System.out.println("5. g finishes non zero value f hangs");

            select = selector.nextInt();

            if (select == -1){
                System.exit(-1);
            }
            else if(select>=0 && select<6){
                Server mainServer =  new Server("localhost", 9000, select,2);

                try {
                    mainServer.run();
                } catch (IOException e) { }

                if(Server.getResults().size()==2||Server.getResults().containsKey(0)) {
                    System.out.println("");
                    System.out.println("RESULT OF MULTIPLY: " + mainServer.getMultiplication() + "\n\n");
                    Server.getResults().clear();
                }
                Server.close();
            }else {
                System.out.println("Try again");
           }
        }
    }
}

