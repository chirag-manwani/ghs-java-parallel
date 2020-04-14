package util;

import graph.Node;

public class Message {
    private Node sender;
    private MType type;
    private int level;
    private int fID;
    private int w;
    private NodeStatus state;

    public Message() {
        this(MType.NULL, -1, -1, NodeStatus.NULL, null);
    }

    public Message(MType type, Node sender) {
        this(type, -1, -1, NodeStatus.NULL, sender);
    }

    public Message(MType type, int levelOrWeight, Node sender) {
        this.type = type;
        this.sender = sender;
        if (type == MType.CONNECT) {
            this.level = levelOrWeight;
        }
        else {
            this.w = levelOrWeight;
        }
    }

    public Message(MType type, int level, int fID, Node sender) {
        this(type, level, fID, NodeStatus.NULL, sender);
    }

    public Message(MType type, int level, int fID, NodeStatus state, Node sender) {
        this.type = type;
        this.level = level;
        this.fID = fID;
        this.state = state;
        this.sender = sender;
    }

    public MType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public int getfID() {
        return fID;
    }

    public NodeStatus getState() {
        return state;
    }

    public Node getSender() {
        return sender;
    }

    public int getWeight() {
        return w;
    }
}