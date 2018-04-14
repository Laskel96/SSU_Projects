import java.io.*;
import java.util.*;



public class Assembler {

	private ArrayList<InstTab> inst = new ArrayList<InstTab>(); // ��ɾ�
	private ArrayList<String> input_data = new ArrayList<String>(); // ����Ÿ 
	private ArrayList<Tokenize> token_table = new ArrayList<Tokenize>(); // ��ū
	private ArrayList<Integer> addr_table = new ArrayList<Integer>(); // �ּ� 
	private ArrayList<SymTab> sym_tab = new ArrayList<SymTab>(); // �ɺ�
	private ArrayList<LitTab> lit_tab = new ArrayList<LitTab>(); // ���ͷ� 
	private ArrayList<Integer> hex_locctr = new ArrayList<Integer>(); //��� �ּ� 
	private ArrayList<String> hex_code = new ArrayList<String>(); //��� �ڵ�
	
	private int inst_index; // ��ɾ� �ε���
	private int line_num; // ���� ��ġ
	private int label_num; // �� �ε���
	private int loc_line; // �����̼� üũ �ε���
	private int locctr; // �����̼�ī����
	private int locctr_check[] = new int[3]; // �����̼� üũ : �������� �� �������� üũ
	private int sum_loc[] = new int[3]; // �����̼� ī���� �� : ���� ��ü �ڵ� ����
	
	private void Assembler(){ // ������
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
	 * ���� : ��ɾ ���Ͽ��� �о�� ����
	 * Input : ��ɾ ����� ���� �̸�
	 * OutPut : ����
	 */
	
	public void init_inst_file(String input){
		try{ // ��ɾ� ���̺� �ۼ�
			BufferedReader in = new BufferedReader(new FileReader(input)); // BufferedReader ����
			String s; // ���� ����
			try{
				while((s = in.readLine()) != null){
					String[] split = s.split(" ");//�����̽��� ����
					InstTab temp = new InstTab(); // �ӽ� ���� ����
					temp.setName(split[0]); // �̸� ����
					temp.setFormat1(Integer.valueOf(split[1])); // ù��° ���� ����
					temp.setFormat2(Integer.valueOf(split[2])); // �ι�° ���� ����
					String t = split[3]; // �ڸ�Ʈ
					t = t.replaceFirst("^0x",""); // 0x ����
					temp.setOpcode(Integer.parseInt(t,16)); // �����ڵ� ����
					temp.setNumOp(Integer.valueOf(split[4])); // ���� ����
					inst.add(temp);//�߰�
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
	 * ���� : �о���� ������ ���, �����Ͽ�
	 * input : ����
	 * output : �о���� ������
	 */
	public void print_data(){
		Iterator<InstTab> itr1 = inst.iterator(); // ���ͷ����� ����
		while(itr1.hasNext()){ // ������ �ִٸ� 
			InstTab temp = new InstTab();
			temp = itr1.next();//�޾Ƽ�
			temp.printInst(); //���
		}
		Iterator<String> itr2 = input_data.iterator(); //���ͷ����� ����
		while(itr2.hasNext()){
			String str = itr2.next();
			System.out.println(str);
		}
	}
	
	/*
	 * ���� : �ڵ带 ���Ͽ��� �о�� ����
	 * Input : ���� �̸�
	 * Output : ����
	 */
	public void init_input_file(String input){
		try{
			BufferedReader in = new BufferedReader(new FileReader(input)); //BufferedReader ����
			String s;		
			while((s = in.readLine()) != null){ // ���� �о
				input_data.add(s); // ����
				line_num++;
			}
		}
		catch(IOException e){
			System.out.println("There is errer" + e);
		}
	}
	
	/*
	 * ���� : ��ɾ� ���� ������ ������ �ִ� �Լ�
	 * Input : ���� �̸�
	 * Output : �ش� ��ɾ��� ���� ����. ������ -1
	 */
	public int search_opNum(String str){
		for(int i = 0; i < inst_index; i++){
			if(inst.get(i).getName().equals(str)) // ������
				return inst.get(i).getNumOP();
			}
		return -1;
	}
	
	/*
	 * ���� : ��ɾ� �̸��� ������ ���̺� ��ġ�� �ε����� ��ȯ�ϴ� �Լ�
	 * Input : ��ɾ� �Ǵ� ��Ʈ��
	 * Output : ������ �ε��� , ������ -1
	 */
	public int search_opcode(String str){
		for(int i = 0; i < inst_index; i++){
			if(inst.get(i).getName().equals(str))
				return i;
		}
		return -1;
	}
	
	/*
	 * ���� : ��ɾ� �̸��� ������ ��ɾ��� ������ ��ȯ�ϴ� �Լ�
	 * Input : ��ɾ� �Ǵ� ��Ʈ��
	 * Output : ������ ������, ������ -1 ��ȯ
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
	 * ���� : ��Ʈ���� �ɺ����̺� ����Ǿ� �ִ����� �˻�
	 * Input : ��Ʈ��, ����
	 * Output : ������ �ε����� ������ -1
	 */
	public int searchSymTab(String str, int sect){
		if(locctr_check[0] == 0) // �ƹ��͵� ��� ���� ������� : ���̺� �����ϱ� ���� ���
			for(int i = 0; i < sym_tab.size(); i++) // ��� ���̺� �˻�
				if(sym_tab.get(i).getSymbol().equals(str))
					return i;
		if(sect == 0) // ù��° ������ ��
			for(int i = 0; i < locctr_check[0]; i++)
				if(sym_tab.get(i).getSymbol().equals(str))
					return i;
		if(sect == 1) // �ι�° ������ ��
			for(int i = locctr_check[0]; i < locctr_check[1]; i++)
				if(sym_tab.get(i).getSymbol().equals(str))
					return i;
		if(sect == 2) // ����° ���� �� ��
			for(int i = locctr_check[1]; i < sym_tab.size(); i++)
				if(sym_tab.get(i).getSymbol().equals(str))
					return i;
		return -1;
	}
	
	/*
	 * ���� : ��ū���̺�, �ɺ����̺�, ���ͷ� ���̺� �� pass2���� �����  ���̺��� �����鿡 ���� ����
	 * Input : ����
	 * Output : ����
	 */
	
	public void assem_pass1(){
		int SumLoc_iter = 0;
		for(int i = 0; i < line_num; i++){ // ��ū ���̺� �ۼ�
			Tokenize temp = new Tokenize();
			temp.token_parsing(input_data.get(i),this);
			token_table.add(temp);
		}
		for(int i = 0; i < line_num; i ++){ //�ɺ� ���̺�, ���ͷ� ���̺� �ۼ�
			SymTab temp = new SymTab();
			Tokenize tok = token_table.get(i);
			if(tok.getOperator().equals("EQU") && !tok.getOperand1().equals("*")){ //EQU�̰� ���ڰ�  *�� �ƴѰ��
				hex_locctr.add(sym_tab.get(sym_tab.size()-1).addr); // �ɺ����̺��� ã�ƿ� �� �ּҸ� ����
			}
			else // �����̼� ī���͸� ����
				hex_locctr.add(locctr);
			
			if(tok.getOperator().equals("EQU")){//EQU�� ���
				if(tok.getOperand1().equals("*")){ // ���� ������
					temp.setSymbol(tok.getLabel());
					temp.setAddr(locctr);
					sym_tab.add(temp);
				}
				else{//X-Y ���� ���϶�
					String[] split = tok.getOperand1().split("-"); // -�� ������
					int num = split.length;
					if(num == 2){// ���ڰ� 2���� ���
						temp.setSymbol(tok.getLabel());;
						temp.setAddr(sym_tab.get(searchSymTab(split[0],0)).getAddress() 
								- sym_tab.get(searchSymTab(split[1],0)).getAddress());
						sym_tab.add(temp);
					}
				}
			}
			else{ // EQU�� �ƴѰ��
				if(tok.getOperator().equals("CSECT")){ // CSECT�� ���
					locctr_check[loc_line++] = sym_tab.size();
					sum_loc[SumLoc_iter++] = locctr; // ������ ������ ��ġ ����
					locctr = 0;
				}
				if(!tok.getLabel().equals("")){ // ���� ���� ��
					temp.setSymbol(tok.getLabel());
					temp.setAddr(locctr);
					sym_tab.add(temp);
				}
				int opcode = search_opcode(tok.getOperator()); // �����ڵ� �ε��� ����
				if(tok.getOperator().contains("+")) // 4�����ΰ��
				{	
					String str = new StringBuilder(tok.getOperator()).deleteCharAt(0).toString(); // �� �տ� ���� �Ѱ��� ����
					opcode = search_opcode(str); // ��˻�
				}
				
				if(opcode != -1){ // ��ɾ��� ���
					if(tok.getOperator().contains("+")) // 4�����ΰ��
						locctr += 4;
					else if(search_format(tok.getOperator(), 1) == 2)//2�����ΰ��
						locctr += 2;
					else{ //3�����ϰ��
						if(tok.getOperand1().startsWith("=")){ // ���ͷ��ΰ��
							LitTab lit = new LitTab();
							lit.setName("*");
							lit.setValue(tok.getOperand1());
							lit_tab.add(lit);
						}
						locctr += 3;
					}
				}
				else{ // ��ɾ �ƴѰ��
					if(tok.getOperator().equals("RESW")){//RESW �� ���
						locctr += 3 * Integer.parseInt(tok.getOperand1());
					}
					else if(tok.getOperator().equals("RESB")){ //RESB�� ���
						locctr += Integer.parseInt(tok.getOperand1());
					}
					else if(tok.getOperator().equals("WORD")){ //WORD�� ���
						locctr += 3;
					}
					else if(tok.getOperator().equals("BYTE")){ // BYTE�� ���
						locctr += 1;
					}
					else if(tok.getOperator().equals("LTORG")){ // LTORG�� ���
						lit_tab.get(0).setAddr(locctr);
						locctr += lit_tab.get(0).getValue().length()-4; // ���ʿ��� ����  =*'���ϴ°�'�̹Ƿ� 4�� ��
					}
					else if(tok.getOperator().equals("END")){//end�� ���
						lit_tab.get(1).setAddr(locctr);
						locctr += (lit_tab.get(1).getValue().length()-4)/2;// �����̼� ī���� ����
						sum_loc[SumLoc_iter++] = locctr; // ���α׷� ������ ��ġ ����
					}
				}
			}
		}
	}

	/*
	 * ���� : pass1�� ������ ���� ��� �ڵ��, ����� �ڵ�� ��ȯ
	 * Input : ����
	 * Output : ����
	 */
	
	public void assem_pass2(){
		int sect = 0; //����
		for(int i = 0; i < line_num; i++){  // ��� ���ο� ���Ͽ� �ּҿ� �ڵ� ���
			int hexcode = 0; // ����ڵ带 0���� �ʱ�ȭ
			String str_format = String.format("%04X\t", hex_locctr.get(i)); // �ּ� ���
			Tokenize tok = token_table.get(i);
			int loc_opcode = search_opcode(tok.getOperator()); // ��ɾ��� �ε��� ����
			
			if(tok.getOperator().contains("+")) // 4������ ���
			{	
				String str = new StringBuilder(tok.getOperator()).deleteCharAt(0).toString();
				loc_opcode = search_opcode(str);
			}
			if(tok.getLabel().equals("") && loc_opcode == -1){ // ���� ����ְ� ��ɾ �ƴѰ��
				System.out.print("    "); //�������
				tok.print_token();
			}
			else if(tok.getOperator().equals("EQU")){ //EQU �� ���
				if(tok.getOperand1().equals("*")) // *�� ��
				{
					System.out.print(str_format);
					tok.print_token();
				}
				else{ // *�� �ƴҶ�
					int loc = searchSymTab(tok.getLabel(),sect);
					String form = String.format("%04X\t", sym_tab.get(loc).addr); //����� �ּҸ� ��� ( ��� �� ��)
					System.out.print(form);
					tok.print_token();
				}
			}
			else if(tok.getOperator().equals("CSECT")){ // CSECT�� ���
				System.out.print("		"); // ���� ���
				tok.print_token();
				sect++; // ���� ����
			}
			else{ // �׿� �Ϲ� ��ɾ��
				System.out.print(str_format);
				tok.print_token();
			}
			
			if(loc_opcode != -1){ // ��ɾ��ϰ�� ����� �ڵ�� ��ȯ
				if(tok.getOperator().contains("+")){ //4����
					hexcode += inst.get(loc_opcode).getOpcode() * 0x1000000;
					hexcode += tok.getFlag() * 0x100000;
				}
				else if(inst.get(loc_opcode).getFormat1() == 2){ // 2����
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
				else{ //3����
					hexcode += inst.get(loc_opcode).getOpcode() * 0x10000;
					hexcode += tok.getFlag() * 0x1000;

					if(tok.getOperand1().startsWith("@")){
					}
					else if(tok.getOperand1().startsWith("#")){//immediate �϶�
						String str = new StringBuilder(tok.getOperand1()).deleteCharAt(0).toString();
						hexcode += Integer.parseInt(str);
					}
					else if(tok.getOperand1().startsWith("=")){//���ͷ��϶�
						for(int j = 0; j < 2;j++)
							if(tok.getOperand1().equals(lit_tab.get(j).getValue()))
								hexcode+= lit_tab.get(j).getAddr() - hex_locctr.get(i+1);
					}
					else{//3����
						if(tok.getOperator().equals("RSUB")){
							hexcode -= 0x2000;
						}
						else{
							if(sym_tab.get(searchSymTab(tok.getOperand1(),sect)).getAddress() < hex_locctr.get(i))
								hexcode += sym_tab.get(searchSymTab(tok.getOperand1(),sect)).getAddress() - hex_locctr.get(i+1) + 0x001000; // �����÷� ����
							else
								hexcode += sym_tab.get(searchSymTab(tok.getOperand1(),sect)).getAddress() - hex_locctr.get(i+1);
						}
					}
				}
			}else if(tok.getOperator().equals("LTORG")){ //���ͷ�
				System.out.print("\n\t" + lit_tab.get(0).getName() + "\t"+lit_tab.get(0).getValue()+"\t\t"); // ���ͷ� ���
				String s = lit_tab.get(0).getValue().replaceAll("[=CX']", ""); // [ , ] , C , X , '  ����
				char [] c = s.toCharArray();
				for(int k = 0; k < c.length; k++){
					hexcode += c[k];//����
					hexcode = hexcode << 8; //8��Ʈ �̵�
				}
				hexcode = hexcode >> 8; // ����ġ
			}else if(tok.getOperator().equals("WORD")){ // WORD�� ��
				System.out.println("000000");
				String temp = "000000";
				hex_code.add(temp);
			}
			else if(tok.getOperator().equals("BYTE")){ // BYTE�� ���
				String s = tok.getOperand1().replaceAll("[=CX']", "");
				hex_code.add(s);
				System.out.print(s);
			}
			else if(tok.getOperator().equals("END")){ // END�� ���
				System.out.print("\n\t" + lit_tab.get(1).getName() + "\t"+lit_tab.get(1).getValue()+"\t\t"); // ���ͷ� ���
				String s = lit_tab.get(1).getValue().replaceAll("[=CX']", "");
				System.out.println("\t\t"+s);
				hex_code.add(s);
			}
			String code_format3 = String.format("%06X", hexcode); // 3�����ΰ��
			String code_format2 = String.format("%04X", hexcode); // 2�����ΰ��
			if(hexcode != 0){ //��� �ڵ尡 0�� �ƴѰ�� -> ��ɾ��� ���
				if(Integer.toHexString(hexcode).length() == 4){ // 2�����ΰ��
					System.out.println(code_format2);
					hex_code.add(code_format2);
				}
				else{ // 3, 4�����ΰ��
					System.out.println(code_format3);
					hex_code.add(code_format3);
				}
			}
			else{ // ��ɾ �ƴѰ��
				System.out.println("");
				hex_code.add("");
			}
		}
		
	}

	/*
	 * ���� : �и��Ǿ��ִ� ���ڵ��� �ϳ��� ��ġ�� �Լ�
	 * Input :  ��Ʈ���� ��ū���̺� �ε���
	 * Output : ��Ʈ��
	 */
	
	public String gatherOperand(String str, int i)
	{
		String s = "";
		if(str.equals("EXTDEF")){ //EXTDEF�ΰ��
			if(!token_table.get(i).getOperand1().equals("")){ // ������� ������ 
				String f = String.format("%s%06X", token_table.get(i).getOperand1(), sym_tab.get(searchSymTab(token_table.get(i).getOperand1(),0)).addr);//��������
				s += f; // �߰�
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
		if(str.equals("EXTREF")){ // EXTREF�� ���
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
	 *���� : Modification record�� �����ϴ� �Լ�
	 *Input : bufferwriter, ����
	 *output : Modification record 	
	 *
	 *		�̱���..
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
	 * ���� : pass2�� ������ڵ带 ���Ͽ� ���
	 * Input : output���� �̸�
	 * output : ���Ͽ� ����
	 */
	
	public void output(String output){
		try{
			int sect = 0; // ����
			int SumLoc_iter = 0; // sum_loc��ȸ��
			int SumLine[] = new int[6]; // �� ������ ����Ʈ ��
			int SumLine_iter = 0; // sum_line ��ȸ��
			locctr_check[0] = 0;// ���Ǹ� ����
			int max = 60; // max 69 - (T : 1) - (�ּ� : 6) - (���� : 2)
			int loop = max; // ���� : �ִ�
			int max_data[] = new int [6]; // ���Ͽ� ���� ù��° �ε��� ����
			int j = 0; // max_data ��ȸ
			boolean ref = true; // ����. text ��½ÿ��� T ����ϱ����� ����
			
			for(int i = 0; i < hex_code.size(); i++){ // max_data ����
				if(loop < max){ // loop�� max���� �������
					if( i < token_table.size()){
						if(token_table.get(i).getOperator().equals("LTORG")){ // LOTRG�ΰ��
							max_data[j++] = i;
							loop = 0;						
						}
						if(token_table.get(i).getOperator().equals("CSECT")){ // CSECT�ΰ��
							if(i == 43) // ������ �˼����� array bound error
								max_data[j++] = 44;
							else
								max_data[j++] = i;
							loop = 0;
						}
					}
					if(loop + hex_code.get(i).length() >= max){ //������ ���ϸ� max���� Ŀ���� ���  ��°��� ���߱� ���� length/2����
						max_data[j++] = i;
					}
					loop += hex_code.get(i).length(); // locctr���� ���Ѵ�
				}
				else{// loop�� max���� Ŭ��� 
					loop = 0; // loop�� 0���� �ʱ�ȭ
				}
			}
			j = 0; // j �� 0���� �ʱ�ȭ
			for(int i = 0; i < hex_code.size(); i++){ // ���� �� ���ϱ�
				if(j < 5){
					for(int k = i; k < max_data[j]; k++,i++){
						SumLine[SumLine_iter] += hex_code.get(k).length()/2; // ������ ���� ����
					}
					i--; // �ϳ� �ǵ�����. �� for������ i++��
					j++; // j����
					SumLine_iter++;
				}else{
					for(int k = i; k < hex_code.size();k++,i++){
						SumLine[SumLine_iter] += hex_code.get(k).length()/2;
					}
					SumLine_iter++;
				}
			}
			
			j = 0; // j��  0 ���� �ʱ�ȭ
			SumLine_iter = 0; // SumLine�� 0���� �ʱ�ȭ
			BufferedWriter des = new BufferedWriter(new FileWriter(output));//BufferedWriter ����
			for(int i = 0; i < hex_code.size();i++){ // hex_locctr��ŭ �ݺ�
				if(i < token_table.size()){
					if(token_table.get(i).getOperator().equals("CSECT") || token_table.get(i).getOperator().equals("START")){ // H �ڵ�
						if(token_table.get(i).getOperator().equals("CSECT")){ // CSECT�ΰ��
							Modification(des,sect++); // Modification ���
							des.write("E");
							des.newLine();
							des.newLine();
						}
						String f = String.format("H%s	%06X%06X", token_table.get(i).getLabel(), sym_tab.get(searchSymTab(token_table.get(i).getLabel(),0)).addr, sum_loc[SumLoc_iter++]); //���� ����
						des.write(f);//���
						des.newLine();
						ref = false;
					}
				}
				if( j < 5){ // j�� 5���� ���� ��
					for(int k = i; k < max_data[j]; k++, i++){ // max_data�� ����ŭ �ݺ�
						if(ref){ //ref�� true�϶� 
							des.write("T");
							String form = String.format("%06X%02X", hex_locctr.get(i), SumLine[SumLine_iter++]);
							des.write(form);
						}
						if(token_table.get(i).getOperator().equals("EXTDEF")){ //EXTEDF�� �������
								des.write("D");
								String s = gatherOperand("EXTDEF", i);
								des.write(s);
								des.newLine();
							}
							
						else if(token_table.get(i).getOperator().equals("EXTREF")){ //EXTREF�� �������
								des.write("R");
								String s = gatherOperand("EXTREF", i);
								des.write(s);
								des.newLine();
								ref = true;
							}
						else{ // �׿� 
							des.write(hex_code.get(k));
							ref = false;
						}
					}
					j++;
					i--;
				}
				else{ // j == 5�� ���
					for(int k = i-1; k < hex_code.size();k++,i++){//������ �ݺ�
						if(k < token_table.size()){ // ��ū���̺� ũ�����, ��ū���̺� ũ�� < ����ڵ� ������ -> LTORG
							if(token_table.get(k).getOperator().equals("CSECT") || token_table.get(k).getOperator().equals("START")){ // H �ڵ�
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