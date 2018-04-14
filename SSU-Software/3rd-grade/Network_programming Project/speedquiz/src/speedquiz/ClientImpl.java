package speedquiz;

import java.awt.*;
//import java.awt.List;
import java.awt.event.*;
import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.swing.*;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.*;
import javax.net.ssl.HttpsURLConnection;
import java.util.Scanner;
import java.util.Arrays;
import java.util.List;


public class ClientImpl extends JFrame implements Client,ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JFrame Game;
	JTextArea display, display2;
	JTextArea question;
	JTextField input,id;
	JLabel label,nick;
	Server server;
	JButton register,close,enter,clear;
	JButton log1,log2,help1,help2, game;
	String name,serverName;
	CardLayout card;
	JPanel npanel;
	String url = "http://218.146.0.107/help/index.html";
	JTextField help = new JTextField("1~3의 숫자를 하나 고르신 후 GO를 눌러주세요! 공백일 경우 종료!");
	JLabel range = new JLabel("입력범위 : 1 ~ 3");
	JTextField input1 = new JTextField("");
	JButton go = new JButton("GO");
	JLabel result = new JLabel("과연 맞출 수 있을까요?");
	JPanel set = new JPanel(new GridLayout(6,1));
	int count = 0;
	JLabel numCorrect = new JLabel("정답 횟수 : 0회");
	
	ArrayList<quiz> QA = new ArrayList<quiz>();
	//int QA_index = 0;;
	
	class quiz{
		String question = "";
		String answer = "";
	}
	
	public ClientImpl()
	{
		super("RMI 채팅");
		
		JPanel questionPanel = new JPanel();
		questionPanel.setPreferredSize(new Dimension(this.getWidth(),150));
		question = new JTextArea();
		question.setPreferredSize(new Dimension(320,140));
		question.setEditable(false);
		questionPanel.add(question);
		question.setText("여기에 문제가 나와요");
		
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		display = new JTextArea();
		display.setEditable(false);
		JScrollPane spane = new JScrollPane(display);
		spane.setViewportView(display);
		spane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		c.add(spane, "Center");
		npanel = new JPanel();
		card = new CardLayout();
		npanel.setLayout(card);
		
		label = new JLabel("대화명 : ");
		id = new JTextField(10);
		register = new JButton("등록");
		register.addActionListener(this);
		close = new JButton("종료");
		close.addActionListener(this);
		JPanel loginPanel = new JPanel();
		log1 = new JButton("로그");
		log1.addActionListener(this);
		help1 = new JButton("도움말");
		help1.addActionListener(this);
		
		
		loginPanel.add(label);
		loginPanel.add(id);
		loginPanel.add(register);
		loginPanel.add(log1);
		loginPanel.add(help1);
		loginPanel.add(close);
		
		nick = new JLabel("즐팅!!");
		input = new JTextField(8);
		input.addActionListener(this);
		enter = new JButton("입력");
		enter.addActionListener(this);
		clear = new JButton("지우기");
		clear.addActionListener(this);
		log2 = new JButton("로그");
		log2.addActionListener(this);
		help2 = new JButton("도움말");
		help2.addActionListener(this);
		game = new JButton("미니게임");
		game.addActionListener(this);
		JPanel chatPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		
		chatPanel.add(nick);
		chatPanel.add(input);
		chatPanel.add(enter);
		chatPanel.add(clear);
		chatPanel.add(help2);
		chatPanel.add(log2);
		chatPanel.add(game);


		npanel.add(loginPanel,"login");
		npanel.add(chatPanel,"chat");
		
		c.add(npanel, "North");
		c.add(questionPanel, "South");
		
		this.setResizable(false);
		
		
		
		card.show(npanel, "login");
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				setVisible(false);
				if( server != null)
				{
					try{
						server.unregister(ClientImpl.this, name);
					}catch(Exception ee)
					{
						System.out.println("120Line");
						ee.printStackTrace();
					}
				}
				dispose();
				System.exit(0);
			}
		});
		game.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(final MouseEvent e){
				Game = new JFrame("랜덤 숫자 맞추기 게임");
				setGame();
			}
		});
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(550,500);
		setVisible(true);
		initialize("Input.txt");
		for(int i = 0; i < QA.size(); i ++){
			System.out.println(QA.get(i).question);
			System.out.println(QA.get(i).answer);
		}
	}
	public void initialize(String filename){
		try{
			BufferedReader in = new BufferedReader(new FileReader(filename));
			String s = "";
			boolean Q = true;
			quiz temp = new quiz();
			while((s = in.readLine()) != null){
				if(Q){
					temp.question = s;
					Q = false;
				}else{
					temp.answer = s;
					Q = true;
					QA.add(temp);
					temp = new quiz();
				}
			}
		}catch(IOException e){
			System.out.println("155Line");
			e.printStackTrace();
		}
	}
	@SuppressWarnings("deprecation")
	private void connect(){
		try{
			UnicastRemoteObject.exportObject(this);
			Registry registry = LocateRegistry.getRegistry("114.71.36.222", 4890, new SslRMIClientSocketFactory());
			server = (Server)registry.lookup("server");
			server.register(this, name);
			
		}catch(Exception e)
		{
			System.out.println("167Line");
			e.printStackTrace();
		}
	}
	public synchronized void back(String msg)
	{
		try{
			display.append(msg+"\n");
			display.setCaretPosition(display.getDocument().getLength());
		}catch(Exception e)
		{
			System.out.println("177Line");
			display.append(e.toString());
		}
	}
	 public static void openWebpage(URI uri) {
	     Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	     if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	         try {
	             desktop.browse(uri);
	         } catch (Exception e) {
	        	 System.out.println("187Line");
	             e.printStackTrace();
	         }
	     }
	 }

	 public static void openWebpage(URL url) {
	     try {
	         openWebpage(url.toURI());
	     } catch (URISyntaxException e) {
	    	 System.out.println("197Line");
	         e.printStackTrace();
	     }
	 }

	 public void update() throws RemoteException{
		 if(server.getIndex() == QA.size()-1){
			 question.setText("문제가 다 떨어졌어요!");
		 }
		 else{
			 question.setText(QA.get(server.getIndex()).question);
			 this.setVisible(true);
		 }
	 }
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();
		
		if(o == register)
		{
			name = id.getText().trim();
			card.next(npanel);
			connect();
			nick.setText(name+"님");
			if(QA.size() != 0){
				try {
					question.setText(QA.get(server.getIndex()).question);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				this.setVisible(true);
			}
			else{
				question.setText("문제가없어요");
				this.setVisible(true);
			}
		}else if(o == input || o == enter)
		{
			try{
				String myurl = "http://218.146.0.107/url.php?name=";
				String suburl = name;
				String suburl2 ="&text=";
				String suburl3 = input.getText();
				String text = input.getText();
				suburl = URLEncoder.encode(suburl,"UTF-8");
				suburl3 = URLEncoder.encode(suburl3,"UTF-8");
				myurl = myurl+suburl+suburl2+suburl3;
				server.send(name + ":" + text);
				URL url = new URL(myurl);
				
				HttpURLConnection http_conn = (HttpURLConnection)url.openConnection();
				http_conn.setDoInput(true);
				http_conn.setRequestProperty("Content-Type","application/x-www.form-urlencoded; charset=EUC-KR");
				http_conn.setRequestMethod("GET");
				if(http_conn.getResponseCode()!=HttpURLConnection.HTTP_OK)
						System.out.println("stop");
				input.setText("");
				update();
			}catch(Exception ex)
			{
				System.out.println("231Line");
				display.append(ex.toString());
			}
		}else if(o == clear)
		{
			display.setText("");
		}else if( o == close)
		{
			System.exit(0);
		}else if(o == help1 || o == help2){
			URL obj = null;
			try {
				obj = new URL(url);
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			openWebpage(obj);
		}
		else if( o == log1 || o == log2){
			try{
				BufferedReader bufferedReader = null;
				try{
					URL url = new URL("http://218.146.0.107/chat.php");
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
					StringBuilder sb = new StringBuilder();
					bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
					String json;
					while((json = bufferedReader.readLine())!=null){
						sb.append(json+"\n");
					}
					Gson gson = new Gson();
					String json_string = sb.toString().trim();
					List<chat> list = gson.fromJson(sb.toString().trim(), new TypeToken<List<chat>>(){}.getType());
					System.out.println(list);
				}
				catch(Exception e2){
					e2.printStackTrace();
					System.out.println("망함");
				}
			}
//				URL obj = new URL(url);
//				openWebpage(obj);
			finally{
			}
		}
	}
	class Print{
		public void print(String name){
			try{
				for(int i = 0; i <= 10; i ++){
					System.out.println("Count of" + name +" is : "+ i);
				}
			}catch(Exception e){
				System.out.println("Interrupted");
			}
		}
	}
	class async extends Thread{
		private Thread t;
		private String threadName;
		Print pt;
		boolean end = false;
		
		async(String name, Print p){
			threadName = name;
			pt = p;
		}
		public void run(){
			pt.print(threadName);
			System.out.println("Thread" + threadName + "end");
			end = true;
		}
		public void start(){
			System.out.println(threadName + " Start");
			if(t == null){
				t = new Thread(this,threadName);
				t.start();
			}
		}
		public boolean isEnd(){
			return end;
		}
	}
	public void setGame(){
		help.setEditable(false);
		help.setHorizontalAlignment(JTextField.CENTER);
		range.setHorizontalAlignment(JLabel.CENTER);
		result.setHorizontalAlignment(JLabel.CENTER);
		input1.setHorizontalAlignment(JTextField.CENTER);
		numCorrect.setHorizontalAlignment(JLabel.CENTER);
		
		set.add(help);
		set.add(range);
		set.add(input1);
		set.add(result);
		set.add(numCorrect);
		set.add(go);

		go.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(final MouseEvent e){
				if(!input1.getText().equals("")){
					if(Integer.parseInt(input1.getText()) > 0 && Integer.parseInt(input1.getText()) < 4){
						startGame();
					}
					else{
						input1.setText("");
						range.setText("1~3사이의 '숫자'를 넣으셔야 합니다!");
					}
				}else{
					System.exit(0);
				}
			}
		});
	
		Game.getContentPane().add(set);
		Game.setVisible(true);
		Game.setSize(400,300);
		Game.setResizable(false);
	}
	@SuppressWarnings("deprecation")
	public void startGame(){
		String s = "";
		Print pt= new Print();
		async t1 = new async("1",pt);
		async t2 = new async("2",pt);
		async t3 = new async("3",pt);
		t1.start();
		t2.start();
		t3.start();
		try{
			t1.join();
			t2.join();
			t3.join();
			while(true){
				if(t1.isEnd()){
					t1.stop();
					t2.stop();
					t3.stop();
					if(input1.getText().equals("1")){
						result.setText("정답입니다!");
						count++;
					}
					else{
						result.setText("틀렸습니다! 정답은 1입니다!");
					}
					input1.setText("");
					s = s.format("정답 횟수 : %d회", count);
					numCorrect.setText(s);
					break;
				}
				if(t2.isEnd()){
					t1.stop();
					t2.stop();
					t3.stop();
					if(input1.getText().equals("2")){
						result.setText("정답입니다!");
						count++;
					}
					else{
						result.setText("틀렸습니다! 정답은 2입니다!");
					}
					input1.setText("");
					s = s.format("정답 횟수 : %d회", count);
					numCorrect.setText(s);
					break;
				}
				if(t3.isEnd()){
					t1.stop();
					t2.stop();
					t3.stop();
					if(input1.getText().equals("3")){
						result.setText("정답입니다!");
						count++;
					}
					else{
						result.setText("틀렸습니다! 정답은 3입니다!");
					}
					input1.setText("");
					s = s.format("정답 횟수 : %d회", count);
					numCorrect.setText(s);
					break;
				}
			}
		}catch(Exception e){
			System.out.println("Interrupted");
		}
	}
	public static void main(String[] args)
	{
   		String pass = "Jeong0908!";
    	System.setProperty("javax.net.ssl.debug", "all");
   		System.setProperty("javax.net.ssl.keyStore", "C:/Users/Administrator/Desktop/speedquiz/bin/.keystore/SSLSocketServerKey");
 		System.setProperty("javax.net.ssl.keyStorePassword", pass);
 		System.setProperty("javax.net.ssl.trustStore", "C:/Users/Administrator/Desktop/speedquiz/bin/trustedcerts");
    	System.setProperty("javax.net.ssl.trustStorePassword", pass);
    	new ClientImpl();

	}
}
