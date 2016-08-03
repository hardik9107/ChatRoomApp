package chat;

import java.rmi.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.rmi.server.*;
import java.rmi.registry.*;

public class RMIChatClientForm extends JFrame implements ChatClientInterface,ActionListener
{
	Container c;
	//JTextArea jta;
	JList listChatrooms, listClients;
	JTextField jtf;
	JButton jbtn;
	JScrollPane jsp1, jsp2, jsp4;
	DefaultListModel model,model1;
	//String key = "rmi://192.168.2.4:1099/sample";
	String key = "rmi://localhost:1099/sample";
	ChatServerInterface csi;
	String myName;
	Hashtable<String, MiniWindow> ht;
	
	public RMIChatClientForm(String name)
	{
		super("Chat Client " + name);
		myName = name;
		setSize(720,475);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c = getContentPane();
		c.setLayout(null);

		/*jta = new JTextArea();
		jsp1 = new JScrollPane(jta);
		jsp1.setBounds(5,5,500,400);
		c.add(jsp1);*/

		model = new DefaultListModel();
		listChatrooms = new JList(model);
		jsp2 = new JScrollPane(listChatrooms);
		jsp2.setBounds(5,5,500,400);
		c.add(jsp2);		

		jtf = new JTextField();
		jtf.setBounds(5,410,500,25);
		c.add(jtf);

		model1 = new DefaultListModel();
		listClients = new JList(model1);
		jsp2 = new JScrollPane(listClients);
		jsp2.setBounds(510,5,200,400);
		c.add(jsp2);

		jbtn = new JButton("Create");
		jbtn.setBounds(510,410,200,25);
		c.add(jbtn);

		jtf.addActionListener(this);
		jbtn.addActionListener(this);
		
		ht = new Hashtable<String, MiniWindow>();

		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				try{
					csi.logout(myName);
				}
				catch(Exception e1){
					System.out.println(e1);
				}
				dispose();
			}
		});
		
		listClients.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent me)
			{
				if(me.getClickCount()==2)
				{
					String hisName = (String)listClients.getSelectedValue();
					if(!(hisName.equals(myName))){
					MiniWindow mw = ht.get(hisName);
					if(mw == null)
					{
						try{
							ChatClientInterface hisRef = csi.giveRef(hisName);
							mw = new MiniWindow(hisName, hisRef);
							ht.put(hisName, mw);
						}
						catch(Exception e)
						{ System.out.println(e);}
					}
					}
				}
			}
		});
		
		try{
			csi = (ChatServerInterface)Naming.lookup(key);
			UnicastRemoteObject.exportObject(this);
			csi.login(myName,this,"sample");
		}
		catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
		}
		//jta.setEditable(false);
		setVisible(true);

	}
	public void takeMsg(String msg)
	{
		//jta.append(msg+"\n");
	}
	public void takeClientList(final Vector<String> clients)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				model1.removeAllElements();
				Enumeration<String> en = clients.elements();
				while(en.hasMoreElements())
				{
					String s = en.nextElement();
					model1.addElement(s);
				}
			}
		});
	}
	public void actionPerformed(ActionEvent e)
	{
		String msg = jtf.getText();
		msg = myName + ": " + msg;
		try{
			csi.takeMsg(msg);
		}
		catch(Exception e1){
			System.out.println(e1);
		}
		jtf.setText("");
	}
	public void pm(String hisName, ChatClientInterface hisRef, String hisMsg)
	{
		MiniWindow mw = ht.get(hisName);
		if(mw == null){
			mw = new MiniWindow(hisName,hisRef);
			ht.put(hisName,mw);
		}
		mw.jta.append(hisMsg+"\n");
	}

	public class MiniWindow extends JFrame implements ActionListener
	{
		Container c1;
		JTextArea jta;
		JTextField jtf;
		JButton jbtn;
		JScrollPane jsp1;
		String hisName;
		ChatClientInterface hisRef;
	
		public MiniWindow(String name, ChatClientInterface ref)
		{
			super("From " + myName +" to " + name);
			hisName = name;
			hisRef = ref;
			setResizable(false);
			setSize(340,280);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			c1 = getContentPane();
			c1.setLayout(null);

			jta= new JTextArea();
			jsp1 = new JScrollPane(jta);
			jsp1.setBounds(5,5,325,200);
			c1.add(jsp1);

			jtf = new JTextField();
			jtf.setBounds(5,210,235,25);
			c1.add(jtf);

			jbtn = new JButton("Send");
			jbtn.setBounds(245,210,85,25);
			c1.add(jbtn);

			addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent we)
				{
					ht.remove(hisName);
				}
			});
			jtf.addActionListener(this);
			jbtn.addActionListener(this);
			jta.setEditable(false);
			setVisible(true);
		}
		
		public void actionPerformed(ActionEvent e)
		{
			String chatroomName = jtf.getText();
			//myMsg = myName + ": "+ myMsg;
			try{
				hisRef.pm(myName,RMIChatClientForm.this,chatroomName);
			}
			catch(Exception e1){
				System.out.println(e1);
			}
			//jta.append(myMsg+ "\n");
			jtf.setText("");		
		}
	
	}

	/*public static void main(String args[])
	{
		RMIChatClient app = new RMIChatClient(args[0]);
	}*/
}