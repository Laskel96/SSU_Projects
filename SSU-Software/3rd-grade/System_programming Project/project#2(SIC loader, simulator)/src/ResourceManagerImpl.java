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
		//메모리 영역을 초기화 하는 메소드
		public void initializeMemory(){
			for(int i = 0; i < 5000; i++){
				memory[i] = 0;
			}	
			for(int i = 1000; i < 1300; i ++){
				memory[i] = 1;
			}
		}
		//각 레지스터 값을 초기화 하는 메소드
		public void initializeRegister(){
			for(int i = 0 ; i < 9; i ++){
				register[i] = 0;
			}
		}
		
		
		//디바이스 접근데 대한 메소드
		//디바이스는 각 이름과 매칭되는 파일로 가정한다.
		//F1이라는 디바이스를 읽으면 F1이라는 파일에서 값을 읽는다.
		//해당 디바이스(파일)를 사용 가능한 상태로 만드는 메소드
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
		
		//선택한 디바이스(파일)에 값을 쓰는 메소드. 파라미터는 변경 가능하다.
		public void writeDevice(String devName, byte[] data, int size){
			
		}
		//선택한 디바이스(파일)에서 값을 읽는 메소드. 파라미터는 변경 가능하다.
		public byte[] readDevice(String devName, int size){
			byte[] temp = {};
			return temp;
		}
		
		//메모리 영역에 값을 쓰는 메소드
		public void setMemory(int locate, byte[] data, int size){
			for(int i = locate, j = 0;  j < size; i++,j++){
				memory[i] = data[j];
			}
		}
		//레지스터에 값을 세팅하는 메소드, regNum은 레지스터 종류를 나타낸다.
		public void setRegister(int regNum, int value){
			register[regNum] = value;
		}
		//메모리 영역에서 값을 읽어오는 메소드
		public byte[] getMemory(int locate, int size){
			byte[] temp = new byte[size];
			for(int j = 0, i = locate; j < size; i++,j++)
				temp[j] = memory[i];
			return temp;
		}
		
		//레지스터에서 값을 읽어오는 메소드
		public int getRegister(int regNum){
			return register[regNum];
		}
		//레지스터 -> 메모리, 디바이스 데이터 타입 변경. 필요한 경우 구현
		//public byte[] getRegister(int data);
		//메모리, 디바이스 -> 레이스터 데이터 타입 변경. 필요한 경우 구현
		public int getRegister(byte[] data){
			int temp= 0;
			return temp;
		}
		
}
