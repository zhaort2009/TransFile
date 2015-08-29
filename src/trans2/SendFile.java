package trans2;

import java.net.*;
import java.io.*; /*在服务器端开启的情况下
 * 实例化套接字
 * 并发送文件
 *  
 *  
 * */
import javax.swing.JFileChooser;

public class SendFile extends Thread {
String remoteIPString = null;// 远程的字符串
int port; // 远程的服务端口
Socket tempSocket; // 临时套接字
OutputStream outSocket; // 发送文件用的输出流
RandomAccessFile outFile; // 欲发送的文件
byte byteBuffer[] = new byte[1024]; // 发送文件用的临时缓存区

// ********测试用********************************************************
public static void main(String args[]) {
String filePath = "D:\\lib.rar";
File fileOBJ = new File(filePath);	
SendFile sf = new SendFile("127.0.0.1",10000,fileOBJ);
sf.start();

}

// ********测试用********************************************************

public SendFile(String remoteIPString, int port, File fileOBJ) { // 构造函数仅用于选择发送文件的位置
// 并从外部接收远程地址和端口
try {
this.remoteIPString = remoteIPString;
this.port = port;

// %%%%%%%%%%%%%%%选择发送的文件位置%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//JFileChooser jfc = new JFileChooser(".");
File file = null;
//int returnVal = jfc.showOpenDialog(new javax.swing.JFrame());
//if (returnVal == JFileChooser.APPROVE_OPTION) {
file = fileOBJ;

//}
// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

outFile = new RandomAccessFile(file, "r");

} catch (Exception e) {
}
}

public void run() {
try {
// 先决条件是服务器端要先开启
this.tempSocket = new Socket(this.remoteIPString, this.port);
System.out.println("与服务器连接成功");
outSocket = tempSocket.getOutputStream();

int amount;
System.out.println("开始发送文件");
while ((amount = outFile.read(byteBuffer)) != -1) {
outSocket.write(byteBuffer, 0, amount);
System.out.println("文件发送中...");
}
System.out.println("Send File complete");
//javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(),"已发送完毕", "提示!", javax.swing.JOptionPane.PLAIN_MESSAGE);
outFile.close();
tempSocket.close();

} catch (IOException e) {
System.out.println(e.toString());
e.printStackTrace();
}

}
}