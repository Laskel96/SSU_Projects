
public class InstTab {
	private String name; // instruction
	private int format1; // format1
	private int format2; // format2
	private int opcode; //opcode
	private int numop; // number of operand
	
	public void InstTab(){ // constructor
		name = "";
		format1 = 0;
		format2 = 0;
		opcode = 0;
		numop = 0;
	}
	
	public String getName(){return name;}
	public int getFormat1(){return format1;}
	public int getFormat2(){return format2;}
	public int getOpcode(){return opcode;}
	public int getNumOP(){return numop;}
	
	public void setName(String s){name = s;}
	public void setFormat1(int n){format1 = n;}
	public void setFormat2(int n){format2 = n;}
	public void setOpcode(int n){opcode = n;}
	public void setNumOp(int n){numop = n;}
	
	public void printInst(){ // 컴파일용 출력함수
		String s = String.format("0x%02X", opcode);
		System.out.println(name+" "+format1+" "+format2+" "+s+" "+numop);
	}
	
}