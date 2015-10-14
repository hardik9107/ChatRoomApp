package chat;

import java.rmi.*;
import java.util.*;

public interface ChatClientInterface extends Remote
{
	public void takeMsg(String msg) throws RemoteException;		//takes msg from server
	public void takeClientList(Vector<String> names) throws RemoteException;
	public void pm(String hisName, ChatClientInterface hisRef, String hisMsg) throws RemoteException;
}