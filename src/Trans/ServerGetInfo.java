package Trans;
/**
 * @(#)ServerSocketTest.java
 *
 * ��������
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
            ss=new ServerSocket(10000);//����������������...
            System.out.println("Server is listening at 10000...");
            while(true){
                socket=ss.accept();
                //��ȡ�ͻ���IP��ַ
                String remoteIP=socket.getInetAddress().getHostAddress();
                //��ȡ�ͻ������Ӷ˿�
                String remotePort=":"+socket.getLocalPort();
                System.out.println("A clinet come in!IP:"+remoteIP+remotePort);
                
                //��ȡ�ͻ�������
                in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line=in.readLine();
                System.out.println("Client send is:"+line);
                
                //������������Ϣ�����ͻ���
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