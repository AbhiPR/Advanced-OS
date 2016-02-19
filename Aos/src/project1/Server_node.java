package project1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;

public class Server_node implements Runnable {
	private int id;
	private String[] all_nodes;
	private Queue<String> neighbour;
	private Socket soc;
	// private int distance = 0;
	private String parent = "";
	private ArrayList<String> child = new ArrayList<String>();

	// Client_node c;

	public Server_node(int id, String[] all_nodes, Queue<String> neighbour) {
		// this.soc=a;
		this.id = id;
		this.all_nodes = all_nodes;
		this.neighbour = neighbour;
	}

	public void outfile() throws FileNotFoundException, UnsupportedEncodingException {
		String filename = Node.config_file_path.split("\\.")[0];
		File f = new File(filename + "-" + id + ".out");
		PrintWriter writer = new PrintWriter(f, "UTF-8");
		if (parent.equals(""))
			writer.println("*");
		else
			writer.println(parent);
		if (child.isEmpty())
			writer.println("*");
		else
			for (String i : child)
				writer.print(i + " ");

		writer.close();
	}

	@Override
	public void run() {

		/*try {
			String h = InetAddress.getLocalHost().getHostName();
			System.out.println(h);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}*/
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String s;
		int count = 0;
		while (count != neighbour.size()) {
			try {
				s = in.readLine();
				if (s.split("\\s+")[0].equals("exp")&& Node.root!=id) {
					if (parent.equals("")) {
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
						for (String i : all_nodes) {
							if (i.split("\\s+")[0].equals(pt)) {
								String host = i.split("\\s+")[1];
								String port = i.split("\\s+")[2];
								Client_node c = new Client_node(neighbour, all_nodes, id);
								// c=new Client_node();
								c.connect(host, port, "nack " + id);
								break;
							}
						}

					}

				}
				/*
				 * else if(distance>Integer.parseInt(s.split("\\s+")[2])) {
				 * parent = s.split("\\s+")[1]; Node.setParent(parent);
				 * distance=Integer.parseInt(s.split("\\s+")[2]);
				 * System.out.println("ack"); } }
				 */
				else if (s.split("\\s+")[0].equals("ack")) {
					child.add(s.split("\\s+")[1]);
					count++;

				} else if (s.split("\\s+")[0].equals("nack")) {
					count++;
				}

				else if (s.equals("b"))
					break;
				else if (s.equals("d"))
					System.out
							.println("id: " + id + "\nall nodes: " + Arrays.toString(all_nodes).replaceAll("\\t+", " ")
									+ "\nneighbours: " + neighbour + "\nParent" + parent + "\nchild" + child);

			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		try {
			outfile();
			Node.root=-2;
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println("server exit");
		//Thread.currentThread().interrupt();
	//	return;

	}

}
