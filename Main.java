import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import graph.Channel;
import graph.Node;

public class Main {
    public static void main(String args[]) {
        String fileName = args[0];

        BufferedReader br = null;
        ArrayList<Node> nodes = new ArrayList<>();

        try {
            br = new BufferedReader(new FileReader(fileName));
            int numNodes = Integer.parseInt(br.readLine().strip());

            for (int nodeID = 0; nodeID < numNodes; nodeID++) {
                Node node = new Node(nodeID);
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
    }
}