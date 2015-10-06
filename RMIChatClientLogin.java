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
	JButton SUBMIT,Register;
  JPanel panel;
  JLabel label1,label2;
  final JTextField  text1,text2;

	public RMIChatClientLogin(){
   label1 = new JLabel();
   label1.setText("Username:");
   text1 = new JTextField(15);
 
   label2 = new JLabel();
   label2.setText("Password:");
   text2 = new JPasswordField(15);
  
   SUBMIT=new JButton("SUBMIT");
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
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3308/chatroom","root","hardik");
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
			   panel.add(Register);
			   panel.add(new JLabel("Incorrect username or password"));
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
   }

	public static void main(String args[])
	{
		RMIChatClientLogin app = new RMIChatClientLogin();
		app.setSize(400,200);
		app.setVisible(true);
	}

}