package message;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.*;

public class test {
	public static void main(String[] argvs){

	try {
		//System.out.println(InetAddress.getLocalHost);
		Socket socket = new Socket("183.174.67.183",9999);
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}