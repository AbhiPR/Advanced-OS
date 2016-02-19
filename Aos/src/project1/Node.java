package project1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Node implements Runnable {

	private ServerSocket server_socket = null;
	private int identifier;
	private int port;
	private String hostname;
	private String[] all_nodes;
	private int number_of_nodes;
	private Queue<String> neighbours = new LinkedList<>();
	public static String config_file_path;
	private String[] info;
	private int type;
	private Queue<String> near_node = new LinkedList<>();
	public static int root = -1;
	private int t;
	private static volatile String parent = "";
	private ArrayList<String> child = new ArrayList<String>();

	public Node(int type, int id, String config_path) {
		this.type = type;
		this.identifier = id;
		config_file_path = config_path;
	}

	public void run() {

		if (type == 1)
			try {
				runServer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		else
			try {
				runClient();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

	}

	private void runClient() throws FileNotFoundException {
		find(config_file_path, identifier);
		while (true) {

			if (root == identifier || !getParent().equals("")) {
				// display();
				
				neighbours.remove(getParent());
				Client_node c = new Client_node(neighbours, all_nodes, identifier);
				c.exp();
				// System.out.println(neighbours+" "+ all_nodes);
				break;

			}
			else if(root==-2)
			{break;}
		}

	}

	public static void setParent(String p) {
		parent = p;
	}

	public static String getParent() {
		return parent;
	}
	

	private void runServer() throws IOException {
		find(config_file_path, identifier);
		// explore();
		listen();
		// display();
		//System.out.println("server:");

	}

	private void listen() throws IOException {
	     server_socket=new ServerSocket(port);
		 while(true)
		{
			Server_node snode = new Server_node(server_socket.accept(),this.identifier, this.all_nodes, this.neighbours);
			Thread t = new Thread(snode);
			t.start();

		}

	}
	/*
	 * private void explore() { for (String j : neighbours) { for (String k :
	 * all_nodes) {
	 * 
	 * if (j.equals(k.split("\\s+")[0])) { String a = k.split("\\s+")[0];
	 * near_node.add(k.replace(a, "").trim()); continue; }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }
	 */

	// finds neighbours, host, port of the current node, info about other nodes
	private void find(String path, int id) throws FileNotFoundException {

		Scanner scan_path = new Scanner(new File(path));
		identifier = id;
		String nextLine = scan_path.nextLine().trim();
		while (nextLine.equals("") || nextLine.charAt(0) == '#') {
			nextLine = scan_path.nextLine().trim();
		}
		if (nextLine.contains("#")) {
			number_of_nodes = Integer.parseInt(nextLine.split("#")[0].substring(0, 1).trim().split("\\s+")[0]);
		} else {
			number_of_nodes = Integer.parseInt(nextLine.trim().split("\\s+")[0]);
			root = Integer.parseInt(nextLine.trim().split("\\s+")[1]);

		}
		this.all_nodes = new String[number_of_nodes];
		nextLine = scan_path.nextLine().trim();
		while (nextLine.equals("") || nextLine.charAt(0) == '#') {
			nextLine = scan_path.nextLine().trim();
		}
		for (int i = 0; i < number_of_nodes; i++) {
			if (nextLine.contains("#")) {
				this.all_nodes[i] = nextLine.split("#")[0].trim();
			} else {
				this.all_nodes[i] = nextLine.trim();

			}

			if (identifier == i) {
				this.info = all_nodes[i].split("\\s+");
				// System.out.println("info :" +Arrays.toString(info));
				hostname = info[1];
				port = Integer.parseInt(info[2]);

			}

			nextLine = scan_path.nextLine();
		}

		while (nextLine.equals("") || nextLine.charAt(0) == '#') {
			nextLine = scan_path.nextLine();
		}
		String circuit;
		for (int i = 0; i < number_of_nodes; i++) {
			if (nextLine.contains("#")) {
				circuit = nextLine.split("#")[0].trim();
			} else {
				circuit = nextLine.replaceAll("\\s+", " ").trim();
			}

			if (i == identifier) {
				String nodes[] = circuit.split(" ");
				for (String j : nodes)
					this.neighbours.add(j);
			}
			nextLine = scan_path.nextLine();
		}

	}

	public void display() {
		System.out.println("id: " + (identifier) + "\nNumber of node: " + number_of_nodes + "\nall nodes: "
				+ Arrays.toString(all_nodes).replaceAll("\\t+", " ") + "\nnode_info" + Arrays.toString(info)
				+ "\nhost & port :" + hostname + " " + port + "\nneighbours: " + neighbours);
		System.out.println("Near nodes info: " + near_node.toString());
	}
}
