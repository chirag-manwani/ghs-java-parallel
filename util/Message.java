package util;

import graph.Node;

public class Message {
    private Node sender;
    private MType type;
    private int level;
    private int fID;
    private int state;

    public Message() {
        sender = null;
        type = MType.NULL;
        level = -1;
        fID = -1;
        state = -1;
    }

    public Message(MType type, int level, int fID, int state, Node sender) {
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

    public int getState() {
        return state;
    }

    public Node getSender() {
        return sender;
    }
}