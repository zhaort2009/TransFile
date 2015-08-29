package Trans;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.Socket;

import com.sun.accessibility.internal.resources.accessibility;

public class ServerThread extends Thread {
private Socket socket;

public ServerThread(Socket socket){
	this.socket=socket;
}

public void run(){
	try{
		DataInputStream inputStream=null;
		try{
			inputStream=new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("receive message buffer error");
			return;
		}
		
		try{
			//save file path in local
			String savePath="E:\\deduptest\\volumes\\files\\";
			int bufferSize=8192;
			byte[] buf=new byte[bufferSize];
			long len=0;
			savePath+=inputStream.readUTF();
			DataOutputStream fileOut=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(savePath)));
			len=inputStream.readLong();
			System.out.println("file length : "+len);
			System.out.println("start to receive file!");
			while(true){
				int read=0;
				if(inputStream!=null){
					read=inputStream.read(buf);
				}
				if(read==-1){
					break;
				}
				fileOut.write(buf,0,read);
			}
			System.out.println("finished!");
			fileOut.flush();
			fileOut.close();
			inputStream.close();
		
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("error!");
			return;
		}
	}catch (Exception e) {
		// TODO: handle exception
		System.out.println("Error handling a client: "+e);
	}
}
}
