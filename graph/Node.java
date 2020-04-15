package graph;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.concurrent.LinkedBlockingQueue;

import util.ChannelStatus;
import util.MType;
import util.Message;
import util.NodeState;

public class Node extends Thread {
    private NodeState state;

    private int fID;
    private int level;
    private int bestWeight;
    private int recP;

    private ArrayList<Channel> channels;

    private Node testCh;
    private Node bestCh;
    private Node parent;

    private LinkedBlockingQueue<Message> q;

    private boolean stop;

    private SortedSet<Integer> s;

    public int sMessages = 0;

    /*
        Node Constructor Functions
    */

    public Node() {
        this(-1, null);
    }

    public Node(int fID, SortedSet<Integer> s) {
        this.fID = fID;
        this.s = s;
        state = NodeState.SLEEP;
        level = 0;
        bestWeight = -1;
        channels = new ArrayList<>();
        testCh = null;
        bestCh = null;
        parent = null;
        recP = 0;
        q = new LinkedBlockingQueue<>();
        stop = false;
    }

    /*
        Helper Functions
    */

    private void addToSet(Channel c) {
        synchronized(s) {
            s.add(c.getWeight());
        }
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Neighours Node:" + fID + "\n-----------------\n");
        buffer.append("NN\tweight\n");
        for(Channel c : channels) {
            buffer.append(c.getNode().fID + "\t" + c.getWeight() + "\n");
        }
        return buffer.toString();
    }

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

    private Channel getBestChannel() {
        Channel bestChannel = null;
        int bestWeight = Integer.MAX_VALUE;

        for(Channel c : channels) {
            int cWeight = c.getWeight();

            if(c.getStatus() == ChannelStatus.BASIC && cWeight < bestWeight) {
                bestWeight = cWeight;
                bestChannel = c;
            }
        }

        return bestChannel;
    }

    private Channel getSenderChannel(Node sender) {
        for (Channel c : channels) {
            if(sender == c.getNode()) {
                return c;
            }
        }
        return null;
    }

    private void processMessage(Message m) {
        MType mType = m.getType();
        switch(mType){
            case WAKEUP:
                wakeUp();
                break;
            case CONNECT:
                connect(m);
                break;
            case INITITATE:
                initiate(m);
                break;
            case TEST:
                test(m);
                break;
            case ACCEPT:
                accept(m);
                break;
            case REJECT:
                reject(m);
                break;
            case REPORT:
                report(m);
                break;
            case CHANGEROOT:
                changeroot();
                break;
            case STOP:
                finish();
                stop = true;
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
        while (!stop) {
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
    private void finish() {
        for(Channel c : channels) {
            if (c.getStatus() == ChannelStatus.BRANCH) {
                Message stopM = new Message(MType.STOP, this);
                c.getNode().addMessage(stopM);
            }
        }
    }

    private void wakeUp() {
        Channel bestChannel = getBestChannel();
        bestChannel.setStatus(ChannelStatus.BRANCH);

        addToSet(bestChannel);

        this.level = 0;
        this.state = NodeState.FOUND;
        this.recP = 0;

        Message m = new Message(MType.CONNECT, this.level, this);
        Node recipient = bestChannel.getNode();
        recipient.addMessage(m);
        sMessages++;
    }

    private void connect(Message m) {
        if (this.state == NodeState.SLEEP) {
            wakeUp();
        }

        int L = m.getLevel();
        Node sender = m.getSender();
        Channel sChannel = getSenderChannel(sender);
        if (L < this.level) {
            sChannel.setStatus(ChannelStatus.BRANCH);
            addToSet(sChannel);
            Message initM = new Message(MType.INITITATE, this.level, this.fID, this.state, this);
            sender.addMessage(initM);
            sMessages++;
        }
        else if (sChannel.getStatus() == ChannelStatus.BASIC) {
            addMessage(m);
        }
        else {
            Message initM = new Message(MType.INITITATE, this.level + 1, sChannel.getWeight(), NodeState.FIND, this);
            sender.addMessage(initM);
            sMessages++;
        }
    }

    private void initiate(Message m) {
        level = m.getLevel();
        fID = m.getfID();
        state = m.getState();
        parent = m.getSender();
        bestCh = null;
        bestWeight = Integer.MAX_VALUE;

        for (Channel c : channels) {
            if (c.getStatus() == ChannelStatus.BRANCH && c.getNode() != parent) {
                Message initM = new Message(MType.INITITATE, level, fID, state, this);
                c.getNode().addMessage(initM);
                sMessages++;
            }
        }
        if (state == NodeState.FIND) {
            recP = 0;
            test();
        }
    }

    private void test(Message m) {
        Node sender = m.getSender();

        if (state == NodeState.SLEEP) {
            wakeUp();
        }

        if (m.getLevel() > level) {
            addMessage(m);
        }
        else if (m.getfID() != fID) {
            Message accM = new Message(MType.ACCEPT, this);
            sender.addMessage(accM);
            sMessages++;
        }
        else {
            Channel sChannel = getSenderChannel(sender);
            if (sChannel.getStatus() == ChannelStatus.BASIC) {
                sChannel.setStatus(ChannelStatus.REJECT);
            }
            if (sender != testCh) {
                Message rejM = new Message(MType.REJECT, this);
                sender.addMessage(rejM);
                sMessages++;
            }
            else {
                test();
            }
        }
    }

    private void test() {
        Channel bestChannel = getBestChannel();
        if (bestChannel == null) {
            testCh = null;
            report();
        }
        else {
            testCh = bestChannel.getNode();
            Message m = new Message(MType.TEST, level, fID, this);
            testCh.addMessage(m);
            sMessages++;
        }
    }

    private void accept(Message m) {
        testCh = null;
        Node sender = m.getSender();
        int weight = getSenderChannel(sender).getWeight();
        if (weight < bestWeight) {
            bestWeight = weight;
            bestCh = sender;
        }
        report();
    }

    private void reject(Message m) {
        Channel sChannel = getSenderChannel(m.getSender());
        if (sChannel.getStatus() == ChannelStatus.BASIC) {
            sChannel.setStatus(ChannelStatus.REJECT);
        }
        test();
    }

    private void report(Message m) {
        Node sender = m.getSender();
        int weight = m.getWeight();

        if (sender != parent) {
            if (weight< bestWeight) {
                bestWeight = weight;
                bestCh = sender;
            }
            recP++;
            report();
        }
        else {
            if (state == NodeState.FIND) {
                addMessage(m);
            }
            else if (weight > bestWeight){
                changeroot();
            }
            else if(weight == bestWeight && bestWeight == Integer.MAX_VALUE) {
                finish();
                stop = true;
            }
        }
    }

    private void report() {
        int branchCount = 0;
        for(Channel c : channels) {
            Node n = c.getNode();
            if ((n != parent) && (c.getStatus() == ChannelStatus.BRANCH)) {
                branchCount++;
            }
        }
        if (recP == branchCount && testCh == null) {
            state = NodeState.FOUND;
            Message m = new Message(MType.REPORT, bestWeight, this);
            parent.addMessage(m);
            sMessages++;
        }
    }

    private void changeroot() {
        Channel bChannel = getSenderChannel(bestCh);
        if (bChannel.getStatus() == ChannelStatus.BRANCH) {
            Message m = new Message(MType.CHANGEROOT, this);
            bestCh.addMessage(m);
        }
        else {
            Message m = new Message(MType.CONNECT, level, this);
            bestCh.addMessage(m);
            bChannel.setStatus(ChannelStatus.BRANCH);
            addToSet(bChannel);
        }
        sMessages++;
    }
}