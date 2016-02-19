package project1;

import java.util.LinkedList;
import java.util.Queue;

public class Client_node {

	private Queue<String> neighbour = new LinkedList<>();
	private String[] all_nodes;
	private int id;

	public Client_node(Queue<String> neighbour, String[] all_nodes,int id) {
		this.neighbour = neighbour;
		this.all_nodes = all_nodes;
		this.id=id;
		
	}

	public void exp() {// System.out.println("clinsidefn");

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
	
	public void connect(String h, String p,String msg)
	{
		System.out.println("host: "+h+" port: "+p+" msg: "+msg);
		
	}
	
	
	
	
	
	
}
