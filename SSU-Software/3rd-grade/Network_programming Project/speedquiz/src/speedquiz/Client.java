package speedquiz;
import java.rmi.*;
public interface Client extends Remote {
	//RMI��� Ŭ���̾�Ʈ ���̷���
	public void back(String msg) throws RemoteException;
	//�Է��� ������ ������ ����
	public String getName() throws RemoteException;
	//����ڰ� ����� �̸��� ����
	public void update() throws RemoteException;
	//���� ���� �Լ�
}
