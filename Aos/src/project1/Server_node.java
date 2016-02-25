package project1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Queue;

public class Server_node implements Runnable {
	private int id;
	private String[] all_nodes;
	private static volatile Queue<String> neighbour;
	private Socket soc_server;
	private String parent = "";
	private ArrayList<String> child = new ArrayList<String>();
	private static volatile int count = 0;

	public Server_node(Socket a, int id, String[] all_nodes, Queue<String> neighbour) {

		this.soc_server = a;
		this.id = id;
		this.all_nodes = all_nodes;
		this.neighbour = neighbour;
	}

	public void outfile() throws FileNotFoundException, UnsupportedEncodingException {

		int s = Node.config_file_path.lastIndexOf('/');
		int d = Node.config_file_path.lastIndexOf('.');
		String filename = Node.config_file_path.substring(s + 1, d);

		System.out.println(filename);
		File f = new File(filename + "-" + id + ".out");
		PrintWriter writer = new PrintWriter(f, "UTF-8");
		String p = Node.getParent();
		ArrayList<String> c = Node.getChild();
		if(!c.isEmpty())
			Collections.sort(c);
			
		if (p.equals(""))
			writer.println("*");
		else
			writer.println(p);
		if (c.isEmpty())
			writer.println("*");
		else
			for (String i : c)
				writer.print(i + " ");

		writer.close();
	}

	@Override
	public void run() {

		BufferedReader br;
		String k;

		String s;

		try {
			br = new BufferedReader(new InputStreamReader(soc_server.getInputStream()));

			while (count != neighbour.size()) {
				s = "";
				while ((k = br.readLine()) != null) {
					s = k;
				}

				if (s.split("\\s+")[0].equals("exp") && Node.root != id) {

					if (Node.getParent().equals("")) {

						parent = s.split("\\s+")[1];
						Node.setParent(parent);
						neighbour.remove(parent);
						for (String i : all_nodes) {
							if (i.split("\\s+")[0].equals(parent)) {
								String host = i.split("\\s+")[1];
								String port = i.split("\\s+")[2];
								Client_node c = new Client_node(neighbour, all_nodes, id);
								c.connect(host, port, "ack " + id);
								break;
							}
						}

					} else {

						String pt = s.split("\\s+")[1];
						neighbour.remove(pt);
						for (String i : all_nodes) {
							if (i.split("\\s+")[0].equals(pt)) {
								String host = i.split("\\s+")[1];
								String port = i.split("\\s+")[2];
								Client_node c = new Client_node(neighbour, all_nodes, id);
								c.connect(host, port, "nack " + id);
								break;
							}
						}

					}

				} else if (s.split("\\s+")[0].equals("ack")) {
					Node.setChild(s.split("\\s+")[1]);
					count++;

				} else if (s.split("\\s+")[0].equals("nack")) {
					count++;

				}
			}
		} catch (IOException e) {

			e.printStackTrace();
		}

		try {

			outfile();
			Node.root = -2;
			soc_server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;

	}

}
