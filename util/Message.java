package util;

public class Message {
    private int type;
    private int level;
    private int fID;
    private int state;

    public Message() {
        type = -1;
        level = -1;
        fID = -1;
        state = -1;
    }

    public Message(int type, int level, int fID, int state) {
        this.type = type;
        this.level = level;
        this.fID = fID;
        this.state = state;
    }

    public int getType() {
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
}