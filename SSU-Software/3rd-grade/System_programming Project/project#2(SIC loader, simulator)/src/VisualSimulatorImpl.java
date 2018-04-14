import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class VisualSimulatorImpl extends JFrame implements VisualSimulator{
	//시뮬레이터를 동작시키기 위한 세팅을 수행한다.
	//SIC 시뮬레이터를 통해 로더를 수행시키고, 로드된 값들을 읽어 보여주어
	//스텝을 진행할 수 있는 상태로 만들어 놓는다.
	
	
	ResourceManagerImpl rMgr;
	File file;
	SicSimulatorImpl sic;
	int num = 1;
	private JTextField fileNameTF;
	private JTextField ProgName_TF;
	private JTextField StartAddr_TF;
	private JTextField LengthProg_TF;
	private JTextField A_Dec;
	private JTextField A_Hex;
	private JTextField X_Dec;
	private JTextField X_Hex;
	private JTextField L_Dec;
	private JTextField L_Hex;
	private JTextField PC_Dec;
	private JTextField PC_Hex;
	private JTextField SW_Dec;
	private JTextField SW_Hex;
	private JTextField B_Dec;
	private JTextField B_Hex;
	private JTextField S_Dec;
	private JTextField S_Hex;
	private JTextField T_Dec;
	private JTextField T_Hex;
	private JTextField F_Dec;
	private JTextField F_Hex;
	private JTextField AddrFirstInst_TF;
	private JTextField StartAddrMem_TF;
	private JTextField TA_TF;
	private JTextArea Log_TF;
	private JTextArea Instruction_TF;
	private JTextField useDev_TF;
	
	JButton one_step;
	JButton all_step;
	private JButton close;
	private JButton open;
	
	private JList<String> Log_list;
	private JList<String> Inst_list;
	
	public void initialize(File obFile, ResourceManager rMgr){
		VisualSimulatorImpl thisobject = this;
		sic = new SicSimulatorImpl();
		this.rMgr = (ResourceManagerImpl)rMgr;
		this.file = obFile;
	
		fileNameTF = new JTextField();
		ProgName_TF = new JTextField();
		StartAddr_TF = new JTextField();
		LengthProg_TF = new JTextField();
		A_Dec = new JTextField();
		A_Hex = new JTextField();
		X_Dec = new JTextField();
		X_Hex = new JTextField();
		L_Dec = new JTextField();
		L_Hex = new JTextField();
		PC_Dec = new JTextField();
		PC_Hex = new JTextField();
		SW_Dec = new JTextField();
		SW_Hex = new JTextField();
		B_Dec = new JTextField();
		B_Hex = new JTextField();
		S_Dec = new JTextField();
		S_Hex = new JTextField();
		T_Dec = new JTextField();
		T_Hex = new JTextField();
		F_Dec = new JTextField();
		F_Hex = new JTextField();
		AddrFirstInst_TF = new JTextField();
		StartAddrMem_TF = new JTextField();
		TA_TF = new JTextField();
		Log_TF = new JTextArea(8,10);
		Instruction_TF = new JTextArea(8,8);
		useDev_TF = new JTextField();
		
		
		ProgName_TF.setEditable(false);
		StartAddr_TF.setEditable(false);
		LengthProg_TF.setEditable(false);
		A_Dec.setEditable(false);
		A_Hex.setEditable(false);
		X_Dec.setEditable(false);
		X_Hex.setEditable(false);
		L_Dec.setEditable(false);
		L_Hex.setEditable(false);
		PC_Dec.setEditable(false);
		PC_Hex.setEditable(false);
		SW_Dec.setEditable(false);
		SW_Hex.setEditable(false);
		B_Dec.setEditable(false);
		B_Hex.setEditable(false);
		S_Dec.setEditable(false);
		S_Hex.setEditable(false);
		T_Dec.setEditable(false);
		T_Hex.setEditable(false);
		F_Dec.setEditable(false);
		F_Hex.setEditable(false);
		AddrFirstInst_TF.setEditable(false);
		TA_TF.setEditable(false);
		
		useDev_TF.setEditable(false);
		
		
		one_step = new JButton("one step");
		all_step = new JButton(" All step ");
		close = new JButton("   Close   ");
		open = new JButton("open");

		JPanel masterPanel = new JPanel(new BorderLayout());
		JPanel filenamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel left = new JPanel();
		JPanel headerPanel = new JPanel(new GridLayout(3,2));
		JPanel registerPanel = new JPanel(new GridLayout(6,3));
		JPanel EXregisterPanel = new JPanel(new GridLayout(5,3));
		JPanel right = new JPanel();
		JPanel endPanel = new JPanel();
		JPanel StartAddrMemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel logPanel = new JPanel();
		JPanel taPanel = new JPanel();
		JPanel last= new JPanel(new GridLayout(1,2));
		
		left.setLayout(new BoxLayout(left,BoxLayout.Y_AXIS));
		right.setLayout(new BoxLayout(right,BoxLayout.Y_AXIS));
		logPanel.setLayout(new BoxLayout(logPanel, BoxLayout.Y_AXIS));
		endPanel.setLayout(new BoxLayout(endPanel, BoxLayout.Y_AXIS));
		
		/*
		 * 파일 이름 패널
		 */
		JLabel fileName = new JLabel("FileName :");
		fileNameTF.setPreferredSize(new Dimension(180,30));
		filenamePanel.add(fileName);
		filenamePanel.add(fileNameTF);
		filenamePanel.add(open);
		
		/*
		 * 헤더 패널
		 */
		//headerPanel.setLayout(new BoxLayout(headerPanel,BoxLayout.Y_AXIS));
		headerPanel.setBorder(BorderFactory.createTitledBorder("H (Header Record"));
		
		JLabel ProgName = new JLabel("Program Name: ");
		JLabel StartAddr = new JLabel("<html>Start Address of<br>Object Program: <html>");
		JLabel LengthProg = new JLabel("Length of Program: ");
		
		headerPanel.add(ProgName);
		headerPanel.add(ProgName_TF);
		headerPanel.add(StartAddr);
		headerPanel.add(StartAddr_TF);
		headerPanel.add(LengthProg);
		headerPanel.add(LengthProg_TF);
		
		left.add(headerPanel);
		
		/*
		 * 레지스터 패널
		 */
		
		registerPanel.setBorder(BorderFactory.createTitledBorder("Register"));

		JLabel blank = new JLabel("");
		JLabel Dec = new JLabel("Dec");
		JLabel Hex = new JLabel("Hex");
		JLabel A = new JLabel("A(#0)");
		JLabel X = new JLabel("X(#1)");
		JLabel L = new JLabel("L(#2)");
		JLabel PC = new JLabel("PC(#8)");
		JLabel SW = new JLabel("SW(#9");
		
		registerPanel.add(blank);
		registerPanel.add(Dec);
		registerPanel.add(Hex);
		registerPanel.add(A);
		registerPanel.add(A_Dec);
		registerPanel.add(A_Hex);
		registerPanel.add(X);
		registerPanel.add(X_Dec);
		registerPanel.add(X_Hex);
		registerPanel.add(L);
		registerPanel.add(L_Dec);
		registerPanel.add(L_Hex);
		registerPanel.add(PC);
		registerPanel.add(PC_Dec);
		registerPanel.add(PC_Hex);
		registerPanel.add(SW);
		registerPanel.add(SW_Dec);
		registerPanel.add(SW_Hex);
		
		
		left.add(registerPanel);
		
		/*
		 * 레지스터 EX 패널
		 */
		EXregisterPanel.setBorder(BorderFactory.createTitledBorder("Register(for EX)"));

		JLabel Ex_blank = new JLabel("");
		JLabel Ex_Dec = new JLabel("Dec");
		JLabel Ex_Hex = new JLabel("Hex");
		JLabel B = new JLabel("B(#3)");
		JLabel S = new JLabel("S(#4)");
		JLabel T = new JLabel("T(#5)");
		JLabel F = new JLabel("F(#6)");
		
		EXregisterPanel.add(Ex_blank);
		EXregisterPanel.add(Ex_Dec);
		EXregisterPanel.add(Ex_Hex);
		EXregisterPanel.add(B);
		EXregisterPanel.add(B_Dec);
		EXregisterPanel.add(B_Hex);
		EXregisterPanel.add(S);
		EXregisterPanel.add(S_Dec);
		EXregisterPanel.add(S_Hex);
		EXregisterPanel.add(T);
		EXregisterPanel.add(T_Dec);
		EXregisterPanel.add(T_Hex);
		EXregisterPanel.add(F);
		EXregisterPanel.add(F_Dec);
		EXregisterPanel.add(F_Hex);
		
		left.add(EXregisterPanel);
		
		/*
		 * 엔드 패널
		 */
		endPanel.setBorder(BorderFactory.createTitledBorder("E(End Record)"));

		JLabel AddrFirstInst = new JLabel("<html>Address of First Instruction<br>In Object Program: <html>");
		AddrFirstInst_TF.setMaximumSize(new Dimension(180,20));
		endPanel.add(AddrFirstInst);
		endPanel.add(AddrFirstInst_TF);
		
		right.add(endPanel);
		
		/*
		 * 메모리 시작주소
		 */
		
		JLabel StartAddrMem = new JLabel("Start Address in Memory");
		JLabel StartAddrMemBlank = new JLabel("                                 ");
		StartAddrMem_TF.setPreferredSize(new Dimension(100,20));
		StartAddrMem_TF.setHorizontalAlignment(StartAddrMem.RIGHT);
		StartAddrMem_TF.setText("0");
		StartAddrMemPanel.add(StartAddrMem);
		StartAddrMemPanel.add(StartAddrMemBlank);
		StartAddrMemPanel.add(StartAddrMem_TF);
		
		right.add(StartAddrMemPanel);
		
		/*
		 * 타겟주소
		 */
		
		JLabel TA = new JLabel("Target Address");
		TA_TF.setPreferredSize(new Dimension(100,20));
		taPanel.add(TA);
		taPanel.add(TA_TF);
		
		right.add(taPanel);

		/*
		 * 로그 패널
		 */
		JLabel Log = new JLabel("Log(명령어 수행 관련)");
		
		//=======================================================================================
		logPanel.setLayout(new BoxLayout(logPanel,BoxLayout.Y_AXIS));
		
		JScrollPane Log_panel = new JScrollPane(Log_TF);
		Log_panel.setSize(this.getWidth(),150);
		Log_panel.setViewportView(Log_TF);
		Log_panel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		
		logPanel.add(Log);
		logPanel.add(Log_panel);
		
		/*
		 * 그 외 ( instruction, 사용중인 장치, 1줄 실행, 모두 실행, 종료)
		 */

		JPanel instPanel = new JPanel();
		instPanel.setLayout(new BoxLayout(instPanel,BoxLayout.Y_AXIS));
		JLabel Inst = new JLabel("Instructions :");
		instPanel.add(Inst);
		JScrollPane Inst_panel = new JScrollPane(Instruction_TF);
		Inst_panel.setViewportView(Instruction_TF);
		Inst_panel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		instPanel.add(Inst_panel);
		
		
		//instPanel.add(Instruction_TF);
		
		//////////////////////////////////////////////////////////////
	
	
		JPanel other = new JPanel();
		other.setLayout(new BoxLayout(other,BoxLayout.Y_AXIS));
		JLabel useDev = new JLabel("UsingDevice");
		useDev.setAlignmentX(CENTER_ALIGNMENT);
		useDev_TF.setAlignmentX(CENTER_ALIGNMENT);
		useDev_TF.setPreferredSize(new Dimension(50,20));
		useDev_TF.setMaximumSize(new Dimension(50,20));
		one_step.setAlignmentX(CENTER_ALIGNMENT);
		one_step.setEnabled(false);
		all_step.setAlignmentX(CENTER_ALIGNMENT);
		all_step.setEnabled(false);
		close.setAlignmentX(CENTER_ALIGNMENT);
		JLabel temp1 = new JLabel("  ");
		JLabel temp2 = new JLabel("  ");
		JLabel temp3 = new JLabel("  ");
		JLabel temp4 = new JLabel("  ");
		JLabel temp6 = new JLabel("  ");
		JLabel temp7 = new JLabel("  ");
		other.add(useDev);
		other.add(useDev_TF);
		other.add(temp1);
		other.add(temp2);
		other.add(temp3);
		other.add(temp4);
		other.add(one_step);
		other.add(temp6);
		other.add(all_step);
		other.add(temp7);
		other.add(close);
		
		/////////////////////////////////////////////////////////////
		last.add(instPanel);
		last.add(other);
		right.add(last);
		
		/*
		 * 메인페널 등록
		 */
		masterPanel.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
		masterPanel.add(filenamePanel, BorderLayout.NORTH);
		masterPanel.add(left, BorderLayout.WEST);
		masterPanel.add(right,BorderLayout.CENTER);
		masterPanel.add(logPanel, BorderLayout.SOUTH);
		
		/*
		 * 메인패널 설정
		 */
		this.setVisible(true);
		this.setSize(500,700);
		this.setResizable(false);
		this.getContentPane().add(masterPanel);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		open.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(final MouseEvent e){
				final FileDialog fd = new FileDialog((Frame)thisobject,"파일열기",FileDialog.LOAD);
				fd.setVisible(true);
				if(fd.getDirectory() != null){
					ResourceManagerImpl rMgr = new ResourceManagerImpl();
					File object_code_file = new File(fd.getDirectory() + fd.getFile());
					one_step.setEnabled(true);
					all_step.setEnabled(true);
					initialize(object_code_file, rMgr);
					update();
				}
			}
		});
		close.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(final MouseEvent e){
				System.exit(0);
			}
		});
		one_step.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(final MouseEvent e){
				oneStep();
			}
		});
		all_step.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(final MouseEvent e){
				allStep();
			}
		});
		sic.initialize(file, this.rMgr);
	}
	
	//하나의 명령어만 수행하는 메소드로써 SIC시뮬레이터에게 작업을 전달한다.
	public void oneStep(){
		num++;
		sic.oneStep();
		update();
	}
	//남은 명령어를 모두 수행하는 메소드로써 SIC시뮬레이터에 작업을 전달한다.
	public void allStep(){
		for(int i = num; i < 102;i++){
			oneStep();
		}
	}
	//작업이 완료되었을 때 변화된 결과를 화면에 업데이트한다.
	public void update(){
		if(file != null)
			fileNameTF.setText(file.getName());
		ProgName_TF.setText(rMgr.getProgName());
		StartAddr_TF.setText(rMgr.getStartSectAddress());
		LengthProg_TF.setText(rMgr.getLengthProg());
		
		TA_TF.setText(sic.getTargetAddr().toUpperCase());
		
		A_Dec.setText(Integer.toString(rMgr.getRegister(0)));
		A_Hex.setText(Integer.toHexString(rMgr.getRegister(0)));
		X_Dec.setText(Integer.toString(rMgr.getRegister(1)));
		X_Hex.setText(Integer.toHexString(rMgr.getRegister(1)));
		L_Dec.setText(Integer.toString(rMgr.getRegister(2)));
		L_Hex.setText(Integer.toHexString(rMgr.getRegister(2)));
		PC_Dec.setText(Integer.toString(rMgr.getRegister(7)));
		PC_Hex.setText(Integer.toHexString(rMgr.getRegister(7)));
		SW_Dec.setText(Integer.toString(rMgr.getRegister(8)));
		SW_Hex.setText(Integer.toHexString(rMgr.getRegister(8)));
		B_Dec.setText(Integer.toString(rMgr.getRegister(3)));
		B_Hex.setText(Integer.toHexString(rMgr.getRegister(3)));
		S_Dec.setText(Integer.toString(rMgr.getRegister(4)));
		S_Hex.setText(Integer.toHexString(rMgr.getRegister(4)));
		T_Dec.setText(Integer.toString(rMgr.getRegister(5)));
		T_Hex.setText(Integer.toHexString(rMgr.getRegister(5)));
		F_Dec.setText(Integer.toString(rMgr.getRegister(6)));
		F_Hex.setText(Integer.toHexString(rMgr.getRegister(6)));
		
		useDev_TF.setText(rMgr.getUsingDev());
		
		String s[] = new String[100];
		if(sic.getInst().size() != 0){
			Instruction_TF.append(sic.getInst().get(sic.getInst().size()-1) + "\n");
			Instruction_TF.setCaretPosition(Instruction_TF.getDocument().getLength());
		}
		String l[] = new String[100];
		if(sic.getLog().size() != 0 ){
			Log_TF.append(sic.getLog().get(sic.getLog().size()-1) + "\n");
			Log_TF.setCaretPosition(Log_TF.getDocument().getLength());
		}
		
		AddrFirstInst_TF.setText(Integer.toHexString(rMgr.getProgStartAddress()));
		
		this.setVisible(true);
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ResourceManager rMgr = null;
		VisualSimulatorImpl vsic = new VisualSimulatorImpl();
		File file =null;
		vsic.initialize(file, rMgr);
	}
}
