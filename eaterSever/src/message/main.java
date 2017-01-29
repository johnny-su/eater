package message;

public class main {
	 public static void main(String[] argvs){

	SocketServer server = new SocketServer(5555);
	server.beginListen();

	 }
}
