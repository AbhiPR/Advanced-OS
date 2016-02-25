package project1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.net.Socket;
import java.util.NoSuchElementException;

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
	public static int root = -1;
	private static volatile String parent = "";
	private static ArrayList<String> child = new ArrayList<String>();

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
			} catch (IOException e) {
				e.printStackTrace();
			}

	}

	private void runClient() throws UnknownHostException, IOException {
		find(config_file_path, identifier);
		while (true) {

			if (root == identifier || !getParent().equals("")) {
				neighbours.remove(getParent());
				if (neighbours.isEmpty())
					break;

				Client_node c = new Client_node(neighbours, all_nodes, identifier);

				long s = System.currentTimeMillis();
				while (System.currentTimeMillis() < s + 2000) {
				}

				c.exp();

				break;

			} else if (root == -2) {
				break;
			}
		}
		Thread.currentThread().interrupt();
		return;
	}

	public static void setParent(String p) {
		parent = p;
	}

	public static String getParent() {
		return parent;
	}

	public static void setChild(String p) {
		child.add(p);
	}

	public static ArrayList<String> getChild() {
		return child;
	}

	private void runServer() throws IOException {
		find(config_file_path, identifier);
		listen();

	}

	private void listen() throws IOException {

		server_socket = new ServerSocket(port);

		while (true) {
			Socket s = server_socket.accept();

			Server_node snode = new Server_node(s, this.identifier, this.all_nodes, this.neighbours);

			Thread t = new Thread(snode);
			t.start();

		}

	}

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
			try {
				nextLine = scan_path.nextLine();
			} catch (NoSuchElementException e) {
				break;
			}
		}

	}

}
