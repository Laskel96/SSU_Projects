
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
	 * 설명 : 명령어 인자들을 저장
	 * Input : 명령어 인자 배열
	 * Output 없음
	 */
	protected void store_operand(String[] s, Assembler A){ // store operand
		int opNum;
		opNum = A.search_opNum(operator);
		if(opNum != -1){//명령어인 경우
			if(opNum == 1){ // 명령어가 1개인경우
				operand1 = s[2];
			}
			else if(opNum == 2){ // 명령어가 2개인경우
				String[] sub_split = s[2].split(",");
				operand1 = sub_split[0];
				operand2 = sub_split[1];
			}
		}
		else{//명령어가 아닌경우
			char[] str = s[2].toCharArray(); // str을 char 배열로
			int num = 0;
			String[] sub_split = s[2].split(","); // ,로 나눔
			num = sub_split.length;
			if(num == 1){//인자 1개
				operand1 = sub_split[0];
			}				
			else if(num ==2){//인자 2개
				operand1 = sub_split[0];
				operand2 = sub_split[1];
			}
			else if(num ==3){//인자 3개
				operand1 = sub_split[0];
				operand2 = sub_split[1];					
				operand3 = sub_split[2];
			}
		}
	}
	
	/*
	 * 설명 : 헥사 명령어의 앞 두자리에 더할 opcode부분
	 * Input : 없음
	 * Ouutput : 없음
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
	 * 설명 : 토큰을 나누어 저장하는 함수
	 * Input : 파싱할 스트링
	 * Output : 없음
	 */
	
	public void token_parsing(String str, Assembler A){
		if(!str.contains(".")){ //주석 문장이 아닐경우
			String[] split = str.split("\t");
			operator = split[1];
			if(!str.startsWith("\t")){//라벨이 있는경우
				label = split[0];
			}
			if(split.length != 2)
				store_operand(split, A);
		}
		else{ comment = str; }
		cal_flag();
	}
}
