
public interface ResourceManager {
	
	//메모리 영역을 초기화 하는 메소드
	public void initializeMemory();
	//각 레지스터 값을 초기화 하는 메소드
	public void initializeRegister();
	
	//디바이스 접근데 대한 메소드
	//디바이스는 각 이름과 매칭되는 파일로 가정한다.
	//F1이라는 디바이스를 읽으면 F1이라는 파일에서 값을 읽는다.
	//해당 디바이스(파일)를 사용 가능한 상태로 만드는 메소드
	public void initialDevice(String devName);
	//선택한 디바이스(파일)에 값을 쓰는 메소드. 파라미터는 변경 가능하다.
	public void writeDevice(String devName, byte[] data, int size);
	//선택한 디바이스(파일)에서 값을 읽는 메소드. 파라미터는 변경 가능하다.
	public byte[] readDevice(String devName, int size);
	
	//메모리 영역에 값을 쓰는 메소드
	public void setMemory(int locate, byte[] data, int size);
	//레지스터에 값을 세팅하는 메소드, regNum은 레지스터 종류를 나타낸다.
	public void setRegister(int regNum, int value);
	//메모리 영역에서 값을 읽어오는 메소드
	public byte[] getMemory(int locate, int size);
	//레지스터에서 값을 읽어오는 메소드
	public int getRegister(int regNum);
	//레지스터 -> 메모리, 디바이스 데이터 타입 변경. 필요한 경우 구현
	//public byte[] getRegister(int data);
	//메모리, 디바이스 -> 레이스터 데이터 타입 변경. 필요한 경우 구현
	public int getRegister(byte[] data);
	
}
