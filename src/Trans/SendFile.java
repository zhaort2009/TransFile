package Trans;

import java.net.*;
import java.io.*; /*�ڷ������˿����������
 * ʵ�����׽���
 * �������ļ�
 *  
 *  
 * */
import javax.swing.JFileChooser;

public class SendFile extends Thread {
String remoteIPString = null;// Զ�̵��ַ���
int port; // Զ�̵ķ���˿�
Socket tempSocket; // ��ʱ�׽���
OutputStream outSocket; // �����ļ��õ������
RandomAccessFile outFile; // �����͵��ļ�
byte byteBuffer[] = new byte[1024]; // �����ļ��õ���ʱ������

// ********������********************************************************
public static void main(String args[]) {
SendFile sf = new SendFile("127.0.0.1", 10000);
sf.start();

}

// ********������********************************************************

public SendFile(String remoteIPString, int port) { // ���캯��������ѡ�����ļ���λ��
// �����ⲿ����Զ�̵�ַ�Ͷ˿�
try {
this.remoteIPString = remoteIPString;
this.port = port;

// %%%%%%%%%%%%%%%ѡ���͵��ļ�λ��%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
JFileChooser jfc = new JFileChooser(".");
File file = null;
int returnVal = jfc.showOpenDialog(new javax.swing.JFrame());
if (returnVal == JFileChooser.APPROVE_OPTION) {
file = jfc.getSelectedFile();

}
// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

outFile = new RandomAccessFile(file, "r");

} catch (Exception e) {
}
}

public void run() {
try {
// �Ⱦ������Ƿ�������Ҫ�ȿ���
this.tempSocket = new Socket(this.remoteIPString, this.port);
System.out.println("����������ӳɹ�");
outSocket = tempSocket.getOutputStream();

int amount;
System.out.println("��ʼ�����ļ�");
while ((amount = outFile.read(byteBuffer)) != -1) {
outSocket.write(byteBuffer, 0, amount);
System.out.println("�ļ�������...");
}
System.out.println("Send File complete");
javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(),
"�ѷ������", "��ʾ!", javax.swing.JOptionPane.PLAIN_MESSAGE);
outFile.close();
tempSocket.close();

} catch (IOException e) {
System.out.println(e.toString());
e.printStackTrace();
}

}
}