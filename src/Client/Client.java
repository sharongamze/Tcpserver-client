package Client;

import Matrix.Index;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Client {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Socket socket =new Socket("127.0.0.1",8010);
        System.out.println("client: Created Socket");

        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream toServer=new ObjectOutputStream(outputStream);
        ObjectInputStream fromServer=new ObjectInputStream(inputStream);

        int[][] Task_one = {
                {1, 0, 0},
                {1, 0, 1},
                {0, 1, 1}

        };

        toServer.writeObject("Task one");
        toServer.writeObject(Task_one);
        List<HashSet<Index>> group_of_ones = (List<HashSet<Index>>)fromServer.readObject();
        System.out.println("All groups with value of 1 on matrix are: ");
        System.out.println(group_of_ones);

        int[][] Task_two = {
                {1, 1, 0, 0, 0},
                {1, 1, 0, 0, 1},
                {1, 1, 0, 0, 1}
        };

        toServer.writeObject("Task two");
        toServer.writeObject(Task_two);
        Index source=new Index(0, 0);
        Index destination=new Index(2, 1);
        toServer.writeObject(source);
        toServer.writeObject(destination);
        List<List<Index>> shortest_paths = (List<List<Index>>) fromServer.readObject();
        System.out.println("Shortest paths from:" + source + " to:" +  destination + " are: ");
        for (List<Index> path : shortest_paths)
            System.out.println(path);


        int[][] Task_three = {
                {1, 1, 0, 1, 1},
                {0, 0, 0, 1, 1},
                {1, 1, 0, 1, 1}
        };

        toServer.writeObject("Task three");
        toServer.writeObject(Task_three);
        int validSubmarine = (int) fromServer.readObject();
        List<List<Index>> submarines= (List<List<Index>>) fromServer.readObject();
        System.out.println("Num of submarines: " + validSubmarine);
        System.out.println("The submarines: " + submarines);

        int[][] Task_four = {
                {100,100,100},
                {500,900,300}
        };

        toServer.writeObject("Task four");
        toServer.writeObject(Task_four);
        Index source_task4=new Index(1, 0);
        Index destination_task4=new Index(1, 2);
        toServer.writeObject(source_task4);
        toServer.writeObject(destination_task4);
        HashMap<List<Index>,Integer> min_cost= (HashMap<List<Index>,Integer>) fromServer.readObject();
        System.out.println("All minimum cost paths from:" + source_task4 + " to:" +  destination_task4 + " are: ");
        min_cost.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + " " + "value " + entry.getValue());
        });

        toServer.writeObject("stop server");
        System.out.println("\nClient: Close all streams");
        fromServer.close();
        toServer.close();
        socket.close();
        System.out.println("Client: Closed operational socket");

    }
}
