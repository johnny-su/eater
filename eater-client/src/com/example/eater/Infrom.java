package com.example.eater;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

import com.example.eater.Forage.sent_newwanted;
import com.example.eater.message.sent_ending_listener_thread;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Infrom extends Activity {
	
	ShareData sd;
	Handler handler;
	
	private ListView listview;
	private ArrayAdapter<String> adapter;
	
	private Button btnLoadMore;
	private Button goback_button;
	
	private String info=null;
	
	BufferedReader br;
	
	private int iscomment=0;
	private int ismessage=1;
	
	
	private int[] tagtype=new int[200];
	private int tagsum=0;
	private String[] tag=new String[200];
	private String[] tagname=new String[200];
	private String foodid=null;
	private String fromuserid=null;
	
	//loding unread info
	 RandomAccessFile rf = null; 
	 long len ;
	 long start ;   
     long nextend;
     
    
     int c = -1;
	 
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newinfo);
		try {
			init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void init() throws IOException{
		
		sd=(ShareData)getApplication();
		handler=new Handler();
		
		listview = (ListView) findViewById(R.id.newinfo_listview); 
		
		View footView = getLayoutInflater().inflate(R.layout.loadmore, null);
		
		btnLoadMore = (Button) footView.findViewById(R.id.btnLoadMore);
		goback_button=(Button) findViewById(R.id.newinfo_button);
				
		listview.addFooterView(footView);
		
		btnLoadMore.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				read_newinfo1();
			}
			
		});
		goback_button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				turn_to_Forage();
			}
			
		});
		
		
		adapter=new ArrayAdapter<String>(this,  
                R.layout.msg_item,R.id.msg_textview);
		listview.setAdapter(adapter);
		item_listen();
		
		//--------初始化
		
		 tagtype=new int[200];
		 tagsum=0;
		tag=new String[200];
		tagname=new String[200];
		//----------------
		
		
		File f =new File("mnt/sdcard/wwwmyeater/loginuser/"+sd.get_loginid()+"/socialcache/newinfo");
		//File f =new File("mnt/sdcard/justin.txt");
		try {
			
			//br =new BufferedReader(new FileReader(f));
			 //read_newinfo();
			
			
			
		    rf = new RandomAccessFile(f, "r");
			 len = rf.length();
			 start = rf.getFilePointer();   
	         nextend = start + len - 1;
	         System.out.println(len+"--"+start+"--"+nextend);
	         
			read_newinfo1();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	public void read_newinfo(){
		
		
		try {
			String str=br.readLine();
			int readsum=0;
			System.out.println("str:"+str);
			while(str!=null&&readsum<10){
				
				//System.out.println(str);
				String[] ss=str.split("%%");
				System.out.println("info-type:"+ss[0]);
				
				if(ss[0].equals("新评论")){
				
				
				info=ss[0]+":"+ss[3] +"评论了你的图片说："+ss[5];
				
				
				
				tagtype[tagsum]=iscomment;
				tag[tagsum]=ss[2];
				tagsum++;
				
				adapter.add(info);
				item_listen();
				
				}
				if(ss[0].equals("新私信")){
				
			    info=ss[0]+":"+ss[2] +"发来私信说："+ss[3];
			    
			    
			    tagtype[tagsum]=ismessage;
				tag[tagsum]=ss[1];
				tagname[tagsum]=ss[2];
				tagsum++;
				
				adapter.add(info);
				item_listen();
				}
				
				str=br.readLine();
				readsum++;
			
			}
			//br.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void read_newinfo1(){
		//System.out.println("come to new info1 -----------");
		 //every time just read ten pieces new info
		if(rf!=null){
			 	
				try {
					 
					// long len = rf.length();
					 //long start = rf.getFilePointer();   
			         //long nextend = start + len - 1;
			        // System.out.println(len+"--"+start+"--"+nextend);
			         String line;
			         rf.seek(nextend);   
			          c = -1;
			         //int readsum=0;
			         for(int i=0;i<10;i++){
			        	 while(nextend > start) {   
			                 c = rf.read();   
			                 if (c == '\n' || c == '\r') {   
			                     line = rf.readLine();   
			                     
			                     System.out.println("readline done......");
			                     if(line!=null){
			                    	 System.out.println(new String(line.getBytes("iso-8859-1"),"utf-8"));
			                    	 String[] ss=new String(line.getBytes("iso-8859-1"),"utf-8").split("%%");
			         				System.out.println("info-type:"+ss[0]);
			         				
			         				if(ss[0].equals("新评论")){
			         				
			         				
			         				info=ss[0]+":"+ss[3] +"评论了你的图片说："+ss[5];
			         				
			         				
			         				
			         				tagtype[tagsum]=iscomment;
			         				tag[tagsum]=ss[2];
			         				tagsum++;
			         				
			         				adapter.add(info);
			         				item_listen();
			         				
			         				}
			         				if(ss[0].equals("新私信")){
			         				
			         			    info=ss[0]+":"+ss[2] +"发来私信说："+ss[3];
			         			    
			         			    
			         			    tagtype[tagsum]=ismessage;
			         				tag[tagsum]=ss[1];
			         				tagname[tagsum]=ss[2];
			         				tagsum++;
			         				
			         				adapter.add(info);
			         				item_listen();
			         				}
			                    	 
			                     }
			                     else{
			                    	 System.out.println("line is null...and i= "+i);
			                    	 //System.out.println(line); 
			                    	 
			                     }
			                     break;
			                     
			                 }
			                 
			                 nextend--;
			                 rf.seek(nextend);
			                
			        	 }
			        	 nextend--;   
		                 rf.seek(nextend);   
		                 if (nextend == 0) {// 当文件指针退至文件开始处，输出第一行   
		                     // System.out.println(rf.readLine()); 
		                	 String s =rf.readLine();
		                	 if(s!=null){
		                    	 String[] ss=s.split("%%");
		         				System.out.println("info-type:"+ss[0]);
		         				
		         				if(ss[0].equals("新评论")){
		         				
		         				
		         				info=ss[0]+":"+ss[3] +"评论了你的图片说："+ss[5];
		         				
		         				
		         				
		         				tagtype[tagsum]=iscomment;
		         				tag[tagsum]=ss[2];
		         				tagsum++;
		         				
		         				adapter.add(info);
		         				item_listen();
		         				
		         				}
		         				if(ss[0].equals("新私信")){
		         				
		         			    info=ss[0]+":"+ss[2] +"发来私信说："+ss[3];
		         			    
		         			    
		         			    tagtype[tagsum]=ismessage;
		         				tag[tagsum]=ss[1];
		         				tagname[tagsum]=ss[2];
		         				tagsum++;
		         				
		         				adapter.add(info);
		         				item_listen();
		         				}
		                    	 
		                     }
		                     else{
		                    	 System.out.println("line is null...and i= "+i);
		                     }
		                 }   
			         }
			         
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   
		         
		}
		else{
			
		}   
		
	}
	
	public void item_listen(){
		 listview.setOnItemClickListener(new OnItemClickListener() {  
			  
	            @Override  
	            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
	                    long arg3) {  
	                //setTitle("点击第"+arg2+"个项目");  
	                
	            	if(tagtype[arg2]==iscomment){
	            		foodid=tag[arg2];
	            		new Thread(new sent_loadcomment()).start();
	            		
	            		turn_to_comment();
	            		
	            	}
	            	else if(tagtype[arg2]==ismessage){
	            		fromuserid=tag[arg2];
	            		new Thread(new sent_loadmessage()).start();
	            		sd.set_to_userid(fromuserid);
	            		sd.set_to_username(tagname[arg2]);
	            		turn_to_message();
	            	}
	               
	               
	            }  
	            
	            
	        }); 
		 } 
	
	public void turn_to_comment(){
		
		Intent GoToPostpic = new Intent();
		   GoToPostpic.setClass(Infrom.this,comment.class);
			startActivity(GoToPostpic);
		finish();
	}
	public void turn_to_message(){
		
		Intent GoToPostpic = new Intent();
		   GoToPostpic.setClass(Infrom.this,message.class);
			startActivity(GoToPostpic);
		finish();
	}
	public void turn_to_Forage(){
		
		Intent GoToPostpic = new Intent();
		   GoToPostpic.setClass(Infrom.this,Forage.class);
			startActivity(GoToPostpic);
			finish();
		
	}
	
	class sent_loadcomment implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				PrintWriter out = new PrintWriter(new OutputStreamWriter( sd.get_mysocket().getOutputStream(),"ISO-8859-1"));
				sd.set_cm_foodid(foodid);
				out.println("%%loadcm\n"+foodid);
				out.flush();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
		
	}
	class sent_loadmessage implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				PrintWriter out = new PrintWriter(new OutputStreamWriter( sd.get_mysocket().getOutputStream(),"ISO-8859-1"));
				
				out.println("%%loadms\n"+fromuserid);
				out.flush();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	Runnable r =new Runnable(){
		public void run(){
			
			
			
		}
	};

}
