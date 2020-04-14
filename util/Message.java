package util;

import graph.Node;

public class Message {
    private Node sender;
    private MType type;
    private int level;
    private int fID;
    private int w;
    private NodeState state;

    public Message() {
        this(MType.NULL, -1, -1, NodeState.NULL, null);
    }

    public Message(MType type, Node sender) {
        this(type, -1, -1, NodeState.NULL, sender);
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
        this(type, level, fID, NodeState.NULL, sender);
    }

    public Message(MType type, int level, int fID, NodeState state, Node sender) {
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

    public NodeState getState() {
        return state;
    }

    public Node getSender() {
        return sender;
    }

    public int getWeight() {
        return w;
    }
}