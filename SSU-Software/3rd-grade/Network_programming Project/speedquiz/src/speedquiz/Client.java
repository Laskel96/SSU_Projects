package speedquiz;
import java.rmi.*;
public interface Client extends Remote {
	//RMI기반 클라이언트 스켈레톤
	public void back(String msg) throws RemoteException;
	//입력한 내용을 서버로 전달
	public String getName() throws RemoteException;
	//사용자가 등록한 이름을 얻어옴
	public void update() throws RemoteException;
	//정보 갱신 함수
}
