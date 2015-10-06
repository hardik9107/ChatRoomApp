package chat;

import java.rmi.*;

public interface ChatServerInterface extends Remote
{
	public void login(String name, ChatClientInterface ref,String chatRoom) throws RemoteException;
	public void takeMsg(String msg) throws RemoteException;
	public void logout(String name) throws RemoteException;	
	public ChatClientInterface giveRef(String name) throws RemoteException;

}