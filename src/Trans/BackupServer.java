package Trans;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import xjtu.dedup.multithreads.ThreadPool;


public class BackupServer {
protected int listenPort=3000;
Socket socket=null;
public static void main(String args[]){
	System.out.println("Server starting");
	BackupServer  server=new BackupServer();
	server.acceptConnection();
	server.close();
}

//create connection
public void acceptConnection(){
	try{
		ServerSocket server=new ServerSocket();
		server.setReuseAddress(true);
		                             
		int size=server.getReceiveBufferSize();
		if(size<131072)
			server.setReceiveBufferSize(131072);
		server.bind(new InetSocketAddress(listenPort));
		ThreadPool tp=new ThreadPool();
		while(true){
			socket=server.accept();
			//new ServerThread(socket).start();
			System.out.println("begin to bind to port: "+listenPort);
			tp.start(socket);
		}
		
	}catch (BindException e) {
		// TODO: handle exception
		System.out.println("unable to bind to port "+listenPort);
	}catch (IOException e) {
		// TODO: handle exception
		System.out.println("Unable to instantiate a ServerSocket Port "+listenPort);
	}
}
public void close(){
	try{
		socket.close();
	}catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
}
}
