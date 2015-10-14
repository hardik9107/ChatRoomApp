package chat;

import java.rmi.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.sql.*;
import chat.*;

public class RMIChatClientLogin extends JFrame{
	JButton SUBMIT,Register, ConfirmRegistration;
  JPanel panel;
  JLabel label1,label2, label3;
  final JTextField  text1,text2,text3;

	public RMIChatClientLogin(){
   label1 = new JLabel();
   label1.setText("Username:");
   text1 = new JTextField(15);
 
   label2 = new JLabel();
   label2.setText("Password:");
   text2 = new JPasswordField(15);

   label3 = new JLabel();
   label3.setText("ConfirmPassword:");
   text3 = new JPasswordField(15);
  
   SUBMIT=new JButton("SUBMIT");
   ConfirmRegistration = new JButton("ConfirmRegistration");
   Register=new JButton("REGISTER");
   
   panel=new JPanel(new GridLayout(4,1));
   panel.add(label1);
   panel.add(text1);
   panel.add(label2);
   panel.add(text2);
   panel.add(SUBMIT);
   panel.add(Register);
   add(panel,BorderLayout.CENTER);
   setTitle("LOGIN FORM");
   SUBMIT.addActionListener(new ActionListener(){
	public void actionPerformed(ActionEvent ae){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatroom","root","hardik");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("Select userId from users where userId ='"+ text1.getText() +"' and password ='"+text2.getText()+"'");
			if(rs.next()){
				setVisible(false);
				RMIChatClientForm app = new RMIChatClientForm(text1.getText());
			}
			else{
				panel.removeAll();
				panel=new JPanel(new GridLayout(5,1));
				text1.setText("");
				text2.setText("");
			   panel.add(label1);
			   panel.add(text1);
			   panel.add(label2);
			   panel.add(text2);
			   panel.add(SUBMIT);			   
			   add(panel,BorderLayout.CENTER);
			   panel.revalidate();
			   panel.repaint();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
   });

   Register.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
		try{
				panel.removeAll();
				panel=new JPanel(new GridLayout(7,1));
				text1.setText("");
				text2.setText("");
			   panel.add(label1);
			   panel.add(text1);
			   panel.add(label2);
			   panel.add(text2);
			   panel.add(label3);
			   panel.add(text3);
			   panel.add(ConfirmRegistration);
			   add(panel,BorderLayout.CENTER);
			   panel.revalidate();
			   panel.repaint();
			   ConfirmRegistration.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					if(text2.getText().equals(text3.getText())){
					try{
						Class.forName("com.mysql.jdbc.Driver");
						Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatroom","root","hardik");
						Statement st = con.createStatement();
						ResultSet rs = st.executeQuery("Select userId from users where userId ='"+ text1.getText() +"'");
						if(!rs.next()){
						PreparedStatement stmt = con.prepareStatement("Insert into users values('"+ text1.getText()+"','"+text2.getText()+"')");
						stmt.executeUpdate();
						panel.removeAll();
						panel.add(label1);
					   panel.add(text1);
					   panel.add(label2);
					   panel.add(text2);
					   panel.add(SUBMIT);
					   panel.add(Register);
					   add(panel,BorderLayout.CENTER);
					   setTitle("LOGIN FORM");
					   panel.repaint();
						}
						else{
							panel.add(new JLabel("username already exists"));
							panel.revalidate();
							panel.repaint();
						}
						}
					catch(Exception ae1){
						System.out.println(ae1);
						ae1.printStackTrace();
					}
					}
					else{
						text2.setText("");
						text3.setText("");
						panel.add(new JLabel("Passwords donot match"));
						panel.revalidate();
						panel.repaint();
					
					}
				}
			   }				   
			   );
			//}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
   });
   }

	public static void main(String args[])
	{
		RMIChatClientLogin app = new RMIChatClientLogin();
		app.setSize(400,200);
		app.setVisible(true);
	}

}