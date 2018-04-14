package speedquiz;
import java.rmi.*;
public interface Server extends Remote{
	//RMI기반 스켈레톤
	public void send(String msg) throws RemoteException;
	//메세지를 보내는 함수
	public void register(Client client, String name) throws RemoteException;
	//이용자 이름을 현재 서버정보에 추가하며, 입장하셨습니다 메세지를 띄워줌
	public void unregister(Client client, String name) throws RemoteException;
	//퇴장시 이용자 이름을 서버 정보에서 제거하며, 퇴장하였습니다.라는 메세지 띄워줌
	public int getIndex() throws RemoteException;
	//인덱스를 받아오는 함수
}
