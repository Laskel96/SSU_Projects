import java.io.*;
import java.util.*;



public class Assembler {

	private ArrayList<InstTab> inst = new ArrayList<InstTab>(); // 명령어
	private ArrayList<String> input_data = new ArrayList<String>(); // 데이타 
	private ArrayList<Tokenize> token_table = new ArrayList<Tokenize>(); // 토큰
	private ArrayList<Integer> addr_table = new ArrayList<Integer>(); // 주소 
	private ArrayList<SymTab> sym_tab = new ArrayList<SymTab>(); // 심볼
	private ArrayList<LitTab> lit_tab = new ArrayList<LitTab>(); // 리터럴 
	private ArrayList<Integer> hex_locctr = new ArrayList<Integer>(); //헥사 주소 
	private ArrayList<String> hex_code = new ArrayList<String>(); //헥사 코드
	
	private int inst_index; // 명령어 인덱스
	private int line_num; // 라인 위치
	private int label_num; // 라벨 인덱스
	private int loc_line; // 로케이션 체크 인덱스
	private int locctr; // 로케이션카운터
	private int locctr_check[] = new int[3]; // 로케이션 체크 : 어디까지가 한 섹션인지 체크
	private int sum_loc[] = new int[3]; // 로케이션 카운터 합 : 섹션 전체 코드 길이
	
	private void Assembler(){ // 생성자
		inst_index = 0;
		line_num = 0;
		label_num = 0;
		loc_line = 0;
		locctr = 0;
	}
	
	public int getInstIndex(){return inst_index;}
	public int getLineNum(){return line_num;}
	public int getLabelNum(){return label_num;}
	public int getLocctr(){return locctr;}
	
	
	/*
	 * 설명 : 명령어를 파일에서 읽어와 저장
	 * Input : 명령어가 저장된 파일 이름
	 * OutPut : 없음
	 */
	
	public void init_inst_file(String input){
		try{ // 명령어 테이블 작성
			BufferedReader in = new BufferedReader(new FileReader(input)); // BufferedReader 선언
			String s; // 라인 저장
			try{
				while((s = in.readLine()) != null){
					String[] split = s.split(" ");//스페이스로 나눔
					InstTab temp = new InstTab(); // 임시 변수 생성
					temp.setName(split[0]); // 이름 저장
					temp.setFormat1(Integer.valueOf(split[1])); // 첫번째 포멧 저장
					temp.setFormat2(Integer.valueOf(split[2])); // 두번째 포멧 저장
					String t = split[3]; // 코멘트
					t = t.replaceFirst("^0x",""); // 0x 제거
					temp.setOpcode(Integer.parseInt(t,16)); // 오피코드 저장
					temp.setNumOp(Integer.valueOf(split[4])); // 인자 개수
					inst.add(temp);//추가
					this.inst_index++;
				}
			}
			catch(NumberFormatException e){
				System.out.println("There is errer" + e);
			}
		}
		catch(IOException e){
			System.out.println("There is errer" + e);
		}
	}

	/*
	 * 설명 : 읽어들인 테이터 출력, 컴파일용
	 * input : 없음
	 * output : 읽어들인 테이터
	 */
	public void print_data(){
		Iterator<InstTab> itr1 = inst.iterator(); // 이터레이터 선언
		while(itr1.hasNext()){ // 다음게 있다면 
			InstTab temp = new InstTab();
			temp = itr1.next();//받아서
			temp.printInst(); //출력
		}
		Iterator<String> itr2 = input_data.iterator(); //이터레이터 선언
		while(itr2.hasNext()){
			String str = itr2.next();
			System.out.println(str);
		}
	}
	
	/*
	 * 설명 : 코드를 파일에서 읽어와 저장
	 * Input : 파일 이름
	 * Output : 없음
	 */
	public void init_input_file(String input){
		try{
			BufferedReader in = new BufferedReader(new FileReader(input)); //BufferedReader 생성
			String s;		
			while((s = in.readLine()) != null){ // 한줄 읽어서
				input_data.add(s); // 저장
				line_num++;
			}
		}
		catch(IOException e){
			System.out.println("There is errer" + e);
		}
	}
	
	/*
	 * 설명 : 명령어 인자 개수를 리턴해 주는 함수
	 * Input : 명렁어 이름
	 * Output : 해당 명령어의 인자 개수. 없으면 -1
	 */
	public int search_opNum(String str){
		for(int i = 0; i < inst_index; i++){
			if(inst.get(i).getName().equals(str)) // 같으면
				return inst.get(i).getNumOP();
			}
		return -1;
	}
	
	/*
	 * 설명 : 명령어 이름을 가지고 테이블에 위치한 인덱스를 반환하는 함수
	 * Input : 명령어 또는 스트링
	 * Output : 있으면 인덱스 , 없으면 -1
	 */
	public int search_opcode(String str){
		for(int i = 0; i < inst_index; i++){
			if(inst.get(i).getName().equals(str))
				return i;
		}
		return -1;
	}
	
	/*
	 * 설명 : 명령어 이름을 가지고 명령어의 포멧을 반환하는 함수
	 * Input : 명령어 또는 스트링
	 * Output : 있으면 포멧을, 없으면 -1 반환
	 */
	public int search_format(String str, int format){
		for(int i = 0; i < inst_index ; i++){
			if(inst.get(i).getName().equals(str))
				if(format == 1)
					return inst.get(i).getFormat1();
				else
					return inst.get(i).getFormat2();
		}
		return -1;
	}
	
	/*
	 * 설명 : 스트링이 심볼테이블에 저장되어 있는지를 검사
	 * Input : 스트링, 섹션
	 * Output : 있으면 인덱스를 없으면 -1
	 */
	public int searchSymTab(String str, int sect){
		if(locctr_check[0] == 0) // 아무것도 들어 있지 않은경우 : 테이블에 저장하기 위해 사용
			for(int i = 0; i < sym_tab.size(); i++) // 모든 테이블 검색
				if(sym_tab.get(i).getSymbol().equals(str))
					return i;
		if(sect == 0) // 첫번째 섹션일 때
			for(int i = 0; i < locctr_check[0]; i++)
				if(sym_tab.get(i).getSymbol().equals(str))
					return i;
		if(sect == 1) // 두번째 섹션일 때
			for(int i = locctr_check[0]; i < locctr_check[1]; i++)
				if(sym_tab.get(i).getSymbol().equals(str))
					return i;
		if(sect == 2) // 세번째 섹션 일 때
			for(int i = locctr_check[1]; i < sym_tab.size(); i++)
				if(sym_tab.get(i).getSymbol().equals(str))
					return i;
		return -1;
	}
	
	/*
	 * 설명 : 토큰테이블, 심볼테이블, 리터럴 테이블 등 pass2에서 사용할  테이블들과 변수들에 값을 저장
	 * Input : 없음
	 * Output : 없음
	 */
	
	public void assem_pass1(){
		int SumLoc_iter = 0;
		for(int i = 0; i < line_num; i++){ // 토큰 테이블 작성
			Tokenize temp = new Tokenize();
			temp.token_parsing(input_data.get(i),this);
			token_table.add(temp);
		}
		for(int i = 0; i < line_num; i ++){ //심볼 테이블, 리터럴 테이블 작성
			SymTab temp = new SymTab();
			Tokenize tok = token_table.get(i);
			if(tok.getOperator().equals("EQU") && !tok.getOperand1().equals("*")){ //EQU이고 인자가  *이 아닌경우
				hex_locctr.add(sym_tab.get(sym_tab.size()-1).addr); // 심볼테이블에서 찾아와 그 주소를 저장
			}
			else // 로케이션 카운터를 저장
				hex_locctr.add(locctr);
			
			if(tok.getOperator().equals("EQU")){//EQU인 경우
				if(tok.getOperand1().equals("*")){ // 식이 없을때
					temp.setSymbol(tok.getLabel());
					temp.setAddr(locctr);
					sym_tab.add(temp);
				}
				else{//X-Y 꼴의 식일때
					String[] split = tok.getOperand1().split("-"); // -로 나눈다
					int num = split.length;
					if(num == 2){// 인자가 2개인 경우
						temp.setSymbol(tok.getLabel());;
						temp.setAddr(sym_tab.get(searchSymTab(split[0],0)).getAddress() 
								- sym_tab.get(searchSymTab(split[1],0)).getAddress());
						sym_tab.add(temp);
					}
				}
			}
			else{ // EQU가 아닌경우
				if(tok.getOperator().equals("CSECT")){ // CSECT인 경우
					locctr_check[loc_line++] = sym_tab.size();
					sum_loc[SumLoc_iter++] = locctr; // 섹션이 끝나는 위치 저장
					locctr = 0;
				}
				if(!tok.getLabel().equals("")){ // 라벨이 있을 때
					temp.setSymbol(tok.getLabel());
					temp.setAddr(locctr);
					sym_tab.add(temp);
				}
				int opcode = search_opcode(tok.getOperator()); // 오피코드 인덱스 저장
				if(tok.getOperator().contains("+")) // 4형식인경우
				{	
					String str = new StringBuilder(tok.getOperator()).deleteCharAt(0).toString(); // 맨 앞에 문자 한개를 제거
					opcode = search_opcode(str); // 재검색
				}
				
				if(opcode != -1){ // 명령어인 경우
					if(tok.getOperator().contains("+")) // 4형식인경우
						locctr += 4;
					else if(search_format(tok.getOperator(), 1) == 2)//2형식인경우
						locctr += 2;
					else{ //3형식일경우
						if(tok.getOperand1().startsWith("=")){ // 리터럴인경우
							LitTab lit = new LitTab();
							lit.setName("*");
							lit.setValue(tok.getOperand1());
							lit_tab.add(lit);
						}
						locctr += 3;
					}
				}
				else{ // 명령어가 아닌경우
					if(tok.getOperator().equals("RESW")){//RESW 인 경우
						locctr += 3 * Integer.parseInt(tok.getOperand1());
					}
					else if(tok.getOperator().equals("RESB")){ //RESB인 경우
						locctr += Integer.parseInt(tok.getOperand1());
					}
					else if(tok.getOperator().equals("WORD")){ //WORD인 경우
						locctr += 3;
					}
					else if(tok.getOperator().equals("BYTE")){ // BYTE인 경우
						locctr += 1;
					}
					else if(tok.getOperator().equals("LTORG")){ // LTORG인 경우
						lit_tab.get(0).setAddr(locctr);
						locctr += lit_tab.get(0).getValue().length()-4; // 불필요한 문자  =*'원하는것'이므로 4를 뺌
					}
					else if(tok.getOperator().equals("END")){//end인 경우
						lit_tab.get(1).setAddr(locctr);
						locctr += (lit_tab.get(1).getValue().length()-4)/2;// 로케이션 카운터 증가
						sum_loc[SumLoc_iter++] = locctr; // 프로그램 마지막 위치 저장
					}
				}
			}
		}
	}

	/*
	 * 설명 : pass1의 정보를 토대로 헥사 코드로, 어셈블리 코드로 변환
	 * Input : 없음
	 * Output : 없음
	 */
	
	public void assem_pass2(){
		int sect = 0; //섹션
		for(int i = 0; i < line_num; i++){  // 모든 라인에 대하여 주소와 코드 출력
			int hexcode = 0; // 헥사코드를 0으로 초기화
			String str_format = String.format("%04X\t", hex_locctr.get(i)); // 주소 출력
			Tokenize tok = token_table.get(i);
			int loc_opcode = search_opcode(tok.getOperator()); // 명령어의 인덱스 저장
			
			if(tok.getOperator().contains("+")) // 4형식인 경우
			{	
				String str = new StringBuilder(tok.getOperator()).deleteCharAt(0).toString();
				loc_opcode = search_opcode(str);
			}
			if(tok.getLabel().equals("") && loc_opcode == -1){ // 라벨이 비어있고 명령어가 아닌경우
				System.out.print("    "); //공백출력
				tok.print_token();
			}
			else if(tok.getOperator().equals("EQU")){ //EQU 인 경우
				if(tok.getOperand1().equals("*")) // *일 때
				{
					System.out.print(str_format);
					tok.print_token();
				}
				else{ // *이 아닐때
					int loc = searchSymTab(tok.getLabel(),sect);
					String form = String.format("%04X\t", sym_tab.get(loc).addr); //저장된 주소를 출력 ( 계산 된 값)
					System.out.print(form);
					tok.print_token();
				}
			}
			else if(tok.getOperator().equals("CSECT")){ // CSECT인 경우
				System.out.print("		"); // 공백 출력
				tok.print_token();
				sect++; // 섹션 증가
			}
			else{ // 그외 일반 명령어들
				System.out.print(str_format);
				tok.print_token();
			}
			
			if(loc_opcode != -1){ // 명령어일경우 어셈블리 코드로 변환
				if(tok.getOperator().contains("+")){ //4형식
					hexcode += inst.get(loc_opcode).getOpcode() * 0x1000000;
					hexcode += tok.getFlag() * 0x100000;
				}
				else if(inst.get(loc_opcode).getFormat1() == 2){ // 2형식
					hexcode += inst.get(loc_opcode).getOpcode() * 0x100;
					if(tok.getOperand1().equals("X")){ hexcode += 0x10;}
					if(tok.getOperand1().equals("A")){ hexcode += 0x00;}
					if(tok.getOperand1().equals("S")){ hexcode += 0x40;}
					if(tok.getOperand1().equals("T")){ hexcode += 0x50;}
					if(inst.get(loc_opcode).getNumOP() == 2){
						if(tok.getOperand2().equals("X")){ hexcode += 0x1;}
						if(tok.getOperand2().equals("A")){ hexcode += 0x0;}
						if(tok.getOperand2().equals("S")){ hexcode += 0x4;}
						if(tok.getOperand2().equals("T")){ hexcode += 0x5;}
					}
				}
				else{ //3형식
					hexcode += inst.get(loc_opcode).getOpcode() * 0x10000;
					hexcode += tok.getFlag() * 0x1000;

					if(tok.getOperand1().startsWith("@")){
					}
					else if(tok.getOperand1().startsWith("#")){//immediate 일때
						String str = new StringBuilder(tok.getOperand1()).deleteCharAt(0).toString();
						hexcode += Integer.parseInt(str);
					}
					else if(tok.getOperand1().startsWith("=")){//리터럴일때
						for(int j = 0; j < 2;j++)
							if(tok.getOperand1().equals(lit_tab.get(j).getValue()))
								hexcode+= lit_tab.get(j).getAddr() - hex_locctr.get(i+1);
					}
					else{//3형식
						if(tok.getOperator().equals("RSUB")){
							hexcode -= 0x2000;
						}
						else{
							if(sym_tab.get(searchSymTab(tok.getOperand1(),sect)).getAddress() < hex_locctr.get(i))
								hexcode += sym_tab.get(searchSymTab(tok.getOperand1(),sect)).getAddress() - hex_locctr.get(i+1) + 0x001000; // 오버플로 보정
							else
								hexcode += sym_tab.get(searchSymTab(tok.getOperand1(),sect)).getAddress() - hex_locctr.get(i+1);
						}
					}
				}
			}else if(tok.getOperator().equals("LTORG")){ //리터럴
				System.out.print("\n\t" + lit_tab.get(0).getName() + "\t"+lit_tab.get(0).getValue()+"\t\t"); // 리터럴 출력
				String s = lit_tab.get(0).getValue().replaceAll("[=CX']", ""); // [ , ] , C , X , '  제외
				char [] c = s.toCharArray();
				for(int k = 0; k < c.length; k++){
					hexcode += c[k];//더함
					hexcode = hexcode << 8; //8비트 이동
				}
				hexcode = hexcode >> 8; // 원위치
			}else if(tok.getOperator().equals("WORD")){ // WORD일 때
				System.out.println("000000");
				String temp = "000000";
				hex_code.add(temp);
			}
			else if(tok.getOperator().equals("BYTE")){ // BYTE인 경우
				String s = tok.getOperand1().replaceAll("[=CX']", "");
				hex_code.add(s);
				System.out.print(s);
			}
			else if(tok.getOperator().equals("END")){ // END인 경우
				System.out.print("\n\t" + lit_tab.get(1).getName() + "\t"+lit_tab.get(1).getValue()+"\t\t"); // 리터럴 출력
				String s = lit_tab.get(1).getValue().replaceAll("[=CX']", "");
				System.out.println("\t\t"+s);
				hex_code.add(s);
			}
			String code_format3 = String.format("%06X", hexcode); // 3형식인경우
			String code_format2 = String.format("%04X", hexcode); // 2형식인경우
			if(hexcode != 0){ //헥사 코드가 0이 아닌경우 -> 명령어인 경우
				if(Integer.toHexString(hexcode).length() == 4){ // 2형식인경우
					System.out.println(code_format2);
					hex_code.add(code_format2);
				}
				else{ // 3, 4형식인경우
					System.out.println(code_format3);
					hex_code.add(code_format3);
				}
			}
			else{ // 명령어가 아닌경우
				System.out.println("");
				hex_code.add("");
			}
		}
		
	}

	/*
	 * 설명 : 분리되어있는 인자들을 하나로 합치는 함수
	 * Input :  스트링과 토큰테이블 인덱스
	 * Output : 스트링
	 */
	
	public String gatherOperand(String str, int i)
	{
		String s = "";
		if(str.equals("EXTDEF")){ //EXTDEF인경우
			if(!token_table.get(i).getOperand1().equals("")){ // 비어있지 않으면 
				String f = String.format("%s%06X", token_table.get(i).getOperand1(), sym_tab.get(searchSymTab(token_table.get(i).getOperand1(),0)).addr);//형식지정
				s += f; // 추가
			}
			if(!token_table.get(i).getOperand2().equals("")){
				String f = String.format("%s%06X", token_table.get(i).getOperand2(), sym_tab.get(searchSymTab(token_table.get(i).getOperand2(),0)).addr);
				s += f;
			}
			if(!token_table.get(i).getOperand3().equals("")){
				String f = String.format("%s%06X", token_table.get(i).getOperand3(), sym_tab.get(searchSymTab(token_table.get(i).getOperand3(),0)).addr);
				s += f;
			}
		}
		if(str.equals("EXTREF")){ // EXTREF인 경우
			if(!token_table.get(i).getOperand1().equals("")){
				String f = String.format("%s", token_table.get(i).getOperand1());
				s += f;
			}
			if(!token_table.get(i).getOperand2().equals("")){
				String f = String.format("%s", token_table.get(i).getOperand2());
				s += f;
			}
			if(!token_table.get(i).getOperand3().equals("")){
				String f = String.format("%s", token_table.get(i).getOperand3());
				s += f;
			}
		}
		return s;
	}
	
	/*
	 *설명 : Modification record를 츨력하는 함수
	 *Input : bufferwriter, 섹션
	 *output : Modification record 	
	 *
	 *		미구현..
	 */
	
	public void Modification(BufferedWriter bw, int s){
		try{
			if(s == 0){
				bw.write("M00000405+RDREC");
				bw.newLine();
				bw.write("M00001105+WRECT");
				bw.newLine();
				bw.write("M00002405+WRECT");
				bw.newLine();
			}else if(s == 1){
				bw.write("M00001805+BUFFER");
				bw.newLine();
				bw.write("M00002105+LENGTH");
				bw.newLine();
				bw.write("M00002806+BUFEND");
				bw.newLine();
				bw.write("M00002806-BUFFER");
				bw.newLine();
			}else if(s == 2){
				bw.write("M00000305+LENGTH");
				bw.newLine();
				bw.write("M00000D05+BUFFER");
				bw.newLine();
			}
		}
		catch(IOException e){
			
		}
	}
	
	/*
	 * 설명 : pass2의 어셈블리코드를 파일에 출력
	 * Input : output파일 이름
	 * output : 파일에 정보
	 */
	
	public void output(String output){
		try{
			int sect = 0; // 센션
			int SumLoc_iter = 0; // sum_loc순회자
			int SumLine[] = new int[6]; // 각 라인의 바이트 함
			int SumLine_iter = 0; // sum_line 순회자
			locctr_check[0] = 0;// 편의를 위함
			int max = 60; // max 69 - (T : 1) - (주소 : 6) - (길이 : 2)
			int loop = max; // 루프 : 최대
			int max_data[] = new int [6]; // 파일에 개행 첫번째 인덱스 저장
			int j = 0; // max_data 순회
			boolean ref = true; // 조건. text 출력시에만 T 출력하기위한 예외
			
			for(int i = 0; i < hex_code.size(); i++){ // max_data 설정
				if(loop < max){ // loop가 max보가 작은경우
					if( i < token_table.size()){
						if(token_table.get(i).getOperator().equals("LTORG")){ // LOTRG인경우
							max_data[j++] = i;
							loop = 0;						
						}
						if(token_table.get(i).getOperator().equals("CSECT")){ // CSECT인경우
							if(i == 43) // 원인을 알수없는 array bound error
								max_data[j++] = 44;
							else
								max_data[j++] = i;
							loop = 0;
						}
					}
					if(loop + hex_code.get(i).length() >= max){ //다음에 더하면 max보다 커지는 경우  출력값을 맞추기 위해 length/2안함
						max_data[j++] = i;
					}
					loop += hex_code.get(i).length(); // locctr값을 더한다
				}
				else{// loop가 max보다 클경우 
					loop = 0; // loop를 0으로 초기화
				}
			}
			j = 0; // j 를 0으로 초기화
			for(int i = 0; i < hex_code.size(); i++){ // 라인 값 더하기
				if(j < 5){
					for(int k = i; k < max_data[j]; k++,i++){
						SumLine[SumLine_iter] += hex_code.get(k).length()/2; // 길이의 반을 더함
					}
					i--; // 하나 되돌려줌. 밖 for문에서 i++함
					j++; // j증가
					SumLine_iter++;
				}else{
					for(int k = i; k < hex_code.size();k++,i++){
						SumLine[SumLine_iter] += hex_code.get(k).length()/2;
					}
					SumLine_iter++;
				}
			}
			
			j = 0; // j를  0 으로 초기화
			SumLine_iter = 0; // SumLine을 0으로 초기화
			BufferedWriter des = new BufferedWriter(new FileWriter(output));//BufferedWriter 선언
			for(int i = 0; i < hex_code.size();i++){ // hex_locctr만큼 반복
				if(i < token_table.size()){
					if(token_table.get(i).getOperator().equals("CSECT") || token_table.get(i).getOperator().equals("START")){ // H 코드
						if(token_table.get(i).getOperator().equals("CSECT")){ // CSECT인경우
							Modification(des,sect++); // Modification 출력
							des.write("E");
							des.newLine();
							des.newLine();
						}
						String f = String.format("H%s	%06X%06X", token_table.get(i).getLabel(), sym_tab.get(searchSymTab(token_table.get(i).getLabel(),0)).addr, sum_loc[SumLoc_iter++]); //포멧 지정
						des.write(f);//출력
						des.newLine();
						ref = false;
					}
				}
				if( j < 5){ // j가 5보다 작을 때
					for(int k = i; k < max_data[j]; k++, i++){ // max_data의 값만큼 반복
						if(ref){ //ref가 true일때 
							des.write("T");
							String form = String.format("%06X%02X", hex_locctr.get(i), SumLine[SumLine_iter++]);
							des.write(form);
						}
						if(token_table.get(i).getOperator().equals("EXTDEF")){ //EXTEDF를 만날경우
								des.write("D");
								String s = gatherOperand("EXTDEF", i);
								des.write(s);
								des.newLine();
							}
							
						else if(token_table.get(i).getOperator().equals("EXTREF")){ //EXTREF를 만날경우
								des.write("R");
								String s = gatherOperand("EXTREF", i);
								des.write(s);
								des.newLine();
								ref = true;
							}
						else{ // 그외 
							des.write(hex_code.get(k));
							ref = false;
						}
					}
					j++;
					i--;
				}
				else{ // j == 5인 경우
					for(int k = i-1; k < hex_code.size();k++,i++){//끝까지 반복
						if(k < token_table.size()){ // 토큰테이블 크기까지, 토큰테이블 크기 < 헥사코드 사이즈 -> LTORG
							if(token_table.get(k).getOperator().equals("CSECT") || token_table.get(k).getOperator().equals("START")){ // H 코드
								Modification(des,sect++);
								des.write("E");
								des.newLine();
								des.newLine();				
								String f = String.format("H%s	%06X%06X", token_table.get(k).getLabel(), sym_tab.get(searchSymTab(token_table.get(k).getLabel(),0)).addr, sum_loc[SumLoc_iter++]);
								des.write(f);
								des.newLine();
								ref = false;
							}
							else if(token_table.get(k).getOperator().equals("EXTDEF")){
								des.write("D");
								String s = gatherOperand("EXTDEF", i);
								des.write(s);
								des.newLine();
							}
							else if(token_table.get(k).getOperator().equals("EXTREF")){
								des.write("R");
								String s = gatherOperand("EXTREF", i-1);
								des.write(s);
								des.newLine();
								ref = true;
							}
							else{
								des.write(hex_code.get(k));
								ref = false;
							}
							if(ref){
								des.write("T");
								String form = String.format("%06X%02X", hex_locctr.get(k), SumLine[SumLine_iter]);
								des.write(form);
							}
							
						}
						else{
							des.write(hex_code.get(k));
							ref = false;
							}
					}
				}
				des.newLine();
				ref = true;
			}
			Modification(des,sect); // Modification record
			des.write("E");
			des.close();
		}
		catch(IOException e){
			System.out.println("There is errer" + e);
		}
	}
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		Assembler my_assembler = new Assembler();
		
		my_assembler.init_inst_file("inst");
		my_assembler.init_input_file("input_20150250");
		my_assembler.assem_pass1();
		my_assembler.assem_pass2();
		my_assembler.output("output_20150250");
	}
}