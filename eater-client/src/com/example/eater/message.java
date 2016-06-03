package com.example.eater;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.eater.Forage.sent_backcomment;
import com.example.eater.Forage.sent_ending_listener_thread;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class message extends Activity {

	ShareData sd;
	Handler handler;
	
	private ListView listview;
	private ArrayAdapter<String> adapter;
	private EditText edittext;
	private Button button;
	private Button goback_button;
	
	PrintWriter out;
	BufferedReader in;
	
	private String messages;
	private String rec_msg;
	private String to_username;
	private String from_username;
	private String from_userid;
	private String[] fusername;
	private String[] context;
	private int msg_length=0;
	
	private int isrun=-1;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		init();
	}

	
	public void init(){
		
		
		sd=(ShareData)getApplication();
		handler=new Handler();
		
		edittext=(EditText)findViewById(R.id.edittext_msg);
		button=(Button)findViewById(R.id.button_msg);
		goback_button=(Button)findViewById(R.id.goback);
		
		
		listview=(ListView)findViewById(R.id.listview_msg);
		adapter=new ArrayAdapter<String>(this,  
                R.layout.msg_item,R.id.msg_textview);
		adapter.add("no message");
		listview.setAdapter(adapter);
		
		
		//-------底部按钮和输入框-----------
	/*	View footview = getLayoutInflater().inflate(R.layout.message_footer, null);
		edittext=(EditText)footview.findViewById(R.id.edittext_msg);
		button=(Button)footview.findViewById(R.id.button_msg);
		listview.addFooterView(footview);
		*/
		
		button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					
					messages=new String(edittext.getText().toString().getBytes("utf-8"),"ISO-8859-1");
					
					SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss ");
					Date curDate = new Date(System.currentTimeMillis());//获取当前时间
					String str = formatter.format(curDate);
					
					
					
					adapter.add(str+" "+"我："+edittext.getText().toString()+"\n");
					
					new Thread(new sent_newmessage()).start();
				    edittext.setText("");
					
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		});
		goback_button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
			
				turn_to_Forage();
				
			}
			});
		
		new Thread(new load_unread_msg()).start();
		
		isrun=1;
		new Thread(new runnable_receving()).start();
		
	}
	
	//--------------
	public void turn_to_Forage(){
		isrun=-1;
		
		sent_ending_listener_thread p =new sent_ending_listener_thread();
		Thread t2 =new Thread(p);
		t2.start();
		
		Intent GoToPostpic = new Intent();
		   GoToPostpic.setClass(message.this,Forage.class);
			startActivity(GoToPostpic);
			finish();
		
	}
	
	//---------------------------------------------------------------------
	Runnable r = new Runnable(){
		public void run(){
			
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss ");
			Date curDate = new Date(System.currentTimeMillis());//获取当前时间
			String str = formatter.format(curDate);
			
			
			
			adapter.add(str+" "+from_username+"："+rec_msg+"\n");
			
		}
	};
	
	Runnable r1 = new Runnable(){
		public void run(){
			
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss ");
			
			for(int i=0;i<msg_length;i++){
			Date curDate = new Date(System.currentTimeMillis());//获取当前时间
			String str = formatter.format(curDate);
			
			
			
			adapter.add(str+" "+fusername[i]+"："+context[i]+"\n");
			}
		}
	};
	
	
	
	//发送私信
	class sent_newmessage implements Runnable{
		public void run(){
			try {
				 if (sd.get_mysocket().isConnected()) {  
	              if (!sd.get_mysocket().isInputShutdown()) { 
	              	
	           	    
	              	
	           	     out =new PrintWriter(new OutputStreamWriter(sd.get_mysocket().getOutputStream(),"ISO-8859-1"));
	           	  
	           		 out.println("%%msg\n"+sd.get_to_userid()+"\n"+messages+"\n");
	               	 out.flush();
	               	 
	               	
	               	 
	               	 System.out.println("sent insert new message request successful!");
	              
	           	   
	               	
	           	   
	              }else{System.out.println("scoket is Input Shutdown");}
		             
					 }
					 else{System.out.println("scoket is not Connected");
				 }
			}catch(IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			
			
		}
	}
	
	class sent_ending_listener_thread implements Runnable{
		public void run(){
			try {
				 if (sd.get_mysocket().isConnected()) {  
	                  if (!sd.get_mysocket().isInputShutdown()) { 
	                  	
	               	   
	               	     out =new PrintWriter(new OutputStreamWriter(sd.get_mysocket().getOutputStream(),"ISO-8859-1"));
	               	  
	               		 out.println("%%changingactivity\n");
	                   	 out.flush();
	                   	 
	                   	 
	                   	 System.out.println("sent insert new wanted request successful!");
	                  
	               	   
	                   	
	               	   
	                  }else{System.out.println("scoket is Input Shutdown");}
	 	             
	 				 }
	 				 else{System.out.println("scoket is not Connected");
				 }
			}catch(IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			
			
		}
	}
	//*******************************
	class runnable_receving implements Runnable{
		
		public void run(){
			
				//Looper.prepare(); 
			
				sd = (ShareData)getApplication();
				
				try {
					 if (sd.get_mysocket().isConnected()) {  
		                    if (!sd.get_mysocket().isInputShutdown()) { 
		                    	
		                       
		                       while(isrun==1){
		                    	   
		                    	   in = new BufferedReader(new InputStreamReader(sd.get_mysocket().getInputStream(),"ISO-8859-1"));
		                    	   System.out.println("isrun ==1: "+isrun);
		                    	   if(isrun==-1){break;}
		                    	   String str =in.readLine();
		                    	   if(str!=null){//start to read the infomation type and  do it in loop
		                    		   
		                    		   System.out.println("isrun ==2: "+isrun);
		                    		   System.out.println("****the request type: "+str);
		                    		   
		                 
		                    	   
		                    	   
		                    	   if(isrun!=1){System.out.println("isrun="+isrun);break;}
		                    	   
		                    	   if(str.equals("%%msg")){
		                    		   
		                    		   
		                    		   from_userid=in.readLine();
		                    		   from_username=new String(in.readLine().getBytes("iso-8859-1"),"utf-8");
		                    		   rec_msg=new String(in.readLine().getBytes("iso-8859-1"),"utf-8");
		                    		   
		                    		   if(sd.get_to_userid()==null){
		                    		   sd.set_to_userid(from_userid);
		                    		   sd.set_to_username(from_username);
		                    		   
		                    		   handler.post(r);
		                    		   }
		                    		   else{
		                    			   if(sd.get_to_userid()==from_userid){
		                    				   handler.post(r);
		                    			   }
		                    			   else{
		                    				   //与某人聊天时，另一个人发来消息，则存入缓存文本
		                    				   BufferedWriter br =new BufferedWriter(new FileWriter("mnt/sdcard/wwwmyeater/loginuser/"+sd.get_loginid()+"/socialcache/newinfo",true));
		                    				   String s =new String(in.readLine().getBytes("iso-8859-1"),"utf-8");
		                       				   System.out.println("***第三方发来消息，存入缓存内容为: "+s);
		                       				   br.write(s+"\n"); 
		                       				   br.close();
		                    			   }
		                    		   }
		                    	   }
		                    	   
		                    	   //接收未读消息--20140821
		                    	   if(str.equals("%%loadms")){
		                    		   
		                    		 
		                    		   int sum = Integer.parseInt(in.readLine());
		                    		   msg_length=sum;
		                    		   fusername=new String[sum];
		                    		   context=new String[sum];
		                    		   
		                    		   for(int i=0;i<sum;i++){
		                    			String msg  = in.readLine();
		                    			String[] ss =msg.split("%%");
		                    			from_username=new String(ss[0].getBytes("iso-8859-1"),"utf-8");
		                    			rec_msg=new String((ss[1]+ss[2]).getBytes("iso-8859-1"),"utf-8");
		                    			
		                    			
		                    			fusername[i]=from_username;
		                    			context[i]=rec_msg;
		                    			//handler.post(r);
		                    		   }
		                    		   System.out.println("start to hanler post..");
		                    		   handler.post(r1);
		                    	   }
		                    	   
		                    	   
		                    	   
		                    	   
		                       }
		                    
		                    
		                       }}
		                    
		                    else{System.out.println("scoket is Input Shutdown");
		                    }
					 }
		             else{
		            	 System.out.println("scoket is not Connected");
		             }
					 
				}catch(IOException e){
					
					System.out.println("runnable_receving---exception...");
					
					
					}
				//Looper.loop();
				}
		}
	

	class load_unread_msg implements Runnable{//文本文件中读入未读的消息

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				File f =new File("mnt/sdcard/wwwmyeater/loginuser/"+sd.get_loginid()+"/"+sd.get_to_userid());
				
				if(f.exists()){
				 
				BufferedReader br =new BufferedReader(new FileReader(f));
				try {
					String str=br.readLine();
					while(str!=null){
						rec_msg=str;
						from_username=sd.get_to_username();
						handler.post(r);
						str=br.readLine();
						
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
				else;
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
	}

}
