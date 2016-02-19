package project1;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;

public class Client_node {

	private Queue<String> neighbour = new LinkedList<>();
	private String[] all_nodes;
	private int id;
	private Socket soc_Client=null;

	public Client_node(Queue<String> neighbour, String[] all_nodes,int id) {
		this.neighbour = neighbour;
		this.all_nodes = all_nodes;
		this.id=id;
		
	}

	public void exp() throws UnknownHostException, IOException {// System.out.println("clinsidefn");

		while (!neighbour.isEmpty()) {// System.out.println("clinsidefnwh");
			String a = neighbour.poll();
			for (String i : all_nodes) {
				if (i.split("\\s+")[0].equals(a)) {
					String host = i.split("\\s+")[1];
					String port = i.split("\\s+")[2];
					connect(host,port,"exp "+String.valueOf(id));
				}
			}

		}
	}
	
	public void connect(String h, String p,String msg) throws UnknownHostException, IOException
	{
	//	System.out.println("host: "+h+" port: "+p+" msg: "+msg);
		int port=Integer.parseInt(p.trim());
		soc_Client=new Socket(h,port);
		BufferedWriter bufOut = new BufferedWriter( new OutputStreamWriter(soc_Client.getOutputStream()));
		bufOut.write("msg");
		bufOut.newLine();
		bufOut.flush();
		
	}
	
	
	
	
	
	
}
