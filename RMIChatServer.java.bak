package chat;

import java.rmi.*;
import java.util.*;
import java.rmi.server.*;
import java.rmi.registry.*;

public class RMIChatServer extends UnicastRemoteObject implements ChatServerInterface
{
	Vector<ClientInfo> v;
	Vector <ChatroomInfo>chatrooms;
	String identity = "sample";
	public RMIChatServer() throws RemoteException
	{
		v = new Vector<ClientInfo>();
		chatrooms = new Vector<ChatroomInfo>();
		try
		{
			LocateRegistry.createRegistry(1099);
			Naming.rebind(identity,this);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	public void login(String name, ChatClientInterface ref, String chatRoom)
	{
		ClientInfo ci = new ClientInfo(name,ref);
		if(chatrooms.size() != 0){
		Enumeration<ChatroomInfo> crn = chatrooms.elements();
		boolean roomExists = false;
		while(crn.hasMoreElements()){
			ChatroomInfo cr = crn.nextElement();
			if(cr.name.equals(chatRoom)){
				roomExists = true;
				cr.chatroomClients.add(ci);
				broadcastMsg(cr,name + " has logged in");
				broadcastList(cr);
			}
		}
		if(!roomExists){
			String identity = chatRoom;
			/*try{
			Naming.rebind(identity,this);
			}
			catch(Exception e){
				e.printStackTrace();
			}*/
			ChatroomInfo cr = new ChatroomInfo(chatRoom);
			cr.chatroomClients.add(ci);
			broadcastMsg(cr,name + " has logged in");
			broadcastList(cr);
		}
		}
		v.add(ci);				
		//broadcastMsg(name + " has logged in");
		broadcastList();
	}
	public void takeMsg(String msg)
	{
		broadcastMsg(msg);
	}
	public void logout(String name)
	{
		ClientInfo ci = new ClientInfo(name, null);
		if(v.contains(ci))
		{
			v.remove(ci);
			broadcastMsg(name + " has logged out");
			broadcastList();
		}
	}
	public void broadcastMsg(String msg)
	{
		Enumeration<ClientInfo> en = v.elements();
		while(en.hasMoreElements())
		{
			try{
				ClientInfo ci = en.nextElement();
				ci.ref.takeMsg(msg);
			}
			catch(Exception e){
				System.out.println(e);
			}
		}
	}
	public void broadcastMsg(ChatroomInfo crm,String msg)
	{
		Enumeration<ClientInfo> en = crm.chatroomClients.elements();
		while(en.hasMoreElements())
		{
			try{
				ClientInfo ci = en.nextElement();
				ci.ref.takeMsg(msg);
			}
			catch(Exception e){
				System.out.println(e);
			}
		}
	}
	public void broadcastList()
	{
		Vector<String> names = new Vector<String>();
		Enumeration<ClientInfo> en = v.elements();
		while(en.hasMoreElements())
		{
			ClientInfo ci = en.nextElement();
			names.add(ci.name);
		}
		Enumeration<ClientInfo> en1 = v.elements();
		while(en1.hasMoreElements())
		{
			try{
				ClientInfo ci = en1.nextElement();
				ci.ref.takeClientList(names);
			}
			catch(Exception e){
				System.out.println(e);
			}
			
		}
	}
	public void broadcastList(ChatroomInfo crm)
	{
		Vector<String> names = new Vector<String>();
		Enumeration<ClientInfo> en = crm.chatroomClients.elements();
		while(en.hasMoreElements())
		{
			ClientInfo ci = en.nextElement();
			names.add(ci.name);
		}
		Enumeration<ClientInfo> en1 = crm.chatroomClients.elements();
		while(en1.hasMoreElements())
		{
			try{
				ClientInfo ci = en1.nextElement();
				ci.ref.takeClientList(names);
			}
			catch(Exception e){
				System.out.println(e);
			}
			
		}
	}
	public ChatClientInterface giveRef(String name)
	{
		ClientInfo ci = new ClientInfo(name,null);
		int index = v.indexOf(ci);
		ci = v.get(index);
		return ci.ref;
	}
	class ClientInfo
	{
		String name;
		ChatClientInterface ref;;
		public ClientInfo(String name, ChatClientInterface ref)
		{
			this.name = name;
			this.ref = ref;
		}
		public boolean equals(Object o)
		{
			if(o instanceof ClientInfo)
			{
				return ((ClientInfo)o).name.equals(name);
			}
			else
				return false;
		}
	}

	class ChatroomInfo
	{
		String name;
		Vector<ClientInfo> chatroomClients;
		//ChatClientInterface ref;;
		public ChatroomInfo(String name)
		{
			this.name = name;
			chatroomClients = new Vector<ClientInfo>();
		}
		public boolean equals(Object o)
		{
			if(o instanceof ChatroomInfo)
			{
				return ((ChatroomInfo)o).name.equals(name);
			}
			else
				return false;
		}
	}

	public static void main(String args[])
	{
		try{
			new RMIChatServer();
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

}