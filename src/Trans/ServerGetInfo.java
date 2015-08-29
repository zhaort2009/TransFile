package Trans;
/**
 * @(#)ServerSocketTest.java
 *
 * 服务器端
 * @author 
 * @version 1.00 2008/8/2
 */
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
public class ServerGetInfo {
    private ServerSocket ss;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    public ServerGetInfo() {
        try{
            ss=new ServerSocket(10000);//建立服务器，监听...
            System.out.println("Server is listening at 10000...");
            while(true){
                socket=ss.accept();
                //获取客户端IP地址
                String remoteIP=socket.getInetAddress().getHostAddress();
                //获取客户端连接端口
                String remotePort=":"+socket.getLocalPort();
                System.out.println("A clinet come in!IP:"+remoteIP+remotePort);
                
                //读取客户端输入
                in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line=in.readLine();
                System.out.println("Client send is:"+line);
                
                //将服务器端信息发往客户端
                out=new PrintWriter(socket.getOutputStream(),true);
                out.println("Your Message Received!");
                
                out.close();
                in.close();
                socket.close();
            }
        }catch(IOException ex){
            System.out.println(ex.getCause());
        }
    }
    
    public static void main (String[] args) {
        new ServerGetInfo();
    }  
}