package com.example.eater;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.example.eater.R.color;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.*;


public class Login extends Activity  {
	
	private String ip= "192.168.1.102" ;
	
	ShareData sd = null;
	protected static final String DEBUG_TAG = null;
	private EditText username      = null;
	private EditText password      = null;
	private Button   login_button  = null;
	private Button   cancel_button = null;
	private Button   register_button = null;
	
	BufferedReader in        = null;
    PrintWriter    out       = null;
    Socket         client    = null;
    
    String LoginUser_name=null;
    String LoginUser_id=null;
	
    String RecentLogin=null;
    File reclog = new File("mnt/sdcard/wwwmyeater/RecentLogin");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login);
		
		login_button=(Button)findViewById(R.id.button1);
		
		//sub-thread for socket connection
		post p1 = new post();
		Thread t1 = new Thread(p1);
		t1.start();
		
		try {
			init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



//------------------------------子类---------------------------------------------	
	
	class MyOnClickListener implements OnClickListener {

		public void onClick(View v) {
			switch(v.getId()) {
			
			case R.id.button1:
				
				 try {
					 if (client.isConnected()) {  
		                    if (!client.isInputShutdown()) {
		                    	login_button.setBackgroundResource(color.white);
		                    	in = new BufferedReader(new InputStreamReader(client.getInputStream(),"ISO-8859-1"));
		                    	out = new PrintWriter(client.getOutputStream());
		                    	
		                    	String post_username=username.getText().toString();
		                    	String post_password=password.getText().toString();
		                    	String name_pw ="%%l"+"\n"+post_username+"%%%"+post_password;
		                    	//System.out.println(name_pw);
		                    	LoginUser_id=post_username;
		                        
		                    	sd.set_login_id(LoginUser_id);
                               	
		                    	
		                    	out.println(name_pw);
		                    	out.flush();
		                    	
		                    	//sub-thread for input output stream
		                    	match m1 = new match();
		                		Thread t2 = new Thread(m1);
		                		t2.start();	
		                    	
		                
		                    }
		                    
					 		else{
					 			System.out.println("Client is InPutShtDown");
					 		}
		              }
					 else{
						 System.out.println("failed to connect");
					 }
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	     
				
				break;
			case R.id.button2:
				finish();
				break;
				
			case R.id.button3:
				if (client.isConnected()) {  
				        if (!client.isInputShutdown()) {  
				        	//System.out.println();
				        	turn_to_RegisterPage();
				    
				        }
				        
				 		else{
				 			System.out.println("Client is InPutShtDown");
				 		}
				  }
				 else{
					 System.out.println("failed to connect");
				 }
				
				break;	
				
			default: break;	
			
			}
		}
		
	}
	
   class post implements Runnable { 
	    

	    public void run() { 
	    	 
	    	try {
				client=new Socket(ip,5555);//183.174.67.183  or 183.174.67.15  183.174.67.80  183.174.67.41
				sd = (ShareData)getApplication(); 
				sd.set_mysocket(client);
				
				System.out.println("Client-Accepted……");
			
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	
	    }
	    	
	        
	    } 
   class match implements Runnable { 
	   public void run() { 
		   String str=null;
		   try {
			while ((str=in.readLine())!=null) {  
			       if (client.isConnected()) {  
			           if (!client.isInputShutdown()) {  
			                  System.out.println("the header:"+str); 
			        	      if(str.equals("Hallelujah")){
			                   	File f =new File("mnt/sdcard/wwwmyeater/loginuser/"+LoginUser_id+"/");
			                	//File fv =new File("mnt/sdcard/wwwmyeater/loginuser/"+LoginUser_id+"/"+LoginUser_id+".jpg");
			                   		if(f.exists()){
			                   			
			                   			//写入最近登陆日志文件-------------------
			                   			FileOutputStream out =new FileOutputStream(reclog);
			                   			String info =username.getText().toString()+"%%%"+password.getText().toString();
			                   			
			                   			out.write(info.getBytes());
			                   			//------------------------
			                   			File f1 =new File("mnt/sdcard/wwwmyeater/loginuser/"+LoginUser_id+"/socialcache/newinfo");	
					                   	if(f1.exists()){
					                   		
					                   	}	
					                   	else{
					                   		f1.getParentFile().mkdirs();
					                   		FileOutputStream out1 =new FileOutputStream(f1);
					                   		out1.close();
					                   		System.out.println("build newinfo file successful...");
					                   	}	
			                   			//------------------------
			                   			System.out.println("1---come to the mainpage");
			                   			turn_to_MainPage();
			                   			break;
			                   		}
			                   		else{
			                		f.mkdirs();//创建文件夹
			                		//fv.mkdirs();
			                		out.println("%%lr");//发送下载登陆用户头像资料等信息
			                    	out.flush();
			                    	System.out.println("---build dir---");
			                    	
			                    	//------------------------
		                   			File f1 =new File("mnt/sdcard/wwwmyeater/loginuser/"+LoginUser_id+"/socialcache/newinfo");	
				                   	if(f1.exists()){
				                   		
				                   	}	
				                   	else{
				                   		f1.getParentFile().mkdirs();
				                   		FileOutputStream out1 =new FileOutputStream(f1);
				                   		out1.close();
				                   		System.out.println("build newinfo file successful...");
				                   	}	
		                   			//------------------------
			                    	
			                   		}
			                	   
			                         //	System.out.println("come to the mainpage");
			                   		//    turn_to_MainPage();
			                   		//    break;
			                   
			                   }
			                   if(str.equals("hallelujah2")){
			                	    
			                	   System.out.println("start to load userinfo");
			                	    String username=new String(in.readLine().getBytes("iso-8859-1"),"utf-8");
			                	    
			                	    
			                	    
			                	    File f1 =new File("mnt/sdcard/wwwmyeater/loginuser/"+LoginUser_id+"/"+LoginUser_id);
				                	File f2 =new File("mnt/sdcard/wwwmyeater/loginuser/"+LoginUser_id+"/"+LoginUser_id+".jpg");
			                	    
				                	FileOutputStream out1 =new FileOutputStream(f1);
				                	FileOutputStream out2 =new FileOutputStream(f2);
				                	
				                	
				                	//----------------reading pic
				                	 String s=in.readLine();
       	                    		 int len=Integer.parseInt(s);
       	                    		 System.out.println("image-length is----"+len);
       	                    		 
                                   	 int c=0;
                                   	 int count=0;
                                   	 byte[] im =new byte[1024*100];
                                   	 
                                   	 while((c=in.read())!=-1){
                                   		
                                   		 im[count]=(byte)c;
                                   		 count++;
                                   		 //System.out.println(count);
                                   		 if(count==(len))break;
                                   	 }
                                   	 byte[] ima=new byte[len];
                                   	 for(int h=0;h<len;h++){
                                   		 ima[h]=im[h];
                                   	 }
                                   	 //--------------finish reading pic--------------
                                   	System.out.println("start writing file");
                                   	out1.write(username.getBytes()); 
                                   	out2.write(ima);
                                   	
                                   	sd.set_login_id(LoginUser_id);
                                   	sd.set_login_user(username);
                                   	
                                   	//----------------------
                                   	FileOutputStream out =new FileOutputStream(reclog);
		                   			String info =LoginUser_id+"%%%"+password.getText().toString();
		                   			
		                   			out.write(info.getBytes());
		                   			
		                   			
                                   	//-----------------------
                                   	
                                   	System.out.println("----finish writing login profile---");
                                   	
                                   	out1.close();
                                   	out2.close();
				                	
				                	
				                	
			                	    System.out.println("2----come to the mainpage");
		                   			turn_to_MainPage();
		                   			break;
			                   }
			                   
			                  if(str.equals("didnt match")){
			                   	System.out.println("------unmatch!------");
			                   }
			                  
			                   
			                   
			                   
			                   mHandler.sendMessage(mHandler.obtainMessage());  
			              
			           }  
			       }  
			   }
			//System.out.println("str: "+str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	   }
   }
	
   
   //---------------------------------函数------------------------------------------
   
   public void init() throws IOException{
		username=(EditText)this.findViewById(R.id.edit_text);
		password=(EditText)this.findViewById(R.id.edit_text01);
		login_button = (Button)this.findViewById(R.id.button1);
		cancel_button = (Button)this.findViewById(R.id.button2);
		register_button=(Button)this.findViewById(R.id.button3);
		
		login_button.setOnClickListener(new MyOnClickListener());
		cancel_button.setOnClickListener(new MyOnClickListener());
		register_button.setOnClickListener(new MyOnClickListener());
		
		
		
		FileInputStream f;
		try {
			f = new FileInputStream(reclog);
			InputStreamReader read;
			
			if(reclog.exists()){
			try {
				read = new InputStreamReader(f,"utf-8");
				BufferedReader b = new BufferedReader(read);
				String a =b.readLine();
				String ss[] = a.split("%%%");
				
				username.setText(ss[0]);
				password.setText(ss[1]);
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			else{
				File f1=new File("mnt/sdcard/wwwmyeater/");
				if(f1.exists()){
					f1.mkdir();
					username.setText("请输入用户名");
					password.setText("请输入密码");
				}
				else{
					username.setText("请输入用户名");
					password.setText("请输入密码");
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}	
   
   public Handler mHandler = new Handler() {  
       public void handleMessage(Message msg) {  
           super.handleMessage(msg);  
          
       }  
   };	
		
  
   public void turn_to_MainPage(){
	   Intent GoToForage = new Intent();
	   GoToForage.setClass(Login.this, Forage.class);
		startActivity(GoToForage);
	  finish();
   }
   
   public void turn_to_RegisterPage(){
	   Intent GoToForage = new Intent();
	   GoToForage.setClass(Login.this, Login_register.class);
	   startActivity(GoToForage);
	   finish();
   }
   
   
	
	public void ShowDialog(String msg) {
        new AlertDialog.Builder(this).setTitle("notification").setMessage(msg)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
	
	
}
	
	
	
	

	

	


	


