package project1;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;

public class Client_node {

	private Queue<String> neighbour = new LinkedList<>();
	private String[] all_nodes;
	private int id;
	private Socket soc_Client = null;

	public Client_node(Queue<String> neighbour, String[] all_nodes, int id) {

		this.neighbour = neighbour;
		this.all_nodes = all_nodes;
		this.id = id;

	}

	public void exp() throws UnknownHostException, IOException {

		while (!neighbour.isEmpty()) {
			String a = neighbour.poll();
			for (String i : all_nodes) {
				if (i.split("\\s+")[0].equals(a)) {
					String host = i.split("\\s+")[1];
					String port = i.split("\\s+")[2];
					String k = "exp " + String.valueOf(id);
					connect(host, port, k);
				}
			}

		}
	}

	public void connect(String h, String p, String msg) throws UnknownHostException, IOException {

		int port = Integer.parseInt(p);
		soc_Client = new Socket(h, port);
		PrintWriter output = new PrintWriter(new OutputStreamWriter(soc_Client.getOutputStream()));
		output.print(msg + "\r\n");
		output.flush();
		soc_Client.close();

	}

}
