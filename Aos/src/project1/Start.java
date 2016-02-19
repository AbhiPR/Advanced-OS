package project1;

import java.util.Arrays;

public class Start implements Runnable{
	
	private int id;
    private String config_path;
    
    public Start(int id, String config_path) {
        this.id = id;
        this.config_path = config_path;
    }
    public void run(){
    	
    	startNode(1);
    	
    	long s=System.currentTimeMillis();
    	while(System.currentTimeMillis()<s+3000){}
    	startNode(0);
    	
    	
    	
    }
    
    private void startNode(int type) {
    	
        Node n = new Node(type, id, config_path);
        Thread t = new Thread(n);
        t.start();
    }

	public static void main(String[] args){
		int no_of_node = 2;
        String config_path = "config1.txt";
        //String h=
        //System.out.println(config_path.split("\\.")));
       Start start = new Start(no_of_node, config_path);
        Thread t=new Thread(start);
        t.start();
         

	}

}
