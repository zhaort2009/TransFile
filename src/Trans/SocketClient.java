package Trans;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient extends Thread {
	protected String hostIp="127.0.0.1";
	protected int hostPort=3000;
	DataInputStream fis;
	DataOutputStream ps;
	File fi;
	String path;
	Socket client;
    public SocketClient(){
    	
    }
    public SocketClient(String path){
    	this.path=path;
    }
    public SocketClient(String path,String aHostIp,int aHostPort){
    	this.path=path;
    	this.hostIp=aHostIp;
    	this.hostPort=aHostPort;
    }
    public void setUpConnection(){
    	try{
    		client=new Socket(hostIp, hostPort);
    		fis=new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
    		ps=new DataOutputStream(client.getOutputStream());
    		fi=new File(path);
    	}catch (UnknownHostException e) {
			// TODO: handle exception
    		System.out.println("Error setting up socket connection :unknown host at: "+hostIp+": "+hostPort);
		}catch (IOException e) {
			// TODO: handle exception
			System.out.println("Error:"+e);
		}
    }
    public void sendMessage(){
    	setUpConnection();
    	if(client==null)
    		return;
    	try{
    		ps.writeUTF(fi.getName());
    		ps.flush();
    		
    		ps.writeLong((long)fi.length());
    		ps.flush();
    		int bufferSize=8*1024;
    		byte[] buf=new byte[bufferSize];
    		
    		while(true){
    			int read=0;
    			if(fis!=null)
    				read=fis.read(buf);
    			if(read==-1)
    				break;
    			ps.write(buf,0,read);
    		}
    		ps.flush();
    		
    		System.out.println("file transfer have finished!");
    	}catch (IOException e) {
			// TODO: handle exception
    		e.printStackTrace();
		}finally{
			try{
				ps.close();
				fis.close();
				client.close();
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
    }
    public static void main(String args[]){
    		String path="F:\\file.txt";
    		SocketClient client=new SocketClient(path);
    		client.sendMessage();
    }
    
}
