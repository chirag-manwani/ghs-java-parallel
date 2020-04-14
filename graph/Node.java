package graph;

import java.util.ArrayList;

import util.NodeStatus;

public class Node implements Runnable {
    private NodeStatus status;
    private int nodeID;
    private int level;
    private int bestWeight;

    private ArrayList<Channel> channels;

    private Channel testCh;
    private Channel bestCh;
    private Channel parent;

    private int recP;

    public Node() {
        status = NodeStatus.NULL;
        nodeID = -1;
        level = -1;
        bestWeight = -1;
        channels = null;
        testCh = null;
        bestCh = null;
        parent = null;
        recP = -1;
    }

    public Node(int nodeID) {
        this.nodeID = nodeID;
        status = NodeStatus.SLEEP;
        level = 0;
        bestWeight = -1;
        channels = new ArrayList<>();
        testCh = null;
        bestCh = null;
        parent = null;
        recP = 0;
    }

    public void addChannel(Channel c) {
        if(c != null) {
            channels.add(c);
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

    }
}