import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SicSimulatorImpl {
	//시뮬레이터를 동작시키기 위한 세팅을 수행한다.
	//메모리 작업 등 실질적인 초기화 작업을 수행한다.
	SicLoaderImpl loader;
	ResourceManagerImpl rMgr;
	File file;
	int loc;
	
	boolean same = false;
	boolean less_than = false;
	boolean only = false;
	
	ArrayList<String> inst = new ArrayList<String>();
	ArrayList<String> log = new ArrayList<String>();
	String targetAddr = "";
	
	public void initialize(File obFile, ResourceManager rMgr){
		this.rMgr = (ResourceManagerImpl)rMgr;
		System.out.println("simulator initialized");
		file = obFile;
		loader = new SicLoaderImpl();
		loader.load(this.file, this.rMgr);
	}
	public String getTargetAddr(){
		return targetAddr;
	}
	public ArrayList<String> getInst(){
		return inst;
	}
	public ArrayList<String> getLog(){
		return log;
	}
	
	public byte[] divideToByte(int data, int num){
		byte[] temp = new byte[num];
		for(int i = num-1; i > 0; i--){
			temp[i] = (byte) (data & 0xFF);
			data = data >> 0xFF;
		}
		return temp;
	}
	
	public int combineToInt(byte[] data, int num)
	{
		int temp = 0;
		for(int i = 0 ; i < num-1; i++){
			temp += data[i];
			temp = temp << 8;
		}
		temp += data[num-1];
		return temp;
	}
	
	public String combineToString(byte[] data, int num){
		String result = "";
		for(int i = 0; i < num; i++){
			int temp = 0;
			temp = data[i];
			result += Integer.toHexString(temp).toUpperCase();
		}
		if(result.length()%2 == 1 && result.length() <= 6){
			result = result.replace("0", "00");
		}
		result = result.replace("FFFFFF", "");
		return result;
	}
	
	public int getAddress(byte[] data,int type){
		int result = 0;
		if(type == 2){
			result += data[1];
		}
		else if(type == 3){
			result += data[1];
			result = result << 8;
			result += data[2];
			result = result &0x0FFF;
		}
		else if(type ==4){
			result += data[1];
			result = result << 8;
			result += data[2];
			result = result << 8;
			result += data[3];
			result = result &0x0FFFFF;
		}
		return result;
	}
	//하나의 명령어만 수행한다. 해당 명령어가 수행되고난 값을 변화를 보여주고
	//다음 명령어를 포인팅한다.
	//실질적인 동작을 수행하는 메소드
	public void oneStep(){
		File file;
		boolean ready = true;
		byte[] temp1 = this.rMgr.getMemory(loc, 2);
		int baseNum = 0;
		baseNum += temp1[0];
		baseNum = baseNum << 8;
		baseNum += temp1[1];
		
		int count = 0;
		int extended = baseNum & 0x10; // 4형식이면 1 아니면 0
		baseNum = baseNum >> 8;
		int opcode = baseNum & 0xFC;
		int immediate = baseNum & 0x01;
		int indirect = baseNum &0x02;
		String Hopcode = Integer.toHexString(opcode).toUpperCase();
		int rdata = 0;
		byte[] temp2; // 명령어 로드
		byte[] temp3; // 추가적인 정보 활용에 사용
		int address = 0;
		switch(Hopcode)
		{
			case "14": //3형식 STL
				log.add("STL");
				temp2 = this.rMgr.getMemory(loc, 3);
				inst.add(combineToString(temp2,3));
				address = getAddress(temp2, 3);
				loc = loc+3;
				byte[] data = new byte[3];
				rdata = rMgr.getRegister(2);
				data = divideToByte(rdata, 3);
				rMgr.setMemory(loc+address, data, 3);
				targetAddr = Integer.toHexString(loc+address);
				break;
			case "48" : // 4형식 JSUB
				log.add("+JSUB");
				temp2 = this.rMgr.getMemory(loc, 4);
				inst.add(combineToString(temp2,4));
				address = getAddress(temp2, 4);
				loc = loc+4;
				rMgr.setRegister(2, loc);
				loc = address;
				targetAddr = Integer.toHexString(address);
				break;
			case "B4" : // 2형식 CLEAR
				log.add("CLEAR");
				temp2 = this.rMgr.getMemory(loc, 2);
				inst.add(combineToString(temp2,2));
				address = getAddress(temp2, 2);
				address = address & 0xF0;
				address = address >> 4;
				if(address == 1)
					rMgr.setRegister(1, 0);
				else if(address == 0)
					rMgr.setRegister(0, 0);
				else if(address == 4)
					rMgr.setRegister(4, 0);
				loc = loc+2;
				targetAddr = "000000";
				break;
			case "74" : //3형식, 4형식 LDT
				if(extended == 0){ // 3형식
					log.add("LDT");
					temp2 = this.rMgr.getMemory(loc, 3);
					inst.add(combineToString(temp2,3));
					address = getAddress(temp2, 3);
					loc += 3;
					temp3 = rMgr.getMemory(loc+address, 3);
					rdata = combineToInt(temp3 , 3);
					rMgr.setRegister(5, rdata);
					targetAddr = Integer.toHexString(loc+address);
					break;
				}else if(extended != 0){
					log.add("+LDT");
					temp2 = this.rMgr.getMemory(loc, 4);
					inst.add(combineToString(temp2,4));
					address = getAddress(temp2, 4);
					loc += 4;
					temp3 = rMgr.getMemory(address, 3);
					rdata = combineToInt(temp3 , 3);
					rMgr.setRegister(5, rdata);
					targetAddr = Integer.toHexString(address);
					break;
				}
			case "E0" : //3형식 TD
				log.add("TD");
				temp2 = this.rMgr.getMemory(loc, 3);
				inst.add(combineToString(temp2,3));
				address = getAddress(temp2, 3);
				loc += 3;
				temp3 = rMgr.getMemory(loc+address, 1);
				String a = "";
				a += temp3[0];
				if(a.equals("-15")){
					a = "F1";
					rMgr.setUsingDev(a);
				}
				if(a.equals("5")){
					a = "05";
					rMgr.setUsingDev(a);
				}
				ready = false;
				rMgr.initialDevice(a);
				ready = true;
				targetAddr = Integer.toHexString(loc+address);
				break;
			case "30" : //3형식 JEQ
				log.add("JEQ");
				temp2 = this.rMgr.getMemory(loc, 3);
				inst.add(combineToString(temp2,3));
				address = getAddress(temp2, 3);
				if(!ready){
					loc = address+loc;
				}
				if(same){
					loc = address+loc;
				}
				loc += 3;
				targetAddr = Integer.toHexString(loc+address);
				break;
			case "D8" : // 3형식 RD
				log.add("RD");
				temp2 = this.rMgr.getMemory(loc, 3);
				inst.add(combineToString(temp2,3));
				address = getAddress(temp2, 3);
				loc += 3;
				temp3 = rMgr.getMemory(loc+address, 1);
				String b = "";
				b += temp3[0];
				if(b.equals("-15"))
					b = "F1";
				try{
					 rdata = rMgr.buf.read();
				}catch(IOException e){
					e.printStackTrace();
				}
				if(rdata == -1 || rdata == '?')
					same = true;
				else{
					rMgr.setRegister(0, rdata);
				}
				targetAddr = Integer.toHexString(loc+address);
				break;
			case "A0" : // COMPR
				log.add("COMPR");
				temp2 = this.rMgr.getMemory(loc, 2);
				inst.add(combineToString(temp2,2));
				address = getAddress(temp2, 2);
				loc+=2;
				int first = address & 0xF0;
				int second = address & 0xF;
				if(rMgr.getRegister(first) == rMgr.getRegister(second))
					same = true;
				targetAddr = "000000";
				break;
			case "54" : //4형식 STCH
				log.add("+STCH");
				temp2 = this.rMgr.getMemory(loc, 4);
				inst.add(combineToString(temp2,4));
				address = getAddress(temp2, 4);
				loc = loc+4;
				byte[] data1 = new byte[1];
				rdata = rMgr.getRegister(0);
				data1[0] = (byte) rdata;
				rMgr.setMemory(address + rMgr.getRegister(1), data1, 1);
				targetAddr = Integer.toHexString(address+rMgr.getRegister(1));
				break;
			case "B8" ://2형식 TIXR
				log.add("TIXR");
				temp2 = this.rMgr.getMemory(loc, 2);
				inst.add(combineToString(temp2,2));
				address = getAddress(temp2, 2);
				loc+=2;
				address = (address & 0xF0) / 16;
				if(rMgr.getRegister(1) < rMgr.getRegister(address)){
					rMgr.setRegister(1, rMgr.getRegister(1)+1);
					less_than = true;
				}
				else
					less_than = false;
				targetAddr = "000000";
				break;
			case "38" : //3형식 JLT
				log.add("JLT");
				temp2 = this.rMgr.getMemory(loc, 3);
				inst.add(combineToString(temp2,3));
				address = getAddress(temp2, 3);
				loc += 3;
				if(less_than){
					loc = loc + address+0x100-0x1000;
				}
				targetAddr = Integer.toHexString(loc + address+0x100-0x1000);
				break;
			case "10" : //4형식 STX
				log.add("STX");
				temp2 = this.rMgr.getMemory(loc, 4);
				inst.add(combineToString(temp2,4));
				address = getAddress(temp2, 4);
				loc = loc+4;
				temp3 = divideToByte(rMgr.getRegister(1),3);
				rMgr.setMemory(address, temp3, 3);
				targetAddr = Integer.toHexString(address);
				break;
			case "4C" : //3형식 RSUB
				log.add("RSUB");
				temp2 = this.rMgr.getMemory(loc,3);
				inst.add(combineToString(temp2,3));
				loc = rMgr.getRegister(2);
				same = false; // 서브루틴 끝나면서 다시 복귀
				targetAddr = Integer.toHexString(rMgr.getRegister(2));
				break;
			case "0" : // 3형식 LDA
				log.add("LDA");
				count++;
				if(immediate == 1 && indirect == 0){
					temp2 = this.rMgr.getMemory(loc, 3);
					inst.add(combineToString(temp2,3));
					address = getAddress(temp2, 3);
					rMgr.setRegister(0, address);
					loc += 3;
					temp3 = rMgr.getMemory(loc+address, 3);
					targetAddr = Integer.toHexString(address+loc);
				}else{
					temp2 = this.rMgr.getMemory(loc, 3);
					inst.add(combineToString(temp2,3));
					address = getAddress(temp2, 3);
					loc += 3;
					temp3 = rMgr.getMemory(loc+address, 3);
					rdata = combineToInt(temp3 , 3);
					rMgr.setRegister(0, rdata);
					targetAddr = Integer.toHexString(address);
				}
				break;
			case "28" : // COMP immeditate
				log.add("COMP");
				temp2 = this.rMgr.getMemory(loc, 3);
				inst.add(combineToString(temp2,3));
				address = getAddress(temp2, 3);
				loc += 3;
				temp3 = rMgr.getMemory(loc+address, 3);
				rdata = combineToInt(temp3 , 3);
				if(rMgr.getRegister(0) == rdata)
					same = true;
				targetAddr = "000000";
				break;
			case "50" : //4형식 +LDCH
				log.add("+LDCH");
				temp2 = this.rMgr.getMemory(loc, 4);
				inst.add(combineToString(temp2,4));
				address = getAddress(temp2, 4);
				loc = loc+4;
				temp3 = rMgr.getMemory(address + rMgr.getRegister(1), 1);
				int l = combineToInt(temp3,1);
				rMgr.setRegister(0, l);
				targetAddr = Integer.toHexString(address);
				break;
			case "DC" : // 3형식 WD
				log.add("WD");
				temp2 = this.rMgr.getMemory(loc, 3);
				inst.add(combineToString(temp2,3));
				address = getAddress(temp2, 3);
				loc += 3;
				temp3 = rMgr.getMemory(loc+address, 1);
				String c = "";
				c += temp3[0];
				if(c.equals("5"))
					b = "05";
				try{
					 rMgr.obuf.write(rMgr.getRegister(0));
				}catch(IOException e){
					e.printStackTrace();
				}
				targetAddr = Integer.toHexString(address+loc);
				break;
			case "3C" : //3형식 J 구현 실패
				log.add("J");
				temp2 = this.rMgr.getMemory(loc, 3);
				inst.add(combineToString(temp2,3));
				address = getAddress(temp2, 3);
				loc += 3;
				targetAddr = Integer.toHexString(address+loc);
				if(!only){
					loc += 3;
					try {
						rMgr.obuf.write('E');
						rMgr.obuf.write('O');
						rMgr.obuf.write('F');
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					only = true;
				}
				else{
					try {
						rMgr.obuf.close();
						rMgr.end = true;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case "C" : // 3형식 STA
				log.add("STA");
				temp2 = this.rMgr.getMemory(loc, 3);
				inst.add(combineToString(temp2,3));
				address = getAddress(temp2, 3);
				loc = loc+3;
				byte[] data2 = new byte[3];
				rdata = rMgr.getRegister(0);
				data = divideToByte(rdata, 3);
				rMgr.setMemory(loc+address, data, 3);
				targetAddr = Integer.toHexString(address+loc);
				break;
				
			default : 
				break;
		}
		rMgr.setRegister(7, loc);
	}
	
	//남은 명령어를 모두 수행하는메소드.
	//목적 코드를 모두 수행하고 난 값의 변화를 보여준다.
	//실질적인 동작을 수행하는 메소드
	public void allStep(){
		for(int i = 0; i < 120; i++)
			oneStep();
	}
	
}
