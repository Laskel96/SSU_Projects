import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SicLoaderImpl implements SicLoader{
	
	class ESTAB_UNIT{
		public String CS = "";
		public String SymName="";
		public String Address = "";
		public String Length = "";
	}
	
	ResourceManagerImpl rMgr; 
	ArrayList<ESTAB_UNIT> ESTAB = new ArrayList<ESTAB_UNIT>(); // ESTAB
	
	/*
	 * estab을 검사하는 매소드
	 * 찾으면 인덱스를 없으면 -1을 반환
	 * type == 0 : control section
	 * type == 1 : symbol 
	 */
	public int searchEstab(String name, int type){ 
		if(type == 0){//controlsection name
			for(int i = 0; i < ESTAB.size(); i++){
				if(ESTAB.get(i).CS.equals(name))
					return i;
			}
		}
		type++;
		if(type == 1){//symbolname
			for(int i = 0; i < ESTAB.size(); i++){
				if(ESTAB.get(i).SymName.equals(name))
					return i;
			}
		}
		return -1;
	}
	
	//목적코드를 읽어 메모리에 로드한다.
	//목적코드의 각 헤더(H, T ,M 등)를 읽어 동작을 수행한다.
		
	public void load(File objFile, ResourceManager rMgr){
		if(objFile != null){
			this.rMgr = (ResourceManagerImpl)rMgr;
			this.rMgr.initializeMemory(); //메모리 초기화
			this.rMgr.initializeRegister(); // 레지스트리 초기화
			//this.rMgr.init_inst_file();
			int PROADDR = this.rMgr.getProgStartAddress(); // os로 부터 받아오는 로딩주소
			try{ // load pass1
				int CSADDR = PROADDR; // constrolsection 시작주소
				int PROGLENGTH = 0; //프로그램 길이
				BufferedReader in = new BufferedReader(new FileReader(objFile)); //BufferedReader 생성
				String s;		
				while((s = in.readLine()) != null){ // 한줄 읽어서
					if(s.startsWith("H")){ // if H
						CSADDR += PROGLENGTH; // 프로그램 길이 더해줌
						StringBuilder sb = new StringBuilder(s);
						sb.deleteCharAt(0); // H제거
						s = sb.toString();// 스트링으로 저장
						String[] split = s.split("	"); //탭으로 분리
						if(searchEstab(split[0],0) == -1){ // section이름으로 검색/ 없다면
							ESTAB_UNIT temp = new ESTAB_UNIT(); // 임시변수
							temp.CS = split[0]; // control section 이름 저장 (프로그램 이름 저장)
							//this.rMgr.setProgName(temp.CS);
							temp.Address = Integer.toHexString(Integer.parseInt(split[1].substring(0, 6),16) + CSADDR); // 주소 저장/ 16진수로 읽어와서 시작주소 더하고 hex로
							temp.Length = split[1].substring(6, 12);
							PROGLENGTH = Integer.parseInt(temp.Length,16); // 프로그램 길이 세팅
							ESTAB.add(temp);
					
							//System.out.println(temp.CS + " " + temp.Length);
						}
						else{
							System.out.println("Duplicated ControlSection Name");
						}
						
					}
					else if(s.startsWith("D")){//if D
						StringBuilder sb = new StringBuilder(s);
						sb.deleteCharAt(0);
						s = sb.toString();
						String[] split = new String[6];
						int j = 0;
						for(int i = 0; i < s.length(); i = i+6){ // 6개씩 짤라 읽음
							split[j++] = s.substring(i, i+6);
						}
						for(int i = 0; i < split.length; i = i+2){ // 이름만 체크  ex(이름, 주소, 이름 , 주소 ... )형태이기 때문
							if(searchEstab(split[i],1) == -1){ // 이름 검사
								ESTAB_UNIT temp = new ESTAB_UNIT();
								temp.SymName = split[i];
								temp.Address = Integer.toHexString(Integer.parseInt(split[i+1], 16)+CSADDR);
								ESTAB.add(temp);
							}
							else{
								System.out.println("Duplicated Symbol Name");
							}
						}
					}
				}
			}
			catch(IOException e){
				System.out.println("There is errer" + e);
			}
			
			try{ // pass2
				int CSADDR = PROADDR;
				int PROGLENGTH = 0;
				int loc = 0; //location counter
				int opcode = -1;
				int opindex = 0;
				BufferedReader in = new BufferedReader(new FileReader(objFile)); //BufferedReader 생성
				String s;		
				while((s = in.readLine()) != null){ // 한줄 읽어서
					if(s.startsWith("H")){ // if H
						CSADDR += PROGLENGTH;
						loc = 0;
						StringBuilder sb = new StringBuilder(s);
						sb.deleteCharAt(0);
						s = sb.toString();
						String[] split = s.split("	");
						PROGLENGTH = Integer.parseInt(split[1].substring(6, 12),16);
					}
					else if(s.startsWith("T")){ // if T
						byte[] object = new byte[4]; //바이트 배열 (ResourceManagerImpl 인터페이스 때문에 byte array를 사용해야함. 하는김에 명령어끼리 자름
						int object_loc = 0; // object index
						int pos = 9; // 스트링 시작 ( T 제거)
						int count = 0; //읽은 개수
						byte[] bA = s.getBytes(); // 스트링을 byte array로
						int extended = 0; // 확장되면 0
						boolean twoform = false;
						while(pos < bA.length){ // 한줄 끝까지
							
							int num = 0;
							num = bA[pos++]-0x30; // 30 빼서 아스키를 숫자로
							if(num > 9) // 만약크면 한번더 빼줌
								num -= 0x7;
							if(count == 0){ //숫자 한개
								object[object_loc] = (byte) (object[object_loc] + num);
								object[object_loc] = (byte) (object[object_loc] << 4);
								count++;
							}else{ // 추가
								StringBuilder sb = new StringBuilder();
								sb.append(String.format("%02X", object[object_loc]));
								object[object_loc] = (byte) (object[object_loc] + num);
								count = 0;
								//System.out.println((char)bA[pos-2]+""+(char)bA[pos-1]+" "+object[object_loc]);
								
								if(object_loc == 1) // extend 검사
									extended = object[object_loc] & 0x10;
								if(object_loc == 0 && (object[object_loc] == -76 || object[object_loc] == -72 || object[object_loc] == -96)){
									twoform = true;
								}
								/*if(object_loc == 0){
									opcode =  object[object_loc] & 0xFC;
									String t = Integer.toHexString(opcode);
									if(opcode < 16)
										t = "0"+t;
									t = t.replaceFirst("^0x",""); // 0x 제거
									opindex = this.rMgr.search_opcode(t);
								}*/
								object_loc++; // 증가
								if(object[0] == -15 ||object[0] == 05)
								{
									object_loc = 0;
									rMgr.setMemory(CSADDR + loc, object, 1); // 저장
									loc = loc+1;
									//System.out.println(Integer.toHexString(CSADDR) + " " + loc + "*****************************");
									object[0] = 0;
								}
								/*if(opindex == -1){
									object_loc = 0;
									rMgr.setMemory(CSADDR + loc, object, 1); // 저장
									loc = loc+1;
									System.out.println(Integer.toHexString(CSADDR) + " " + loc + "*****************************");
									object[0] = 0;
									opindex = 0;
								}*/
							}
							if(twoform){
								if(object_loc == 2){
									object_loc = 0;
									rMgr.setMemory(CSADDR + loc, object, 2); // 저장
									loc = loc+2;
									//System.out.println(Integer.toHexString(CSADDR) + " " + loc + "*****************************");
									for(int i = 0; i < 2; i++){ // 초기화
										object[i] = 0;
									}
									twoform = false;
								}
							}
							if(extended == 0){ // 3혁식일때
								if(object_loc == 3){
									object_loc = 0;
									rMgr.setMemory(CSADDR + loc, object, 3); // 저장
									loc = loc+3;
									//System.out.println(Integer.toHexString(CSADDR) + " " + loc + "*****************************");
									for(int i = 0; i < 3; i++){ // 초기화
										object[i] = 0;
									}
								}
							}
							else{
								if(object_loc == 4){ // 4형식일때
									object_loc = 0;
									rMgr.setMemory(CSADDR + loc, object, 4); // 저장
									loc = loc+4;
									//System.out.println(Integer.toHexString(CSADDR) + " " + loc + "*****************************");
										for(int i = 0; i < 4; i++){
										object[i] = 0;
									}
								}
							}
						}
							//System.out.println(num + "  " + temp);
					}else if(s.startsWith("M")){
						//System.out.println(s);
						String addr = s.substring(1,7);
						String num = s.substring(7,9);
						String sym = s.substring(10,s.length());
						//System.out.println(addr + " " + num + " " +sym);
						//System.out.print("M :");
						byte[] mem = this.rMgr.getMemory(CSADDR+Integer.parseInt(addr,16), 3);
						//System.out.println(CSADDR+Integer.parseInt(addr,16));
						/*for(int i = 0; i < mem.length ; i ++){
							System.out.println(mem[i]);
						}*/
						int i = 0;
						i += mem[0];
						i = i << 8;
						i += mem[1];
						i = i << 8;
						i += mem[2];
						//System.out.println("=======" + Integer.toHexString(i));
						if(s.charAt(9) == '+')
							i += Integer.parseInt(ESTAB.get(searchEstab(sym,0)).Address,16);
						else if(s.charAt(9) == '-')
							i -= Integer.parseInt(ESTAB.get(searchEstab(sym,0)).Address,16);
						//System.out.println("=======" + Integer.toHexString(i));
	
						mem[2] = (byte) (i & 0xFF);
						i = i >> 8;
						mem[1] = (byte) (i & 0xFF);
						i = i >> 8;
						mem[0] = (byte) (i & 0xFF);
						
						
						this.rMgr.setMemory(CSADDR+Integer.parseInt(addr,16), mem, 3);
					}
				}
			}catch(IOException e){
				System.out.println("There is errer" + e);
			}
		}
	}
}
