import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ResourceManagerImpl implements ResourceManager{
		
		byte[] memory = new byte[5000];
		int[] register = new int[9];
		String filename;
		String startSectAddress = "000000";
		String progName = "COPY";
		String lengthProg = "1033";
		int ProgStartAddr = 0;
		File file;
		BufferedInputStream buf;
		boolean only = false;
		BufferedOutputStream obuf;
		boolean end = false;
		String using_dev = "";
		
		public String getLengthProg(){
			return lengthProg;
		}
		
		public void setUsingDev(String str){
			using_dev = str;
		}
		public String getUsingDev(){
			return using_dev;
		}
		public int getProgStartAddress(){
			return ProgStartAddr;
		}
		public String getStartSectAddress(){
			return startSectAddress;
		}
		public void setStartSectAddress(String str){
			startSectAddress = str;
		}
		public String getProgName(){
			return progName;
		}
		public void setProgName(String str){
			progName = str;
		}
		//�޸� ������ �ʱ�ȭ �ϴ� �޼ҵ�
		public void initializeMemory(){
			for(int i = 0; i < 5000; i++){
				memory[i] = 0;
			}	
			for(int i = 1000; i < 1300; i ++){
				memory[i] = 1;
			}
		}
		//�� �������� ���� �ʱ�ȭ �ϴ� �޼ҵ�
		public void initializeRegister(){
			for(int i = 0 ; i < 9; i ++){
				register[i] = 0;
			}
		}
		
		
		//����̽� ���ٵ� ���� �޼ҵ�
		//����̽��� �� �̸��� ��Ī�Ǵ� ���Ϸ� �����Ѵ�.
		//F1�̶�� ����̽��� ������ F1�̶�� ���Ͽ��� ���� �д´�.
		//�ش� ����̽�(����)�� ��� ������ ���·� ����� �޼ҵ�
		public void initialDevice(String devName){
			File file = new File(devName);   
            if (!file.exists()) {
            	try{
            		file.createNewFile();
            	}catch(IOException e){
            		System.out.println("There is errer" + e);
            	}
            }
            if(buf == null && !only){
	            try{
	            	if(devName.equals("F1"))
	            		buf = new BufferedInputStream(new FileInputStream(file));
	            	only = true;
	            }catch(IOException e){
	            	e.printStackTrace();
	            }
            }
            if(obuf == null){
	            try{
	            	if(devName.equals("05"))
	            		obuf = new BufferedOutputStream(new FileOutputStream(file));
	            }catch(IOException e){
	            	e.printStackTrace();
	            }
            }
		}
		
		//������ ����̽�(����)�� ���� ���� �޼ҵ�. �Ķ���ʹ� ���� �����ϴ�.
		public void writeDevice(String devName, byte[] data, int size){
			
		}
		//������ ����̽�(����)���� ���� �д� �޼ҵ�. �Ķ���ʹ� ���� �����ϴ�.
		public byte[] readDevice(String devName, int size){
			byte[] temp = {};
			return temp;
		}
		
		//�޸� ������ ���� ���� �޼ҵ�
		public void setMemory(int locate, byte[] data, int size){
			for(int i = locate, j = 0;  j < size; i++,j++){
				memory[i] = data[j];
			}
		}
		//�������Ϳ� ���� �����ϴ� �޼ҵ�, regNum�� �������� ������ ��Ÿ����.
		public void setRegister(int regNum, int value){
			register[regNum] = value;
		}
		//�޸� �������� ���� �о���� �޼ҵ�
		public byte[] getMemory(int locate, int size){
			byte[] temp = new byte[size];
			for(int j = 0, i = locate; j < size; i++,j++)
				temp[j] = memory[i];
			return temp;
		}
		
		//�������Ϳ��� ���� �о���� �޼ҵ�
		public int getRegister(int regNum){
			return register[regNum];
		}
		//�������� -> �޸�, ����̽� ������ Ÿ�� ����. �ʿ��� ��� ����
		//public byte[] getRegister(int data);
		//�޸�, ����̽� -> ���̽��� ������ Ÿ�� ����. �ʿ��� ��� ����
		public int getRegister(byte[] data){
			int temp= 0;
			return temp;
		}
		
}
