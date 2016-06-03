package com.example.eater;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.eater.Forage.runnable_load_info2;
import com.example.eater.Forage.runnable_load_lackingpic;
import com.example.eater.Forage.runnable_sentLoadMoreRequest;
import com.example.eater.Forage.runnable_sentReFreshRequest;
import com.example.eater.MyListView.OnRefreshListener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PersonalHome extends Activity {
	
	private ImageView avatar_imageview;
	private TextView  username_textview;
	private MyListView mlistview;
	private BufferedReader in;
	
	private int sum=0;
	private String[] foodname;
	private String[] foodspeak;
	private String[] foodlocation;
	
	
	Handler handler= new Handler(Looper.getMainLooper());
	ShareData sd ;
	private String[] lackingpic=new String [10];
	private int lackingsum=0;
	
	SimpleAdapter listItemAdapter=null;
	ArrayList<HashMap<String, Object>> listItem=null;
	HashMap<String, Object> map = new HashMap<String, Object>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personalpage);
		
		//handler = new Handler();
		init();
		
		
	}
	
	
	public void init(){
		
		sd = (ShareData)getApplication();
		
		avatar_imageview=(ImageView)findViewById(R.id.imageview1);
		username_textview=(TextView)findViewById(R.id.textview1);
		
		try {
			sd = (ShareData)getApplication();
			FileInputStream fis = new FileInputStream("mnt/sdcard/wwwmyeater/loginuser/"+sd.get_loginid()+"/"+sd.get_loginid()+".jpg");
			BitmapFactory.Options opt =new BitmapFactory.Options();
			opt.inPurgeable = true;
			Bitmap bitmap=BitmapFactory.decodeStream(fis,null,opt);
			
			avatar_imageview.setImageBitmap(bitmap);

			FileInputStream f = new FileInputStream("mnt/sdcard/wwwmyeater/loginuser/"+sd.get_loginid()+"/"+sd.get_loginid());
			
			InputStreamReader read = new InputStreamReader(f,"utf-8");//考虑到编码格式
	        BufferedReader bufferedReader = new BufferedReader(read);
	        String s=bufferedReader.readLine();
			System.out.println("userlogin:"+s);
	        
	        username_textview.setText(s);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		mlistview = (MyListView) findViewById(R.id.listview1); 
        
		
		
		// 生成动态数组，加入数据  
		 listItem   
			= new ArrayList<HashMap<String, Object>>();
  		//生成适配器的Item和动态数组对应的元素  
     	listItemAdapter = new SimpleAdapter(this,listItem,// 数据源   
         R.layout.personalpagelist_item,//ListItem的XML实现  
         // 动态数组与ImageItem对应的子项          
         new String[] {"food_imageview","speak_textview","comment_textview"},   
         // list_items的XML文件里面的一个Button,两个TextView ID  
         new int[] {R.id.imageview,R.id.textview1,R.id.textview2}  
     			);  
     	//------------------
     	 sent_FreshPersonalPageQuset s=new sent_FreshPersonalPageQuset();
         Thread l=new Thread(s);
         l.start();
      
         
         runnable_loadPersonalPageQuset r =new runnable_loadPersonalPageQuset(); 
 		Thread f =new Thread(r);
 		f.start();
     	 
     	//---------- 添加并且显示  
     	mlistview.setAdapter(listItemAdapter); 	
		
		
     	mlistview.setonRefreshListener(new OnRefreshListener() {  
   		  
            @Override  
            public void onRefresh() {  
                new AsyncTask<Void, Void, Void>() {  
                    protected Void doInBackground(Void... params) {  
                        try {  
                            Thread.sleep(1000);  
                        } catch (Exception e) {  
                            e.printStackTrace();  
                        }  
                        //刷新添加
                        new Thread(){  
                            public void run(){    
                                    
                                handler.post(r2);   
                                }  //先删除前面留下的所有item项目                   
                        }.start(); 
                       
                        sent_FreshPersonalPageQuset s=new sent_FreshPersonalPageQuset();
                        Thread l=new Thread(s);
                        l.start();
                     
                        
                        runnable_loadPersonalPageQuset r =new runnable_loadPersonalPageQuset(); 
                		Thread f =new Thread(r);
                		f.start();
 //------------------------------------------------------------------                       
                        return null;  
                    }  
  
                    @Override  
                    protected void onPostExecute(Void result) {  
                    	listItemAdapter.notifyDataSetChanged();  
                        mlistview.onRefreshComplete();  
                    }  
                }.execute(null, null, null);  
            }  
        });  
     	
     	//-----------------加载更多按钮
     			View footView = getLayoutInflater().inflate(R.layout.loadmore, null);
     			
     			Button btnLoadMore = (Button) footView.findViewById(R.id.btnLoadMore);
     			mlistview.addFooterView(footView);
     			btnLoadMore.setOnClickListener(new OnClickListener() {
     				   public void onClick(View v) {
     				    handler.post(new Runnable() {
     				     public void run() {
     				    	sent_LoadMorePersonalPageQuset  s=new sent_LoadMorePersonalPageQuset ();
     	                     Thread l=new Thread(s);
     	                     l.start();
     				    	 
     				    	 
     	                    runnable_loadPersonalPageQuset r =new runnable_loadPersonalPageQuset(); 
     	             		Thread f =new Thread(r);
     	             		f.start();
     				     }
     				    });
     				   }
     				  });
     			//-------------------
     	
     	
     	
     	
     	
	}
	
	public void setPersonalInfo() throws IOException{
		sd = (ShareData)getApplication();
		FileInputStream fis = new FileInputStream("mnt/sdcard/wwwmyeater/loginuser/"+sd.get_loginid()+"/"+sd.get_loginid()+".jpg");
		BitmapFactory.Options opt =new BitmapFactory.Options();
		opt.inPurgeable = true;
		Bitmap bitmap=BitmapFactory.decodeStream(fis,null,opt);
		
		avatar_imageview.setImageBitmap(bitmap);

		FileInputStream f = new FileInputStream("mnt/sdcard/wwwmyeater/loginuser/"+sd.get_loginid()+"/"+sd.get_loginid());
		
		InputStreamReader read = new InputStreamReader(f,"utf-8");//考虑到编码格式
        BufferedReader bufferedReader = new BufferedReader(read);
        String s=bufferedReader.readLine();
		System.out.println("userlogin:"+s);
        
        //username_textview.setText("jjj");
		
		
	}
	
	
	class sent_FreshPersonalPageQuset implements  Runnable{
		
		public void run(){
			try {
				 if (sd.get_mysocket().isConnected()) {  
	                   if (!sd.get_mysocket().isInputShutdown()) { 
	                   	
	                	   
	                	   PrintWriter out =new PrintWriter(new OutputStreamWriter(sd.get_mysocket().getOutputStream(),"ISO-8859-1"));
	                	  
	                		 out.println("%%rp\n");
	                    	 out.flush();
	                    	 
	                    	 
	                    	 System.out.println("sent refresh personal request successful!");
	                   
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
	
class sent_LoadMorePersonalPageQuset implements  Runnable{
		
		public void run(){
			try {
				 if (sd.get_mysocket().isConnected()) {  
	                   if (!sd.get_mysocket().isInputShutdown()) { 
	                   	
	                	   
	                	   PrintWriter out =new PrintWriter(new OutputStreamWriter(sd.get_mysocket().getOutputStream(),"ISO-8859-1"));
	                	  
	                		 out.println("%%mp\n");
	                    	 out.flush();
	                    	 
	                    	 
	                    	 System.out.println("sent refresh personal request successful!");
	                   
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
	
	
	
	class runnable_loadPersonalPageQuset implements  Runnable{
		public void run(){
			
			
			sd = (ShareData)getApplication();
			
			try {
				 if (sd.get_mysocket().isConnected()) {  
	                    if (!sd.get_mysocket().isInputShutdown()) { 
	                    	BufferedReader in = new BufferedReader(new InputStreamReader(sd.get_mysocket().getInputStream(),"ISO-8859-1"));
	                    	
	                    	
	                    	String str =in.readLine();
	                    	System.out.println("person request-type: "+str);
	                    	
	                    	if((str).equals("%%rp")){
	                    		int end=Integer.parseInt(in.readLine());
	                    		
	                    		
	                    		//------clean----------------
	                    		sum=0;
	                    		lackingsum=0;
	                    		for(int i=0;i<10;i++){
	                    			lackingpic[i]=null;
	                    		}
	                    		//---------clean---------
	                    		foodspeak=new String[end];
	                    		foodlocation=new String[end];
	                    		foodname=new String[end];
	                    		sum=end;
	                    		for(int k =0;k<end;k++){
	                    			in.readLine();
	                    		 System.out.println("now--strat---to---load---personalpage---info");
	               
	                    			 String s1=in.readLine();
	                    			 foodname[k]=s1;
	                    			 File f=new File( "mnt/sdcard/wwwmyeater/"+s1+".jpeg");
	                    			 if(!f.exists()){
	                    				 System.out.println("lackingpic!!:"+lackingsum+"---"+s1);
	                    				 lackingpic[lackingsum]=s1;
	                    				 lackingsum++;
	                    			 }
	                    			 else{
	                    				 
	                    			 }
	                        	//-----------------------------------------------------------		
	                        	 String speak=in.readLine();
	                        	 speak=new String(speak.getBytes("iso-8859-1"),"utf-8");
	                        	 foodspeak[k]=speak;
	                        	
	                        	 String location=in.readLine();
	                        	 foodlocation[k]=location;
	                        	 //location=new String(speak.getBytes("iso-8859-1"),"utf-8");
	                        	 
	                        	
	                    		 //----------------------
	                       /* 	 HashMap<String, Object> map = new HashMap<String, Object>();	 
	                        	 
	                                 map.put("food_imageview", "mnt/sdcard/wwwmyeater/"+s1+".jpeg");
	                                 
	                                 map.put("speak_textview", speak);
	                                 map.put("comment_textview", location);
	                                 listItem.add(map);*/
	                        	 
	                        	 
	                                 //Handler handler = new Handler(Looper.getMainLooper());
	                                 //handler.post(r1); 
	                    		 //    System.out.println("-------info finished:"+k);
	                    		
	                        /*         new Thread(){  
		                                 public void run(){    
		                                	 //listItemAdapter.notifyDataSetChanged();
		                                     handler.post(r1);   
		                                     }                     
		                            }.start();*/  
	                               //  listItemAdapter.notifyDataSetChanged();
	                    		    //item_listen();
	                    		    
	                    		   
		                       
	                    		}
	                    		//-----------------------
	                    		 new Thread(){  
	                                 public void run(){    
	                                	 //listItemAdapter.notifyDataSetChanged();
	                                     handler.post(r3);   
	                                     }                     
	                            }.start();  
	                    		//-----------------------
	                    		if(lackingsum!=0){
	                    			PrintWriter out = new PrintWriter(new OutputStreamWriter(sd.get_mysocket().getOutputStream(),"ISO-8859-1"));
	                    			String lackingpicname=lackingpic[0]+"\n";
	                    			for(int j=1;j<lackingsum;j++){
	                    			
	                    			String pic=lackingpic[j];
	                    			lackingpicname=lackingpicname+pic+"\n";
	                    			}
	                    			out.println("%%hj\n"+lackingsum+"\n"+lackingpicname);
	                    			out.flush();
	                    			System.out.println("sent lacking pic id successful!");
	                    			//--------------------
	                                runnable_load_lackingpic rl =new runnable_load_lackingpic();
	                       		 	Thread t=new Thread(rl);
	                       		 	t.start();
	                    			
	                    		}
	                    		
	                    	}
	                    
	                    }
	                    else{System.out.println("scoket is Input Shutdown");}
	             
				 }
				 else{System.out.println("scoket is not Connected");}
			
			}catch(IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			
		} 
	}  
	
	class runnable_load_lackingpic implements Runnable{
		
		public void run(){
			
			if (sd.get_mysocket().isConnected()) {  
	            if (!sd.get_mysocket().isInputShutdown()) { 
	            	try {
						in = new BufferedReader(new InputStreamReader(sd.get_mysocket().getInputStream(),"ISO-8859-1"));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	
	            	
	            	String str;
					try {
						str = in.readLine();
						System.out.println(str);
		            	
		            	if((str).equals("%%hj")){
		            		 int numb=Integer.parseInt(in.readLine());//没有缓存请求载入的图片数目 
		            		 System.out.println(numb);
		            		for(int i=0;i<numb;i++){
		            			String picname=in.readLine();
		            			
		            			
		            			
		            			 String s1=in.readLine();
	                    		 System.out.println("imagelength:"+s1);
	                    		 int len=Integer.parseInt(s1);
	                    		// System.out.println("image-length is----"+len);
	                    		 //in.readLine();
	                        	 int c=0;
	                        	 int count=0;
	                        	 byte[] im1 =new byte[1024*1024*4];
	                        	 
	                        	 while((c=in.read())!=-1){
	                        		 im1[count]=(byte)c;
	                        		 count++;
	                        		
	                        		 if(count==(len))break;
	                        	 }
	                        	 byte[] ima1=new byte[len];
	                        	 for(int h=0;h<len;h++){
	                        		 ima1[h]=im1[h];
	                        	 }
	                        	
	                    			 String bitname1 =picname;
	                    			 System.out.println(picname);
	                    			 saveMyBitmap(bitname1,getbitmap(ima1));
	                    			 System.out.println("write the lacking pic");
	                    			 
	                    			// Handler handler = new Handler(Looper.getMainLooper());
	                                // handler.post(r1); 
	                    			 new Thread(){  
	                    	             public void run(){    
	                    	                     
	                    	                 handler.post(r1);   
	                    	                 }                     
	                    	         }.start();   
		            		}
		            		
		            	}
						
			
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	
	            }
			}
			
			
		}
		
	}
	
	
	
	Runnable   r1=new  Runnable(){  
        @Override  
        public void run() {  
            //更新界面  
        	mlistview.setVisibility(View.GONE);
        	
        	
           listItemAdapter.notifyDataSetChanged();
           mlistview.setVisibility(View.VISIBLE);
           System.out.println("--------listview updata---------");
        }  
          
    };	
    Runnable   r2=new  Runnable(){  
        @Override  
        public void run() {  
            //更新界面  
           for(int i=listItem.size();i>0;i--){
        	   listItem.remove(i-1);
           }
           listItemAdapter.notifyDataSetChanged();
           System.out.println("--------load finished---------");
        }  
          
    };	
	
    
    
    Runnable   r3=new  Runnable(){  
        @Override  
        public void run() {  
            //更新界面  
          for(int i=0;i<sum;i++)
          {
        	  HashMap<String, Object> map = new HashMap<String, Object>();	 
         	 
              map.put("food_imageview", "mnt/sdcard/wwwmyeater/"+foodname[i]+".jpeg");
              
              map.put("speak_textview", foodspeak[i]);
              map.put("comment_textview", foodlocation[i]);
              listItem.add(map);
              item_listen();
              listItemAdapter.notifyDataSetChanged();
           }
           System.out.println("--------load finished---------");
        }  
          
    };	
	
	
	
	
	
	
	
	
	
    public void item_listen(){
    	mlistview.setOnItemClickListener(new OnItemClickListener() {  
			  
	            @Override  
	            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
	                    long arg3) {  
	                setTitle("点击第"+arg2+"个项目");  
	                  
	               
	                  
	               
	            }  
	        });  
	
		 // 添加长按点击  
	        mlistview.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {  
	              
	            @Override  
	            public void onCreateContextMenu(ContextMenu menu, View v,  
	                                            ContextMenuInfo menuInfo) {  
	                menu.setHeaderTitle("长按菜单-ContextMenu");     
	                menu.add(0, 0, 0, "弹出长按菜单0");  
	                menu.add(0, 1, 0, "弹出长按菜单1");     
	            }  
	        });   
	    
	
	
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//接收文件时使用bitmap
		public Bitmap getbitmap(byte[] a){
			Bitmap bitmap = BitmapFactory.decodeByteArray(a, 0, a.length); 
			return bitmap;
		}
	
	//保存bitmap文件
		public void saveMyBitmap(String bitName,Bitmap mBitmap) throws IOException {
	        File f = new File("mnt/sdcard/wwwmyeater/" + bitName + ".jpeg");
	        f.createNewFile();
	        FileOutputStream fOut = null;
	        try {
	                fOut = new FileOutputStream(f);
	        } catch (FileNotFoundException e) {
	                e.printStackTrace();
	        }
	        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
	        try {
	                fOut.flush();
	        } catch (IOException e) {
	                e.printStackTrace();
	        }
	        try {
	                fOut.close();
	        } catch (IOException e) {
	                e.printStackTrace();
	        }
	   }
		
	
	
	
}
