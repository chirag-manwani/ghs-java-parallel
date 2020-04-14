package graph;

import util.ChannelStatus;

public class Channel {
    private Node n;
    private int weight;
    private ChannelStatus status;

    public Channel() {
        n = null;
        weight = -1;
        status = ChannelStatus.NULL;
    }

    public Channel(Node n, int weight) {
        this.n = n;
        this.weight = weight;
        this.status = ChannelStatus.BASIC;
    }

    public Node getN() {
        return n;
    }

    public int getWeight() {
        return weight;
    }

    public ChannelStatus getStatus() {
        return status;
    }

    public void setStatus(ChannelStatus status) {
        this.status = status;
    }
}