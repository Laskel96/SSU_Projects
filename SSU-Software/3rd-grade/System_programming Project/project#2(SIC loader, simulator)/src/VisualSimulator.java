import java.io.File;

public interface VisualSimulator {
	//�ùķ����͸� ���۽�Ű�� ���� ������ �����Ѵ�.
	//SIC �ùķ����͸� ���� �δ��� �����Ű��, �ε�� ������ �о� �����־�
	//������ ������ �� �ִ� ���·� ����� ���´�.
	public void initialize(File obFile, ResourceManager rMgr);
	
	//�ϳ��� ��ɾ �����ϴ� �޼ҵ�ν� SIC�ùķ����Ϳ��� �۾��� �����Ѵ�.
	public void oneStep();
	//���� ��ɾ ��� �����ϴ� �޼ҵ�ν� SIC�ùķ����Ϳ� �۾��� �����Ѵ�.
	public void allStep();
	//�۾��� �Ϸ�Ǿ��� �� ��ȭ�� ����� ȭ�鿡 ������Ʈ�Ѵ�.
	public void update();
	
}
