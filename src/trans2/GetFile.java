package trans2;

/*
 * �����õ��İﶨ�˿ڳ�ʼΪ10000����󶨲��ɹ�������Ķ˿�
 * �󶨴�����tryBindTimes��ʶ����ﶨʧ�ܻ������һ��
 * ��ǰ�ﶨ�˿���DefaultBindPort+tryBindTimes����
 * ���ϵͳ(���ô˳���Ķ���)���Ի�ȡ��ǰ�İﶨ�˿�
 * �����߿ͻ��˷���Ķ˿ں���ʹ������ȷ���ӵ��ö˿���
 */

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFileChooser;

public class GetFile extends Thread {

ServerSocket serSocket; // �����׽��ֵȴ� �Է������Ӻ��ļ�����
Socket tempSocket; // �ɷ����׽��ֲ����� �׽���
InputStream inSocket; // ���ڶ�ȡ

RandomAccessFile inFile = null; // ��������ļ�
byte byteBuffer[] = new byte[1024];// ��ʱ������

int defaultBindPort = 10000;// Ĭ����10000�˿ڼ�������
int tryBindTimes = 0; // ��ʼ�İ󶨶˿ڴ���Ϊ0
int currentBindPort = defaultBindPort + tryBindTimes;// ��ǰ�󶨵Ķ˿ں���10000Ĭ�϶˿�

private void bindToServerPort() throws Exception// �׳��쳣��ԭ�����޷��󶨷���Ķ˿�
{
try {
System.out.println("�԰󶨵Ķ˿ں���:" + this.currentBindPort);// ����󶨵Ķ˿ںŵ���ǰ�Ŀ���̨��
serSocket = new ServerSocket(this.currentBindPort); // ���Լ��Ļ����Ͽ�һ���������׽���
// ���ȴ������ߵ�����

} catch (Exception e) {
e.printStackTrace();

System.out.println(e.toString());// �󶨲��ɹ�����

this.tryBindTimes = this.tryBindTimes + 1;// ���˲�ֹһ����
// �ɲ鿴�ԵĴ���getTryBindedTimes
this.currentBindPort = this.defaultBindPort + this.tryBindTimes;//
if (this.tryBindTimes >= 20) {
throw new Exception("�޷��󶨵�ָ���˿�" + '\n' + "����̫�����!");
// ����ԵĴ�������20�� �˳�
}
this.bindToServerPort();// �ݹ�İ�
}
System.out.println("�ɹ��󶨵Ķ˿ں���:" + this.currentBindPort);// ����󶨵Ķ˿ںŵ���ǰ�Ŀ���̨��

}

public int getTryBindedTimes() {
return this.tryBindTimes;
}

public int getCurrentBindingPort() {
return this.currentBindPort;
}

// **********������*********************************************
public static void main(String args[]) {
String filePath = "E:\\open2.rar";
File fileOBJ = new File(filePath);
GetFile gf = null;
try {
gf = new GetFile(10000,fileOBJ);
} catch (Exception e) {
e.printStackTrace();
System.out.println("�޷������ļ�!");
System.exit(1);
}
gf.start();

}

// **********������*********************************************

public GetFile(int port,File fileOBJ) throws Exception// �׳��쳣��ԭ�����޷��󶨷���Ķ˿�
{
try {

this.bindToServerPort();// �󶨷���Ķ˿�

} catch (Exception e) {
e.printStackTrace();
System.out.println(e.toString());// �󶨲��ɹ�����
throw new Exception("�󶨶˿ڲ��ɹ�!");

}
//JFileChooser jfc = new JFileChooser(".");// �ļ�ѡ���� �Ե�ǰ��Ŀ¼��
//jfc.showSaveDialog(new javax.swing.JFrame());
File savedFile = fileOBJ; // ��ȡ��ǰ��ѡ���ļ�����

if (savedFile != null)// ѡ�����ļ�
{
inFile = new RandomAccessFile(savedFile, "rw");// ���Զ�ȡ���ݵ���������ļ�
// ����ÿ���Կ�ķ�ʽ��ȡ����
}
}

public void run() {
try {
if (this.inFile == null) {
System.out.println("û��ѡ���ļ�");
this.serSocket.close();// �رշ����׽���
return; // û��ѡ���ļ�
}
System.out.println("wait for..." + '\n' + "�ȴ��Է�����");
tempSocket = serSocket.accept(); // �ȴ��Է�������
this.serSocket.setSoTimeout(5000);// �����������Ͻ��׳��쳣
this.inSocket = tempSocket.getInputStream(); // ��ȡ������
} catch (Exception ex) {
System.out.println(ex.toString());
ex.printStackTrace();
return;
}

int amount; // ����Ϊ�����ļ����� ���� �׽�����������
try {
while ((amount = inSocket.read(byteBuffer)) != -1) {
inFile.write(byteBuffer, 0, amount);
}

inSocket.close(); // �ر���
//javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(),"�ѽ��ճɹ�", "��ʾ!", javax.swing.JOptionPane.PLAIN_MESSAGE);
System.out.println("Get OK");
System.out.println("�������!");
inFile.close(); // �ر��ļ�
tempSocket.close(); // �ر���ʱ�׽���
this.serSocket.close();// �رշ����׽���
} catch (IOException e) {
System.out.println(e.toString());
e.printStackTrace();
}

}

}
