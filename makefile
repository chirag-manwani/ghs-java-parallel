run: compile
	java -cp class/ Main ${ARGS}

compile: graph util main

graph: graph/Channel.java graph/Node.java
	javac -d class/ graph/Channel.java graph/Node.java

util: status util/Message.java
	javac -d class/ util/Message.java

status: util/MType.java util/ChannelStatus.java util/NodeStatus.java
	javac -d class/ util/MType.java util/ChannelStatus.java util/NodeStatus.java

main: Main.java
	javac -d class/ Main.java