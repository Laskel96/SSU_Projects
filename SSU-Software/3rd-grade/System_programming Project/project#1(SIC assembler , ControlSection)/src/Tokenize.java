
public class Tokenize {
	private String label;
	private String operator;
	private String operand1;
	private String operand2;
	private String operand3;
	private String comment;
	private char flag;
	
	public Tokenize(){
		label = "";
		operator = "";
		operand1 = "";
		operand2 = "";
		operand3 = "";
		comment = "";
		flag = '\0';
	}
	
	public String getLabel(){return label;}
	public String getOperator(){return operator;}
	public String getOperand1(){return operand1;}
	public String getOperand2(){return operand2;}
	public String getOperand3(){return operand3;}
	public String getComment(){return comment;}
	public char getFlag(){return flag;}
	
	public void setLabel(String str){label = str;}
	public void setOperator(String str){operator = str;}
	public void setOperand1(String str){operand1 = str;}
	public void setOperand2(String str){operand2 = str;}
	public void setOperand3(String str){operand3 = str;}
	public void setComment(String str){comment = str;}
	public void setFlag(char c){flag = c;}
	
	public void print_token(){
		if(!comment.equals("")){
			System.out.print(comment+"\t");
		}
		System.out.print(label+"\t");
		System.out.print(operator+"\t");
		System.out.print(operand1+"\t");
		System.out.print(operand2+"\t");
		System.out.print(operand3+"\t");
	}

	/*
	 * ���� : ��ɾ� ���ڵ��� ����
	 * Input : ��ɾ� ���� �迭
	 * Output ����
	 */
	protected void store_operand(String[] s, Assembler A){ // store operand
		int opNum;
		opNum = A.search_opNum(operator);
		if(opNum != -1){//��ɾ��� ���
			if(opNum == 1){ // ��ɾ 1���ΰ��
				operand1 = s[2];
			}
			else if(opNum == 2){ // ��ɾ 2���ΰ��
				String[] sub_split = s[2].split(",");
				operand1 = sub_split[0];
				operand2 = sub_split[1];
			}
		}
		else{//��ɾ �ƴѰ��
			char[] str = s[2].toCharArray(); // str�� char �迭��
			int num = 0;
			String[] sub_split = s[2].split(","); // ,�� ����
			num = sub_split.length;
			if(num == 1){//���� 1��
				operand1 = sub_split[0];
			}				
			else if(num ==2){//���� 2��
				operand1 = sub_split[0];
				operand2 = sub_split[1];
			}
			else if(num ==3){//���� 3��
				operand1 = sub_split[0];
				operand2 = sub_split[1];					
				operand3 = sub_split[2];
			}
		}
	}
	
	/*
	 * ���� : ��� ��ɾ��� �� ���ڸ��� ���� opcode�κ�
	 * Input : ����
	 * Ouutput : ����
	 */
	protected void cal_flag(){
		if(operand1.contains("@")){ flag += 32; }//indirect
		else if(operand1.contains("#")){ flag += 16; }//immediate
		else flag += 48; //XE simple
		if(operand1.startsWith("X")||operand2.startsWith("X")||operand3.startsWith("X"))
		{flag += 8;} //use X register
		
		if(operator.contains("+")){flag+=1;} //extended
		else if(!operand1.contains("#")){flag+=2;}//no base relative
	}

	/*
	 * ���� : ��ū�� ������ �����ϴ� �Լ�
	 * Input : �Ľ��� ��Ʈ��
	 * Output : ����
	 */
	
	public void token_parsing(String str, Assembler A){
		if(!str.contains(".")){ //�ּ� ������ �ƴҰ��
			String[] split = str.split("\t");
			operator = split[1];
			if(!str.startsWith("\t")){//���� �ִ°��
				label = split[0];
			}
			if(split.length != 2)
				store_operand(split, A);
		}
		else{ comment = str; }
		cal_flag();
	}
}
