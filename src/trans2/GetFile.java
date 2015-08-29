package trans2;

/*
 * 该类用到的帮定端口初始为10000如果绑定不成功试另外的端口
 * 绑定次数用tryBindTimes表识如果帮定失败会对它加一的
 * 当前帮定端口由DefaultBindPort+tryBindTimes决定
 * 外界系统(调用此程序的对象)可以获取当前的帮定端口
 * 并告诉客户端服务的端口号以使其能正确连接到该端口上
 */

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFileChooser;

public class GetFile extends Thread {

ServerSocket serSocket; // 服务套接字等待 对方的连接和文件发送
Socket tempSocket; // 由服务套接字产生的 套接字
InputStream inSocket; // 用于读取

RandomAccessFile inFile = null; // 随机访问文件
byte byteBuffer[] = new byte[1024];// 临时缓寸区

int defaultBindPort = 10000;// 默认用10000端口监听请求
int tryBindTimes = 0; // 初始的绑定端口次数为0
int currentBindPort = defaultBindPort + tryBindTimes;// 当前绑定的端口号是10000默认端口

private void bindToServerPort() throws Exception// 抛出异常的原因是无法绑定服务的端口
{
try {
System.out.println("试绑定的端口号是:" + this.currentBindPort);// 输出绑定的端口号到当前的控制台上
serSocket = new ServerSocket(this.currentBindPort); // 在自己的机器上开一个服务类套接字
// 并等待发送者的连接

} catch (Exception e) {
e.printStackTrace();

System.out.println(e.toString());// 绑定不成功重试

this.tryBindTimes = this.tryBindTimes + 1;// 试了不止一次了
// 可查看试的次数getTryBindedTimes
this.currentBindPort = this.defaultBindPort + this.tryBindTimes;//
if (this.tryBindTimes >= 20) {
throw new Exception("无法绑定到指定端口" + '\n' + "试了太多次了!");
// 如果试的次数超过20次 退出
}
this.bindToServerPort();// 递归的绑定
}
System.out.println("成功绑定的端口号是:" + this.currentBindPort);// 输出绑定的端口号到当前的控制台上

}

public int getTryBindedTimes() {
return this.tryBindTimes;
}

public int getCurrentBindingPort() {
return this.currentBindPort;
}

// **********测试用*********************************************
public static void main(String args[]) {
String filePath = "E:\\open2.rar";
File fileOBJ = new File(filePath);
GetFile gf = null;
try {
gf = new GetFile(10000,fileOBJ);
} catch (Exception e) {
e.printStackTrace();
System.out.println("无法传送文件!");
System.exit(1);
}
gf.start();

}

// **********测试用*********************************************

public GetFile(int port,File fileOBJ) throws Exception// 抛出异常的原因是无法绑定服务的端口
{
try {

this.bindToServerPort();// 绑定服务的端口

} catch (Exception e) {
e.printStackTrace();
System.out.println(e.toString());// 绑定不成功重试
throw new Exception("绑定端口不成功!");

}
//JFileChooser jfc = new JFileChooser(".");// 文件选择器 以当前的目录打开
//jfc.showSaveDialog(new javax.swing.JFrame());
File savedFile = fileOBJ; // 获取当前的选择文件引用

if (savedFile != null)// 选择了文件
{
inFile = new RandomAccessFile(savedFile, "rw");// 用以读取数据的随机访问文件
// 可以每次以块的方式读取数据
}
}

public void run() {
try {
if (this.inFile == null) {
System.out.println("没有选择文件");
this.serSocket.close();// 关闭服务方套接字
return; // 没有选择文件
}
System.out.println("wait for..." + '\n' + "等待对方接入");
tempSocket = serSocket.accept(); // 等待对方的连接
this.serSocket.setSoTimeout(5000);// 五秒钟连不上将抛出异常
this.inSocket = tempSocket.getInputStream(); // 获取输入流
} catch (Exception ex) {
System.out.println(ex.toString());
ex.printStackTrace();
return;
}

int amount; // 以下为传送文件代码 和流 套接字清理工作
try {
while ((amount = inSocket.read(byteBuffer)) != -1) {
inFile.write(byteBuffer, 0, amount);
}

inSocket.close(); // 关闭流
//javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(),"已接收成功", "提示!", javax.swing.JOptionPane.PLAIN_MESSAGE);
System.out.println("Get OK");
System.out.println("接收完毕!");
inFile.close(); // 关闭文件
tempSocket.close(); // 关闭临时套接字
this.serSocket.close();// 关闭服务方套接字
} catch (IOException e) {
System.out.println(e.toString());
e.printStackTrace();
}

}

}

