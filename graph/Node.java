package graph;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import util.MType;
import util.Message;
import util.NodeStatus;

public class Node extends Thread {
    private NodeStatus status;
    private int nodeID;
    private int level;
    private int bestWeight;

    private ArrayList<Channel> channels;

    private Channel testCh;
    private Channel bestCh;
    private Channel parent;

    private int recP;
    private LinkedBlockingQueue<Message> q;


    /*
        Node Constructor Functions
    */

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
        q = null;
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
        q = new LinkedBlockingQueue<>();
    }

    /*
        Helper Functions
    */

    public void addChannel(Channel c) {
        if (c != null) {
            channels.add(c);
        }
    }

    public void addMessage(Message m) {
        try {
            q.put(m);
        } catch (InterruptedException e) {
            System.out.println("Node interrupted while adding message");
        }
    }

    private void processMessage(Message m) {
        MType mType = m.getType();
        switch(mType){
            case WAKEUP:
                System.out.println("Wakeup Message Received");
                break;
            case CONNECT:
                System.out.println("Connect Message Received");
                break;
            case INITITATE:
                System.out.println("Initiate Message Received");
                break;
            case TEST:
                System.out.println("Test Message Received");
                break;
            case ACCEPT:
                System.out.println("Accept Message Received");
                break;
            case REJECT:
                System.out.println("Reject Message Received");
                break;
            case REPORT:
                System.out.println("Report Message Received");
                break;
            case CHANGEROOT:
                System.out.println("Changeroot Message Received");
                break;
            default:
                System.out.println("Improper message code");
        }

    }

    /*
        Thread run function
    */
    @Override
    public void run() {
        while (true) {
            try {
                Message m = q.take();
                processMessage(m);
            } catch (InterruptedException e) {
                System.out.println();
            }
        }
    }

    /*
        GHS Functions
    */
    public void wakeUp() {

    }

    public void connect(Message m) {

    }

    public void initiate(Message m) {

    }

    public void test(Message m) {

    }

    public void test() {

    }

    public void accept(Message m) {

    }

    public void reject(Message m) {

    }

    public void report(Message m) {

    }

    public void report() {

    }

    public void changeroot(Message m) {

    }

    public void changeroot() {

    }
}