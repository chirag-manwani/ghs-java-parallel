import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import graph.Channel;
import graph.Node;
import util.MType;
import util.Message;

public class Main {
    public static void main(String args[]) {
        String fileName = args[0];

        BufferedReader br = null;
        ArrayList<Node> nodes = new ArrayList<>();

        HashMap<Integer, Edge> m = new HashMap<>();
        SortedSet<Integer> s = new TreeSet<>();

        try {
            br = new BufferedReader(new FileReader(fileName));
            int numNodes = Integer.parseInt(br.readLine().strip());

            for (int nodeID = 0; nodeID < numNodes; nodeID++) {
                // Node node = new Node(Integer.MAX_VALUE);
                Node node = new Node(nodeID, s);
                nodes.add(node);
            }

            String line = null;
            while ((line = br.readLine()) != null) {
                line = line.strip().substring(1, line.length() - 1);
                String parts[] = line.split(",");

                int startNode = Integer.parseInt(parts[0].strip());
                int endNode = Integer.parseInt(parts[1].strip());
                int weight = Integer.parseInt(parts[2].strip());

                Channel c1 = new Channel(nodes.get(endNode), weight);
                Channel c2 = new Channel(nodes.get(startNode), weight);

                nodes.get(startNode).addChannel(c1);
                nodes.get(endNode).addChannel(c2);

                Edge edge = new Edge(startNode, endNode, weight);
                m.put(weight, edge);
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found. Quitting.");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("IOException occured. Quitting");
            System.exit(2);
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                System.out.println("Error in Closing stream");
                System.exit(3);
            }
        }

        // Graph created successfully
        for (Node n : nodes) {
            n.start();
        }

        nodes.get(0).addMessage(new Message(MType.WAKEUP, null));

        for (Node n : nodes) {
            try {
                n.join();
            } catch (InterruptedException e) {
                System.out.println("Error in joining");
                System.exit(4);
            }
        }

        for(Integer i : s) {
            System.out.println(m.get(i));
        }
        int totalMessages = 0;
        for(Node n : nodes) {
            totalMessages += n.sMessages;
        }
        System.out.println(totalMessages);
    }
}


class Edge {
    int weight;
    int start;
    int end;

    public Edge(int start, int end, int weight) {
        this.weight = weight;
        this.start = start;
        this.end = end;
    }

    public String toString() {
        return "(" + start + ", " + end + ", " + weight + ")";
    }
}
