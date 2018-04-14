
public class LitTab {
	private int addr;
	private String value;
	private String name;
	
	public LitTab(){
		addr = 0;
		value = "";
		name = "";
	}
	
	public int getAddr(){ return addr;}
	public String getValue(){ return value;}
	public String getName(){ return name;}
	
	public void setAddr(int i){ addr = i;}
	public void setValue(String str){ value = str;}
	public void setName(String str){ name = str; }
	
	public void print_lit(){
		System.out.println(addr+"    "+value+"    "+name);
	}
}


