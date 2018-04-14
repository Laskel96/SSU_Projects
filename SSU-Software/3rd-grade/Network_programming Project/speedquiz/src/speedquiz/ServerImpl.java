package speedquiz;
import java.rmi.server.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.*;
import java.util.*;
import java.rmi.registry.*;

import java.io.IOException;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

import speedquiz.ClientImpl.quiz;

public class ServerImpl extends UnicastRemoteObject implements Server{
	/**
	 * 
	 */
	static int Port = 4890;
	
	private static final long serialVersionUID = 1L;
	Vector<Client> clientList;
	ArrayList<quiz> QA = new ArrayList<quiz>();
	private int index = 0;
	
	class quiz{
		String question = "";
		String answer = "";
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
			e.printStackTrace();
		}
	}
	
	public ServerImpl() throws RemoteException
	{
		super(0, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory(null,null,true));  //4500은 port 번호 입력 sslRMI socket을 받고 보내는 서버 생성
		clientList = new Vector<Client>();
		initialize("Input.txt");
	}
	public synchronized void register(Client client,String name) throws RemoteException
	{
		clientList.add(client);
		System.out.println("register called");
		send(name + ": 님이 입장하셨습니다.");
	}
	public void send(String msg) throws RemoteException {
		synchronized(clientList){
			Enumeration<Client> e = clientList.elements();
			String ans = msg;
			String temp[] = ans.split(":");
			if(temp.length != 1 ){
				if(temp[1].equals(QA.get(index).answer.trim())){
					index++;
				}
			}
			while(e.hasMoreElements())
			{
				Client c = (Client)e.nextElement();
				c.back(msg);
				if(temp.length != 1 && index != 0){
					if(temp[1].equals(QA.get(index-1).answer.trim())){
						c.back(temp[0] + " is correct");
					}
				}
				c.update();
			}
		}
	}
	public synchronized void unregister(Client client,String name) throws RemoteException {
		clientList.removeElement(client);
		send(name + "님이 퇴장하셨습니다.");
	}
	public static void main(String[] args)
	{
		try{
			setSettings();
			ServerImpl s = new ServerImpl();
			LocateRegistry.createRegistry(Port, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory()); //registry를 sslRMI Socket 통신하는 registry로 생성하겠다는 내용
			Registry registry = LocateRegistry.getRegistry("114.71.36.222", Port, new SslRMIClientSocketFactory());
			registry.rebind("server", s);
			System.out.println("RMI Chat Server is Ready.");
		}catch (Exception e)
		{
			System.out.println("Server 45 line");
			e.printStackTrace();
		}
	}
	public int getIndex(){
		return index;
	}
	
	private static void setSettings(){
		String pass = "Jeong0908!"; //이부분은 인증서 비빌번호
		System.setProperty("java.rmi.server.hostname","114.71.36.222");
		System.setProperty("javax.net.ssl.debug", "all"); //모든 소켓을 ssl로 디버깅 하겠다는 내용
    	System.setProperty("javax.net.ssl.keyStore", "C:/Users/Guik/Desktop/speedquiz/bin/.keystore/SSLSocketServerKey");//keystore 입력 & a부분은 server인증서 경로
  	    System.setProperty("javax.net.ssl.keyStorePassword", pass);//인증서 비밀번호
  	    System.setProperty("javax.net.ssl.trustStore", "C:/Users/Guik/Desktop/speedquiz/bin/trustedcerts");//인증서 발급한 다음 신뢰도 있는 인증서(trustedcerts)를 발급 받는데 그거 쓰고 b는 그 trustedcerts 경로
  	    System.setProperty("javax.net.ssl.trustStorePassword", pass);//trustedcerts 비밀번호
	}
}
