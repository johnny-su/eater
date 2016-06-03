package com.example.eater;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import com.example.eater.message.runnable_receving;
import com.example.eater.message.sent_ending_listener_thread;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class comment extends Activity{
	
	ShareData sd;
	Handler handler;
	PrintWriter out;
	BufferedReader in;
	
	private int isrun=-1;
	
	private ImageView imageview;
	private TextView textview;
	private Button button;
	
	private ListView listview;
	private ArrayAdapter<String> adapter;
	
	private int cmsum=0;
	private String[] comment;
	
	private String foodspeak;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment);
		
		init();
	}
	
	public void init(){
		sd = (ShareData)getApplication();
		handler=new Handler();
		
		imageview=(ImageView)findViewById(R.id.cm_imageview);
		textview=(TextView)findViewById(R.id.cm_textview);
		button=(Button)findViewById(R.id.cm_button);
		button.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				turn_to_above();
			}
		});
		
		
		
		
		FileInputStream fis;
		try {
			fis = new FileInputStream("mnt/sdcard/wwwmyeater/"+sd.get_cm_foodid()+".jpeg");
			BitmapFactory.Options opt =new BitmapFactory.Options();
			opt.inPurgeable = true;
			Bitmap bitmap=BitmapFactory.decodeStream(fis,null,opt);
			
			imageview.setImageBitmap(bitmap);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
		
		listview=(ListView)findViewById(R.id.cm_listview);
		adapter=new ArrayAdapter<String>(this,  
                R.layout.msg_item,R.id.msg_textview);
		
		listview.setAdapter(adapter);
		
		cmsum=0;
		isrun=1;
		new Thread(new runnable_receving()).start();
	}
	
	//*******************************
		class runnable_receving implements Runnable{
			
			public void run(){
				
					//Looper.prepare(); 
				
					sd = (ShareData)getApplication();
					
					try {
						 if (sd.get_mysocket().isConnected()) {  
			                    if (!sd.get_mysocket().isInputShutdown()) { 
			                    	
			                    	in = new BufferedReader(new InputStreamReader(sd.get_mysocket().getInputStream(),"ISO-8859-1"));
			                       while(isrun==1){
			                    	   
			                    	   
			                    	   System.out.println("isrun ==1: "+isrun);
			                    	   if(isrun==-1){break;}
			                    	   String str =in.readLine();
			                    	   if(str!=null){//start to read the infomation type and  do it in loop
			                    		   
			                    		   System.out.println("isrun ==2: "+isrun);
			                    		   System.out.println("****the request type: "+str);
			                    		   
			                 
			                    	   
			                    	   
			                    	   if(isrun!=1){System.out.println("isrun="+isrun);break;}
			                    	   
			                    	   if(str.equals("%%loadcm")){
			                    		   
			                    		   cmsum=Integer.parseInt(in.readLine());
			                    		   comment=new String[cmsum];
			                    		   for(int i=0;i<cmsum;i++){
			                    			   
			                    			   String[] ss =new String(in.readLine().getBytes("iso-8859-1"),"utf-8").split("%%");
			                    			   comment[i]=ss[1]+"£º"+ss[2]+"  ÓÚ"+ss[3];
			                    			   
			                    		   }
			                    		   
			                    		   handler.post(r);
			                    		   
			                    	   }
			                    	   
			                    	   if(str.equals("%%loadfoodspeak")){
			                    		  foodspeak=new String(in.readLine().getBytes("iso-8859-1"),"utf-8"); 
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
		
		
	Runnable r= new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			for(int i=0;i<cmsum;i++){
			adapter.add(comment[i]);
			}
			
		}
		
	};
	Runnable r1= new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			textview.setText(foodspeak);
			
		}
		
	};
	
	public void turn_to_above(){
		isrun=-1;
		
		sent_ending_listener_thread p =new sent_ending_listener_thread();
		Thread t2 =new Thread(p);
		t2.start();
		
		Intent GoToPostpic = new Intent();
		   GoToPostpic.setClass(comment.this,Infrom.class);
			startActivity(GoToPostpic);
			finish();
		
	}
	
}
