
public interface ResourceManager {
	
	//�޸� ������ �ʱ�ȭ �ϴ� �޼ҵ�
	public void initializeMemory();
	//�� �������� ���� �ʱ�ȭ �ϴ� �޼ҵ�
	public void initializeRegister();
	
	//����̽� ���ٵ� ���� �޼ҵ�
	//����̽��� �� �̸��� ��Ī�Ǵ� ���Ϸ� �����Ѵ�.
	//F1�̶�� ����̽��� ������ F1�̶�� ���Ͽ��� ���� �д´�.
	//�ش� ����̽�(����)�� ��� ������ ���·� ����� �޼ҵ�
	public void initialDevice(String devName);
	//������ ����̽�(����)�� ���� ���� �޼ҵ�. �Ķ���ʹ� ���� �����ϴ�.
	public void writeDevice(String devName, byte[] data, int size);
	//������ ����̽�(����)���� ���� �д� �޼ҵ�. �Ķ���ʹ� ���� �����ϴ�.
	public byte[] readDevice(String devName, int size);
	
	//�޸� ������ ���� ���� �޼ҵ�
	public void setMemory(int locate, byte[] data, int size);
	//�������Ϳ� ���� �����ϴ� �޼ҵ�, regNum�� �������� ������ ��Ÿ����.
	public void setRegister(int regNum, int value);
	//�޸� �������� ���� �о���� �޼ҵ�
	public byte[] getMemory(int locate, int size);
	//�������Ϳ��� ���� �о���� �޼ҵ�
	public int getRegister(int regNum);
	//�������� -> �޸�, ����̽� ������ Ÿ�� ����. �ʿ��� ��� ����
	//public byte[] getRegister(int data);
	//�޸�, ����̽� -> ���̽��� ������ Ÿ�� ����. �ʿ��� ��� ����
	public int getRegister(byte[] data);
	
}
