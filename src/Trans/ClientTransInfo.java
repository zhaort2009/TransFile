package Trans;
/**
 * @(#)ClientTest.java
 *
 * 客户端
 * @author 
 * @version 1.00 2008/8/2
 */
import java.net.Socket;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
public class ClientTransInfo {
    private Socket socket;
    BufferedReader in;
    PrintWriter out;
    
    public ClientTransInfo(String fileInfo) {
        String str = fileInfo;
        InputStream tmp = new ByteArrayInputStream(str.getBytes());    
        try{
            System.out.println("Try to connect to 127.0.0.1:10000");
            //向服务器发出连接请求
            socket=new Socket("127.0.0.1",10000);
            System.out.println("The Server Connected!");   
            
            System.out.println("Please enter some characters:");
            //读取用户输入信息
            BufferedReader line=new BufferedReader(new InputStreamReader(tmp));
            
            //输出从服务器端获得的信息
            out=new PrintWriter(socket.getOutputStream(),true);
            out.println(line.readLine());
            
            //读取服务器端信息
            in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(in.readLine());
            
            out.close();
            in.close();
            socket.close();    
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
    
    public static void main(String[] args){
    	new ClientTransInfo("hear me");
    }
    
}