package speedquiz;
import java.rmi.*;
public interface Server extends Remote{
	//RMI��� ���̷���
	public void send(String msg) throws RemoteException;
	//�޼����� ������ �Լ�
	public void register(Client client, String name) throws RemoteException;
	//�̿��� �̸��� ���� ���������� �߰��ϸ�, �����ϼ̽��ϴ� �޼����� �����
	public void unregister(Client client, String name) throws RemoteException;
	//����� �̿��� �̸��� ���� �������� �����ϸ�, �����Ͽ����ϴ�.��� �޼��� �����
	public int getIndex() throws RemoteException;
	//�ε����� �޾ƿ��� �Լ�
}
