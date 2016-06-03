package com.example.eater;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Login_register extends Activity {

	private EditText un_edittext;
	private EditText pw_edittext;
	private EditText pws_edittext;
	private Button   sure_button;
	private Button   cancel_button;
	
	
	private ShareData sd;
	
	public Handler mHandler = new Handler() {  
	       public void handleMessage(Message msg) {  
	           super.handleMessage(msg);  
	          
	       }  
	   };
	   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_register);
		init();
		}
	
	
	
	public void init(){
		
		sd=(ShareData)getApplication();
		
		un_edittext=(EditText)this.findViewById(R.id.username_edittext);
		pw_edittext=(EditText)this.findViewById(R.id.password_edittext);
		pws_edittext=(EditText)this.findViewById(R.id.passwordofsure_edittext);
		sure_button=(Button)this.findViewById(R.id.sure_button);
		cancel_button=(Button)this.findViewById(R.id.cancel_button);
	
		
		
		sure_button.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				
				sent_registerinfo s =new sent_registerinfo();
				Thread t = new Thread(s);
				t.start();
				
			}
			
		}
			
		);
		
		cancel_button.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				turn_to_LoginPage();
			}
			
		}
			
		);
		
		
	}
	
	
	
	
	class sent_registerinfo implements Runnable{
		public void run(){
			Looper.prepare(); 
			sd = (ShareData)getApplication(); 
			try {
				 System.out.println("start to sent register");
				 if (sd.get_mysocket().isConnected()) {  
	                    if (!sd.get_mysocket().isInputShutdown()) { 
	                    	BufferedReader in = new BufferedReader(new InputStreamReader(sd.get_mysocket().getInputStream(),"ISO-8859-1"));
	                    	PrintWriter out =new PrintWriter(new OutputStreamWriter(sd.get_mysocket().getOutputStream(),"ISO-8859-1"));
	                    	
	                    	String username =new String(un_edittext.getText().toString().getBytes("utf-8"),"iso-8859-1");
	                    	
	                    	String password =pw_edittext.getText().toString();
	                    	String password2 =pws_edittext.getText().toString();
	                    	
	                    	
	                    	if(!password.equals(password2)){
	                    		System.out.println("密码确认不一致");
	                    		
	                    		//Looper.prepare();
	                    		new AlertDialog.Builder(Login_register.this)    
	                    		  
	                    		                .setTitle("")  
	                    		  
	                    		                .setMessage("密码输入不一致")  
	                    		  
	                    		                .setPositiveButton("确定", null)  
	                    		  
	                    		                .show();  
	                    		mHandler.sendMessage(mHandler.obtainMessage());
	                    		
	                    	}
	                    	else{
	                    	//sent register info
	                    	String a = "%%ll\n"+username+"\n"+password2+"\n";
	                    	
	                    	out.println(a);
	                    	out.flush();
	                    	System.out.println("--sent register info");
	                    	//receive info
	                    	 String str=null;
	              		   try {
	              			while ((str=in.readLine())!=null) {  
	              			      if(str.equals("%%ll")){
	              			    	  if(in.readLine().equals("registerfinish")){
	              			    		 String userid=in.readLine();
	              			    		  
	    	                    		new AlertDialog.Builder(Login_register.this)    
	    	                    		  
	    	                    		                .setTitle("")  
	    	                    		  
	    	                    		                .setMessage("注册成功！您的吃货号码为："+userid+"请牢记这个号码，因为登陆的时候需要用到这个id号，什么？为什么不能输入用户名或者邮箱来登陆而要记这个难记的号码？对不起，开发组全体成员只有一只程序狗。。所以。。")  
	    	                    		  
	    	                    		                .setPositiveButton("确定",new DialogInterface.OnClickListener() {
	    	                    		                    @Override
	    	                    		                    public void onClick(DialogInterface dialog, int which) {
	    	                    		                    	
	    	                    		                    	turn_to_LoginPage();
	    	            	              			    		 
	    	                    		                      
	    	                    		                    }
	    	                    		                })  
	    	                    		  
	    	                    		                .show();
	    	                    		mHandler.sendMessage(mHandler.obtainMessage());
	    	                    		//  turn_to_LoginPage();
	              			    		  break;
	              			    	  }
	              			    	  else{
	              			    		  System.out.println("unable to register");
	              			    		  break;
	              			    	  }
	              			      }
	              			}
	              		   } catch (IOException e) {
	              			// TODO Auto-generated catch block
	              			e.printStackTrace();
	              		      }
	                      }
	                    	// mHandler.sendMessage(mHandler.obtainMessage());  
	                    }
	                    else{System.out.println("scoket is Input Shutdown");}
	             
				 }
				 else{System.out.println("scoket is not Connected");}
			
			}catch(IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			 Looper.loop(); 
		}
	
	}
	
	public void turn_to_MainPage(){
		   Intent GoToForage = new Intent();
		   GoToForage.setClass(Login_register.this, Forage.class);
		   startActivity(GoToForage);
		   finish();
	   }
	
	public void turn_to_LoginPage(){
		   Intent GoToForage = new Intent();
		   GoToForage.setClass(Login_register.this, Login.class);
		   startActivity(GoToForage);
		   finish();
	   }
	
	/*public Handler mHandler = new Handler() {  
	       public void handleMessage(Message msg) {  
	           super.handleMessage(msg);  
	          
	       }  
	   };*/	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
