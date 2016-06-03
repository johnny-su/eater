package com.example.eater;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.example.eater.Login.MyOnClickListener;
import com.example.eater.MyListView.OnRefreshListener;
import com.example.eater.R.color;



import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
 

public class Forage extends Activity {
	//----------for sliding menu------
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private String[] itemcontent;
	private ImageView userimageview;
	private TextView  usertextview;
	//--------------------------------
	private int isrun = 1;
	//-------------for insert new wanted---------
	private int[] item_tag=new int[500];
	private int   tagnumber=0;
	private int   itemclickposition;
	
	private String comments=null;
	private String rec_msg=null;
	private String backuser=null;
	
	private String messages=null;
	//-----------------------------------------
	
	
	
	Button post_button=null;
	Button msg_button=null;
	//ListView list=null; 
	//SimpleAdapter listItemAdapter=null;
	MyListView list=null;
	SimpleAdapter listItemAdapter=null;
	ArrayList<HashMap<String, Object>> listItem=null;
	HashMap<String, Object> map = new HashMap<String, Object>();
	BufferedReader in        = null;
    PrintWriter    out       = null;
    ShareData sd ;
    
    private String fname;
    private String fspeak;
    private String use;
    private String avat;
    
    private String[] foodname;
    private String[] foodspeak;
    private String[] user;
    private String[] avatarname;
    private int sum=0;
    
    private String[] lackingpic=new String [10];
    private int lackingsum=0;
    
    private int yes=1;
    private int no=0;
    private int if_have_unreadcomment;
    
    int file_already=0;
    Handler handler=new Handler(Looper.getMainLooper());
    //Handler handler1;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forage1);
		
		//------
		
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebtn); 
		//------
		
		try {
			init_sliding_menu();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//----------
			//list = (ListView) findViewById(R.id.listView1);
			list = (MyListView) findViewById(R.id.listview1); 

			// ���ɶ�̬���飬��������  
			 listItem   
				= new ArrayList<HashMap<String, Object>>();
      		//������������Item�Ͷ�̬�����Ӧ��Ԫ��  
         	listItemAdapter = new SimpleAdapter(this,listItem,// ����Դ   
             R.layout.list_item1,//ListItem��XMLʵ��  
             // ��̬������ImageItem��Ӧ������          
             new String[] {"imageview1","username","foodinfo","button","imageview2","comment"},   
             // list_items��XML�ļ������һ��Button,����TextView ID  
             new int[] {R.id.imageview1,R.id.textview1,R.id.textview2,
         				R.id.textview3,R.id.imageview2,R.id.textview4}  
         			);  
         	
         	 	// ��Ӳ�����ʾ  
             	list.setAdapter(listItemAdapter); 	
		
        //----78990----------------------------------------
             	list.setonRefreshListener(new OnRefreshListener() {  
             		  
                    @Override  
                    public void onRefresh() {  
                        new AsyncTask<Void, Void, Void>() {  
                            protected Void doInBackground(Void... params) {  
                                try {  
                                    Thread.sleep(1000);  
                                } catch (Exception e) {  
                                    e.printStackTrace();  
                                }  
                                //ˢ�����
                                new Thread(){  
                                    public void run(){    
                                            
                                        handler.post(r2);   
                                        }  //��ɾ��ǰ�����µ�����item��Ŀ                   
                                }.start(); 
                                
                                runnable_sentReFreshRequest s=new runnable_sentReFreshRequest();
                                Thread l=new Thread(s);
                                l.start();
                             
                                
                       //         runnable_load_info2 r =new runnable_load_info2(); 
                        //		Thread f =new Thread(r);
                       // 		f.start();
         //------------------------------------------------------------------                       
                                return null;  
                            }  
          
                            @Override  
                            protected void onPostExecute(Void result) {  
                            	listItemAdapter.notifyDataSetChanged();  
                                list.onRefreshComplete();  
                            }  
                        }.execute(null, null, null);  
                    }  
                });  
             
		//------------------
		post_button= (Button)this.findViewById(R.id.button1);
		post_button.setBackgroundResource(color.darkblue);
		post_button.setOnClickListener(new MyOnClickListener());
		msg_button= (Button)this.findViewById(R.id.new_msg);
		msg_button.setBackgroundResource(color.darkwhite);
		msg_button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				handler.post(r6);
				//turn_to_messagepage();
				turn_to_infrom();
				
				
				
			}
			
		});
		
		//-----------------���ظ��ఴť
		View footView = getLayoutInflater().inflate(R.layout.loadmore, null);
		
		Button btnLoadMore = (Button) footView.findViewById(R.id.btnLoadMore);
		list.addFooterView(footView);
		btnLoadMore.setOnClickListener(new OnClickListener() {
			   public void onClick(View v) {
			    handler.post(new Runnable() {
			     public void run() {
			    	 runnable_sentLoadMoreRequest s=new runnable_sentLoadMoreRequest();
                     Thread l=new Thread(s);
                     l.start();
			    	 
			    	 
			  //  	runnable_load_info2 r =new runnable_load_info2(); 
             	//	Thread f =new Thread(r);
             //		f.start();
			     }
			    });
			   }
			  });
		//-------------------
		
		
		
		
		
		
		sd = (ShareData)getApplication();
		
		
		
		//***************
		
		
		
		runnable_receving r =new runnable_receving(); 
		Thread f =new Thread(r);
		f.start();
		//***************
		//handler = new Handler(); 
		new Thread(new sent_unreadcoment_request()).start();
		new Thread(new sent_unreadmessage_request()).start();
		
		if(sd.get_forage_page_isloaded()==-1){
			System.out.println("fist time to init");
			//handler = new Handler(); 
		//	init2();
		//	refresh_content();
			 runnable_sentReFreshRequest s=new runnable_sentReFreshRequest();
             Thread l=new Thread(s);
             l.start();
             
             
             
        //    runnable_load_info2 r =new runnable_load_info2(); 
     	//	Thread f =new Thread(r);
     	//	f.start();
			
			sd.set_forage_page_isloaded(1);
			
			//new Thread(new sent_unreadcoment_request()).start();
		}
		else{
			System.out.println("second time to init");
			
			runnable_sentReFreshRequest s=new runnable_sentReFreshRequest();
            Thread l=new Thread(s);
            l.start();
            
            
          // runnable_load_info2 r =new runnable_load_info2(); 
    	//	Thread f =new Thread(r);
    	//	f.start();
		//	init3();
		}
		
	/*	 Thread thread=new Thread(new sent_unreadcoment_request());
		
		thread.start();
		System.out.println("&&&&&&&&&&&&&&");
		*/
	}
	
	
	
	
	public void init(){
		
		  //refresh content
       // 
		
		//list = (ListView) findViewById(R.id.listView1);  

		// ���ɶ�̬���飬��������  
      //  ArrayList<HashMap<String, Object>> listItem   
      //      = new ArrayList<HashMap<String, Object>>();  
        for(int i=0;i<10;i++)  
        {  
            HashMap<String, Object> map = new HashMap<String, Object>();  
            
            map.put("imageview1", R.drawable.avatar);
            map.put("username","rertroog4det");
            map.put("foodinfo", "beijing haidian street zhongguan");
            map.put("button", "this is button place");
            
            map.put("imageview2", "mnt/sdcard/wwwmyeater/"+i+".jpeg");
            map.put("comment", "This is comment"+(i+1));
            listItem.add(map);  
        }  
        // ������������Item�Ͷ�̬�����Ӧ��Ԫ��  
       /* 	listItemAdapter = new SimpleAdapter(this,listItem,// ����Դ   
            R.layout.list_item,//ListItem��XMLʵ��  
            // ��̬������ImageItem��Ӧ������          
           new String[] {"imageview1","username","foodinfo","button","imageview2","comment"},   
            // list_items��XML�ļ������һ��Button,����TextView ID  
            new int[] {R.id.imageview1,R.id.textview1,R.id.textview2,
        				R.id.textview3,R.id.imageview2,R.id.textview4}  
        );  
        	
        	 // ��Ӳ�����ʾ  
            list.setAdapter(listItemAdapter); 	
            */
            //click item perform
           item_listen();
           
         
           
           
	}
	
	public void item_listen(){
		 list.setOnItemClickListener(new OnItemClickListener() {  
			  
	            @Override  
	            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
	                    long arg3) {  
	                setTitle("�����"+arg2+"����Ŀ");  
	                //-----------new wanted
	                itemclickposition=arg2;  
	               sent_newwanted sw = new sent_newwanted();
	                Thread t=new Thread(sw);
	                t.start();
	                
	                new AlertDialog.Builder(Forage.this)    
          		  
	                .setTitle("")  
	  
	                .setMessage("�����������")  
	  
	                .setPositiveButton("ȷ��", null)  
	  
	                .show();  
	                
	               
	            
	            }  
	            
	            
	        });  
	
		 // ��ӳ������  
	        list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {  
	              
	            @Override  
	            public void onCreateContextMenu(ContextMenu menu, View v,  
	                                            ContextMenuInfo menuInfo) {  
	                menu.setHeaderTitle("ѡ�����");     
	                menu.add(0, 0, 0, "�ͳ���˽��");  
	                menu.add(0, 1, 0, "���۸÷���");     
	            }  
	        });   
	    
	
	
	
	}
	
	
	@Override  
    public boolean onContextItemSelected(MenuItem item) { 
		
		AdapterView.AdapterContextMenuInfo menuInfo;
        menuInfo =(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        itemclickposition=menuInfo.position;
        
        
        
        //setTitle("����˳����˵�����ĵ�"+item.getItemId()+"����Ŀ");
        
        
        
        if(item.getItemId()==0){
        	
        	new Thread(new sent_findposter()).start();
        
        	turn_to_messagepage();
        	
        	
        }
        if(item.getItemId()==1){
        	
        	
        	
       /* 	new AlertDialog.Builder(Forage.this)    
  		  
            .setTitle("")  

            .setMessage("��������")  

            .setPositiveButton("ȷ��", null)  

            .show();     
        */	
        	final EditText edittext=new EditText(Forage.this);
        	
        	new AlertDialog.Builder(Forage.this)  
        	.setTitle("��������")  
        	.setIcon(android.R.drawable.ic_dialog_info)  
        	.setView(edittext)  
        	.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                	
                	comments=edittext.getText().toString();
                	System.out.println(comments);
                	sent_newcomment sw = new sent_newcomment();
                    Thread t=new Thread(sw);
                    t.start();
                	
			    		 
                  
                }
            })  
        	.setNegativeButton("ȡ��", null)  
        	.show();  
        	
        	
        	
        }
        
        return super.onContextItemSelected(item);  
    }  
	
	public void refresh_content(){
		load_image r =new load_image(); 
		Thread f =new Thread(r);
		f.start();
		
	}
	
	//---------------------------------------------
	public void init2(){
		
		  //refresh content
     // 
		
	//	list = (ListView) findViewById(R.id.listView1);  

		// ���ɶ�̬���飬��������  
    //  ArrayList<HashMap<String, Object>> listItem   
     //     = new ArrayList<HashMap<String, Object>>();  
      for(int i=0;i<10;i++)  
      {  
         // HashMap<String, Object> map = new HashMap<String, Object>();  
          
          map.put("imageview1", R.drawable.avatar);
          map.put("username","rertroog4det");
          map.put("foodinfo", "beijing haidian street zhongguan");
          map.put("button", "this is button place");
          
          map.put("imageview2", R.drawable.loading);
          map.put("comment", "This is comment"+(i+1));
          listItem.add(map);  
      }  
      // ������������Item�Ͷ�̬�����Ӧ��Ԫ��  
   //   	listItemAdapter = new SimpleAdapter(this,listItem,// ����Դ   
    //      R.layout.list_item,//ListItem��XMLʵ��  
     //     // ��̬������ImageItem��Ӧ������          
      //    new String[] {"imageview1","username","foodinfo","button","imageview2","comment"},   
          // list_items��XML�ļ������һ��Button,����TextView ID  
        //  new int[] {R.id.imageview1,R.id.textview1,R.id.textview2,
      	//			R.id.textview3,R.id.imageview2,R.id.textview4}  
      //);  
      	
      	 // ��Ӳ�����ʾ  
          list.setAdapter(listItemAdapter);  	
         
          //click item perform
         item_listen();
         }
	
	public void init3(){
//		list = (ListView) findViewById(R.id.listView1);  

		// ���ɶ�̬���飬��������  
 //     ArrayList<HashMap<String, Object>> listItem   
 //         = new ArrayList<HashMap<String, Object>>();
      //-------------
      HashMap<String, Object> map = new HashMap<String, Object>();  
      
      map.put("imageview1", R.drawable.avatar);
      map.put("username","rertroog4det");
      map.put("foodinfo", "beijing haidian street zhongguan");
      map.put("button", "this is button place");
      
      //map1.put("imageview2", sd.get_imagepath());
      map.put("imageview2", "mnt/sdcard/10.jpg");
      map.put("comment", "This is comment1");
      listItem.add(map);
      
      
      //-----------
  /*    for(int i=0;i<9;i++)  
      {  
          HashMap<String, Object> map = new HashMap<String, Object>();  
          
          map.put("imageview1", R.drawable.avatar);
          map.put("username","rertroog4det");
          map.put("foodinfo", "beijing haidian street zhongguan");
          map.put("button", "this is button place");
          
          map.put("imageview2", "mnt/sdcard/wwwmyeater/"+i+".jpeg");
          map.put("comment", "This is comment"+(i+2));
          listItem.add(map);  
      }  
      // ������������Item�Ͷ�̬�����Ӧ��Ԫ��  
      	listItemAdapter = new SimpleAdapter(this,listItem,// ����Դ   
          R.layout.list_item,//ListItem��XMLʵ��  
          // ��̬������ImageItem��Ӧ������          
          new String[] {"imageview1","username","foodinfo","button","imageview2","comment"},   
          // list_items��XML�ļ������һ��Button,����TextView ID  
          new int[] {R.id.imageview1,R.id.textview1,R.id.textview2,
      				R.id.textview3,R.id.imageview2,R.id.textview4}  
      );  
      	
      	 // ��Ӳ�����ʾ  
          list.setAdapter(listItemAdapter); 	
    */      
          //click item perform
         item_listen();
         new Thread(){  
             public void run(){    
                     
                 handler.post(r);   
                 }                     
         }.start();  
	}
	//-----------------------------------------------
	
	
	public void init4(){
		//����ˢ������
		
		
		//ˢ�����
        map.put("imageview1", R.drawable.avatar);
        map.put("username","rertroog4det");
        map.put("foodinfo", "beijing haidian street zhongguan");
        map.put("button", "this is button place");
        
        map.put("imageview2", R.drawable.loading);
        map.put("comment", "This is comment new");
        listItem.add(map);  
	}
	
	
	
	public void init_sliding_menu() throws IOException{
		sd = (ShareData)getApplication();
		userimageview=(ImageView)findViewById(R.id.userimageview);
		usertextview=(TextView)findViewById(R.id.usertextview);
		
		FileInputStream fis = new FileInputStream("mnt/sdcard/wwwmyeater/loginuser/"+sd.get_loginid()+"/"+sd.get_loginid()+".jpg");
		BitmapFactory.Options opt =new BitmapFactory.Options();
		opt.inPurgeable = true;
		Bitmap bitmap=BitmapFactory.decodeStream(fis,null,opt);
		
		userimageview.setImageBitmap(bitmap);
		
		
		FileInputStream f = new FileInputStream("mnt/sdcard/wwwmyeater/loginuser/"+sd.get_loginid()+"/"+sd.get_loginid());
		
		InputStreamReader read = new InputStreamReader(f,"utf-8");//���ǵ������ʽ
        BufferedReader bufferedReader = new BufferedReader(read);
		
        
		usertextview.setText(bufferedReader.readLine());
		
		System.out.println(usertextview.getText().toString());
		
		
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	    mDrawerList = (ListView) findViewById(R.id.listview);
	     
	     //set a custom shadow that overlays the main content when the drawer opens
	     mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
	     itemcontent=new String[] { "����", "�ҵ���ʳ", 
	                "����", "����", "�˳�"}; 
			
			
			// set up the drawer's list view with items and click listener
	        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
	                R.layout.slidingmenu_item,itemcontent));
	        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
	     
	}
	 /* The click listner for ListView in the navigation drawer */
    public class DrawerItemClickListener implements ListView.OnItemClickListener {
       @Override
       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           selectItem(position);
       }
   }
    private void selectItem(int position) {
    	if(position==0){
    		setTitle("����");
    		 new Thread(){  
                 public void run(){    
                         
                     handler.post(r2);   
                     }  //��ɾ��ǰ�����µ�����item��Ŀ                   
             }.start(); 
            
             sent_unreadcoment_request s=new sent_unreadcoment_request();
             Thread l=new Thread(s);
             l.start();
          
             
         //    runnable_load_info2 r =new runnable_load_info2(); 
     	//	Thread f =new Thread(r);
     	//	f.start();
    	}
    	
        if(position==2)setTitle("����˵�2��");
        if(position==1){
        	setTitle("����˵�1��");
        	
        	turn_to_personalhomepage();
        	
        	finish();
        }
        	
        	
        if(position==3)setTitle("����˵�3��");
        if(position==4){
        	setTitle("����˵�4��");
        	try {
				sd.get_mysocket().close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	finish();
        	}
    }

	
	
	
	
	
	public void turn_to_postpage(){
		isrun=-1;
		
		sent_ending_listener_thread p =new sent_ending_listener_thread();
		Thread t2 =new Thread(p);
		t2.start();
		
		Intent GoToPostpic = new Intent();
		   GoToPostpic.setClass(Forage.this,Post_pic.class);
			startActivity(GoToPostpic);
			finish();
		
	}
	
	public void turn_to_personalhomepage(){
		isrun=-1;
		sent_ending_listener_thread p =new sent_ending_listener_thread();
		Thread t2 =new Thread(p);
		t2.start();
		
		   Intent GoToPersonalhome = new Intent();
		   GoToPersonalhome.setClass(Forage.this,PersonalHome.class);
			startActivity(GoToPersonalhome);
			finish();
	}
	
	public void turn_to_messagepage(){
		isrun=-1;
		
		sent_ending_listener_thread p =new sent_ending_listener_thread();
		Thread t2 =new Thread(p);
		t2.start();
		
		Intent GoToMessage = new Intent();
		   GoToMessage.setClass(Forage.this,message.class);
			startActivity(GoToMessage);
			finish();
		
	}
	
	public void turn_to_infrom(){
		isrun=-1;
		
		sent_ending_listener_thread p =new sent_ending_listener_thread();
		Thread t2 =new Thread(p);
		t2.start();
		
		Intent GoToMessage = new Intent();
		   GoToMessage.setClass(Forage.this,Infrom.class);
			startActivity(GoToMessage);
			finish();
		
	}
	
	//�����ļ�ʱʹ��bitmap
	public Bitmap getbitmap(byte[] a){
		Bitmap bitmap = BitmapFactory.decodeByteArray(a, 0, a.length); 
		return bitmap;
	}
	//bitmapѹ������
	private Bitmap compressImage(Bitmap image) {  
		  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��  
        int options = 100;  
        while ( baos.toByteArray().length / 1024>100) {  //ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��         
            baos.reset();//����baos�����baos  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//����ѹ��options%����ѹ��������ݴ�ŵ�baos��  
            options -= 10;//ÿ�ζ�����10  
        }
        
        if(image != null && !image.isRecycled()){ 

            // ���ղ�����Ϊnull

    	image.recycle(); 

    	image = null; 

        } System.gc();
    
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//��ѹ���������baos��ŵ�ByteArrayInputStream��  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//��ByteArrayInputStream��������ͼƬ  
        return bitmap;  
    }  
	private Bitmap compressBitmap(Bitmap image) {  
	      
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();         
	    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
	    if( baos.toByteArray().length / 1024>1024) {//�ж����ͼƬ����1M,����ѹ������������ͼƬ��BitmapFactory.decodeStream��ʱ���    
	        baos.reset();//����baos�����baos  
	        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//����ѹ��50%����ѹ��������ݴ�ŵ�baos��  
	    }  
	    
	    if(image != null && !image.isRecycled()){ 

            // ���ղ�����Ϊnull

    	image.recycle(); 

    	image = null; 

        } System.gc();
	    
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());  
	    BitmapFactory.Options newOpts = new BitmapFactory.Options();  
	    //��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��  
	    newOpts.inJustDecodeBounds = true;  
	    Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);  
	    newOpts.inJustDecodeBounds = false;  
	    int w = newOpts.outWidth;  
	    int h = newOpts.outHeight;  
	    //���������ֻ��Ƚ϶���800*480�ֱ��ʣ����ԸߺͿ���������Ϊ  
	    float hh = 800f;//�������ø߶�Ϊ800f  
	    float ww = 480f;//�������ÿ��Ϊ480f  
	    //���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ�����ݽ��м��㼴��  
	    int be = 1;//be=1��ʾ������  
	    if (w > h && w > ww) {//�����ȴ�Ļ����ݿ�ȹ̶���С����  
	        be = (int) (newOpts.outWidth / ww);  
	    } else if (w < h && h > hh) {//����߶ȸߵĻ����ݿ�ȹ̶���С����  
	        be = (int) (newOpts.outHeight / hh);  
	    }  
	    if (be <= 0)  
	        be = 1;  
	    newOpts.inSampleSize = be;//�������ű���  
	    //���¶���ͼƬ��ע���ʱ�Ѿ���options.inJustDecodeBounds ���false��  
	    isBm = new ByteArrayInputStream(baos.toByteArray());  
	    bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);  
	    return compressImage(bitmap);//ѹ���ñ�����С���ٽ�������ѹ��  
	}  
	//����bitmap�ļ�
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
	//Bitmapתbyte[]  
    public static byte[] BitmapToBytes(Bitmap Bitmap) 
    { 
    	ByteArrayOutputStream ms = null; 
       
            ms = new ByteArrayOutputStream(); 
            Bitmap.compress(CompressFormat.JPEG, 20, ms);//��bitmap20%������ѹ�� �� ms������

            byte[] byteImage = ms.toByteArray();//ת��
           
            Bitmap.recycle();//
            System.gc();
        
            try {
				ms.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        
            return byteImage;
    } 
 
	
	
	//------------------------------------------------------
	class refresh implements  Runnable {
		public void run(){
			sd = (ShareData)getApplication(); 
			try {
				 if (sd.get_mysocket().isConnected()) {  
	                    if (!sd.get_mysocket().isInputShutdown()) { 
	                    	in = new BufferedReader(new InputStreamReader(sd.get_mysocket().getInputStream(),"ISO-8859-1"));
	                    	out = new PrintWriter(sd.get_mysocket().getOutputStream());
	                    	out.println("ssss");
	                    	out.flush();
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

	class upload_image implements Runnable{
		
		public void run(){
			sd = (ShareData)getApplication();
			
			try {
				 if (sd.get_mysocket().isConnected()) {  
	                    if (!sd.get_mysocket().isInputShutdown()) { 
	                    	
	                    	FileInputStream f = new FileInputStream ("mnt/sdcard/mmm.jpg");
	                    	byte[] buffer = new byte[23726]; 
	                    	f.read(buffer);
	                		String s = new String(buffer,"ISO-8859-1");
	                    	
	                    	
	                    	String image = "%%%"+"\n"+s;
	                    	System.out.println(s);
	                    
	                    	out = new PrintWriter(new OutputStreamWriter(sd.get_mysocket().getOutputStream(),"ISO-8859-1"));
	                    	out.println(image);
	                    	out.flush();
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
	
	class load_image implements Runnable{
		
		public void run(){
			
			sd = (ShareData)getApplication();
			
			try {
				 if (sd.get_mysocket().isConnected()) {  
	                    if (!sd.get_mysocket().isInputShutdown()) { 
	                    	in = new BufferedReader(new InputStreamReader(sd.get_mysocket().getInputStream(),"ISO-8859-1"));
	                    	
	                    	
	                    	String str =null;
	                    	
	                    	if((str=in.readLine()).equals("%%%")){
	                    		for(int k =0;k<10;k++){
	                    		 //FileOutputStream f = new FileOutputStream(new File("mnt/sdcard/"+k+".jpg"));
	                    		 System.out.println("now--strat---to---load---the---image");
	                    
	                    		 String s=in.readLine();
	                    		 int len=Integer.parseInt(s);
	                    		 System.out.println("image-length is----"+len);
	                    		 
                            	 int c=0;
                            	 int count=0;
                            	 byte[] im =new byte[1024*1024*4];
                            	 
                            	 while((c=in.read())!=-1){
                            		 im[count]=(byte)c;
                            		 count++;
                            		
                            		 if(count==(len))break;
                            	 }
                            	 byte[] ima=new byte[len];
                            	 for(int h=0;h<len;h++){
                            		 ima[h]=im[h];
                            	 }
                            	 
                            	 
                            	// f.write(ima);
                            	 System.out.println("----load finished---");
	                    		// f.close(); 
	                    		
	                    		 //ѹ��ͼƬ
	                    		 if(ima.length>=1024*100){
	                    		 Bitmap bitmap=compressBitmap(getbitmap(ima));
	                    		 String bitname =k+"";
	                    		 saveMyBitmap(bitname,bitmap);
	                    		 }
	                    		 else{
	                    			 String bitname =k+"";
	                    			 saveMyBitmap(bitname,getbitmap(ima));
	                    		 }
	                    		 //----------------------
	                    		}
	                    		 new Thread(){  
	                                 public void run(){    
	                                         
	                                     handler.post(r);   
	                                     }                     
	                             }.start();                        
	                  
	                    	
	                    		 
	                    		 
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
	
	Runnable   r=new  Runnable(){  
        @Override  
        public void run() {  
            //���½���  
           init();
           listItemAdapter.notifyDataSetChanged();
           System.out.println("--------load finished---------");
        }  
          
    };  
    Runnable   r1=new  Runnable(){  
        @Override  
        public void run() {  
            //���½���  
           //init();
           listItemAdapter.notifyDataSetChanged();
           System.out.println("--------load finished---------");
        }  
          
    };
    
    Runnable   r2=new  Runnable(){  
        @Override  
        public void run() {  
            //���½���  
           for(int i=listItem.size();i>0;i--){
        	   listItem.remove(i-1);
           }
           
           //------------item������tag��գ���Ŀ������Ϊ0
           tagnumber=0;
           item_tag=new int[500];
           //-----------item������tag��գ���Ŀ������Ϊ0
           
           listItemAdapter.notifyDataSetChanged();
           System.out.println("--------load finished---------");
        }  
          
    };
    
    
    
    Runnable   r3=new  Runnable(){  
        @Override  
        public void run() {  
            //���½���   
        for(int i=0;i<sum;i++){
	 HashMap<String, Object> map = new HashMap<String, Object>();	 
     
 	 map.put("imageview1", "mnt/sdcard/wwwmyeater/"+avatarname[i]+".jpeg");
     map.put("username",user[i]);
     map.put("foodinfo", "beijing haidian street zhongguan");
     map.put("button", "this is button place");
     
     map.put("imageview2", "mnt/sdcard/wwwmyeater/"+foodname[i]+".jpeg");
     map.put("comment", foodspeak[i]);
     listItem.add(map);  
 
    item_listen();
    listItemAdapter.notifyDataSetChanged();}
    System.out.println("--------load finished---------");
   
        }
};

Runnable   r4=new  Runnable(){  
    @Override  
    public void run() {  
        //���½���   
   
 HashMap<String, Object> map = new HashMap<String, Object>();	 
 
	 map.put("imageview1", "mnt/sdcard/wwwmyeater/"+avat+".jpeg");
 map.put("username",use);
 map.put("foodinfo", "beijing haidian street zhongguan");
 map.put("button", "this is button place");
 
 map.put("imageview2", "mnt/sdcard/wwwmyeater/"+fname+".jpeg");
 map.put("comment", fspeak);
 listItem.add(map);  

item_listen();
listItemAdapter.notifyDataSetChanged();
System.out.println("--------load finished---------");

    }
};


Runnable   r5=new  Runnable(){  
    @Override  
    public void run() {  
    	
    	//msg_button.setBackgroundColor(color.green);
    	//msg_button.setTextColor(color.red);
    	msg_button.setBackgroundResource(color.red);
    	
    }};

Runnable   r6=new  Runnable(){  
        @Override  
        public void run() {  
        	
        	//msg_button.setTextColor(color.blue);
        	msg_button.setBackgroundResource(color.white);
        	
        }};

    
    class MyOnClickListener implements OnClickListener {

		public void onClick(View v) {
			post_button.setBackgroundResource(color.white);
			turn_to_postpage();
		}
		}
    
class runnable_sentReFreshRequest implements Runnable{
		
		public void run(){
			try {
				 if (sd.get_mysocket().isConnected()) {  
	                   if (!sd.get_mysocket().isInputShutdown()) { 
	                   	
	                	   
	                	     out =new PrintWriter(new OutputStreamWriter(sd.get_mysocket().getOutputStream(),"ISO-8859-1"));
	                	  
	                		 out.println("%%r\n");
	                    	 out.flush();
	                    	 
	                    	 
	                    	 System.out.println("sent refresh request successful!");
	                   
	                    	 file_already=0;
	                    	    
	                    	 
	                    	 
	                	   
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

class runnable_sentLoadMoreRequest implements Runnable{
	
	public void run(){
		try {
			 if (sd.get_mysocket().isConnected()) {  
                   if (!sd.get_mysocket().isInputShutdown()) { 
                   	
                	   
                	     out =new PrintWriter(new OutputStreamWriter(sd.get_mysocket().getOutputStream(),"ISO-8859-1"));
                	  
                		 out.println("%%m\n");
                    	 out.flush();
                    	 
                    	 
                    	 System.out.println("sent refresh request successful!");
                   
                	   
                    	
                	   
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
    
class runnable_load_info implements Runnable{
	
	public void run(){
		
		Looper.prepare();
		
		sd = (ShareData)getApplication();
		
		try {
			 if (sd.get_mysocket().isConnected()) {  
                    if (!sd.get_mysocket().isInputShutdown()) { 
                    	in = new BufferedReader(new InputStreamReader(sd.get_mysocket().getInputStream(),"ISO-8859-1"));
                    	
                    	
                    	String str =in.readLine();
                    	System.out.println(str);
                    	
                    	if((str).equals("%%r")){
                    		int end=Integer.parseInt(in.readLine());
                    		
                    		for(int k =0;k<end;k++){
                    			in.readLine();
                    		 System.out.println("now--strat---to---load---the---info");
                    		 
                    		 String username=in.readLine();
                    		 username=new String(username.getBytes("iso-8859-1"),"utf-8");
                    		 System.out.println(username);
                    		 
                    		 
                    		 
                    		 String s=in.readLine();
                    		 
                    		 int len=Integer.parseInt(s);
                    		 System.out.println("image-length is----"+len);
                    		 
                        	 int c=0;
                        	 int count=0;
                        	 byte[] im =new byte[1024*1024*4];
                        	 
                        	 while((c=in.read())!=-1){
                        		 im[count]=(byte)c;
                        		 count++;
                        		
                        		 if(count==(len))break;
                        	 }
                        	 byte[] ima=new byte[len];
                        	 for(int h=0;h<len;h++){
                        		 ima[h]=im[h];
                        	 }
                        	 	 int a=k+file_already;
                    			 String bitname ="avatar"+a;
                    			 
                    			 saveMyBitmap(bitname,getbitmap(ima));
                    		//-----------------------------
                    			 in.readLine();
                    			 String s1=in.readLine();
                        		 System.out.println("imagelength:"+s1);
                        		 len=Integer.parseInt(s1);
                        		// System.out.println("image-length is----"+len);
                        		 //in.readLine();
                            	 c=0;
                            	 count=0;
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
                            	
                        			 String bitname1 =""+a;
                        			 
                        			 saveMyBitmap(bitname1,getbitmap(ima1));
                        			 in.readLine();
                        	 String speak=in.readLine();
                        	 speak=new String(speak.getBytes("iso-8859-1"),"utf-8");
                        			 
                        	// System.out.println("speak:"+speak);
                        	
                    		 //----------------------
                        	 HashMap<String, Object> map = new HashMap<String, Object>();	 
                        	     
                        	 	 map.put("imageview1", "mnt/sdcard/wwwmyeater/"+bitname+".jpeg");
                                 map.put("username",username);
                                 map.put("foodinfo", "beijing haidian street zhongguan");
                                 map.put("button", "this is button place");
                                 
                                 map.put("imageview2", "mnt/sdcard/wwwmyeater/"+bitname1+".jpeg");
                                 map.put("comment", speak);
                                 listItem.add(map);  
                    		 //    System.out.println("-------info finished:"+k);
                                 new Thread(){  
                                     public void run(){    
                                             
                                         handler.post(r1);   
                                         }                     
                                 }.start();                        
                      
                    		    
                           
                    		    item_listen();  
                    		}
                    		 new Thread(){  
                                 public void run(){    
                                         
                                     handler.post(r1);   
                                     }                     
                             }.start();                        
                  
                             file_already=file_already+end;
                    		
                    		 
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
    
//----------------------------------------------------------------
class runnable_load_info2 implements Runnable{
	
	public void run(){
		Looper.prepare(); 
		sd = (ShareData)getApplication();
		
		try {
			 if (sd.get_mysocket().isConnected()) {  
                    if (!sd.get_mysocket().isInputShutdown()) { 
                    	in = new BufferedReader(new InputStreamReader(sd.get_mysocket().getInputStream(),"ISO-8859-1"));
                    	
                    	
                    	String str =in.readLine();
                    	System.out.println(str);
                    	
                    	if((str).equals("%%r")){
                    		int end=Integer.parseInt(in.readLine());
                    		//------clean----------------
                    		sum=end;
                    		foodname=new String[end];
                    		foodspeak=new String[end];
                    		user=new String[end];
                    		avatarname=new String[end];
                    		lackingsum=0;
                    		for(int i=0;i<10;i++){
                    			lackingpic[i]=null;
                    		}
                    		//---------clean---------
                    		for(int k =0;k<end;k++){
                    			in.readLine();
                    		 System.out.println("now--strat---to---load---the---info");
                    		 
                    		 String username=in.readLine();
                    		 username=new String(username.getBytes("iso-8859-1"),"utf-8");
                    		 System.out.println(username);
                    		 user[k]=username;
                    		 use=username;
                    		 
                    		 String s=in.readLine();
                    		 
                    		 int len=Integer.parseInt(s);
                    		 System.out.println("image-length is----"+len);
                    		 
                        	 int c=0;
                        	 int count=0;
                        	 byte[] im =new byte[1024*1024*4];
                        	 
                        	 while((c=in.read())!=-1){
                        		 im[count]=(byte)c;
                        		 count++;
                        		
                        		 if(count==(len))break;
                        	 }
                        	 byte[] ima=new byte[len];
                        	 for(int h=0;h<len;h++){
                        		 ima[h]=im[h];
                        	 }
                        	 	 int a=k+file_already;
                    			 String bitname ="avatar"+a;
                    			 
                    			 saveMyBitmap(bitname,getbitmap(ima));
                    			 avatarname[k]=bitname;
                    			 avat=bitname;
                    		//-----------------------------
                    			 in.readLine();
                    			 String s1=in.readLine();
                    			 foodname[k]=s1;
                    			 
                    			 //-------Ϊÿ��item����һ��foodid��tag���Ա�ʵ�ֵ��޵����ݿ�洢
                    			 
                    			 item_tag[tagnumber]=Integer.parseInt(s1);
                    			 tagnumber++;
                    			 //-------Ϊÿ��item����һ��foodid��tag���Ա�ʵ�ֵ��޵����ݿ�洢
                    			 
                    			 fname=s1;
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
                        	 fspeak=speak;
                        	// System.out.println("speak:"+speak);
                        	
                    		 //----------------------
                       /* 	 HashMap<String, Object> map = new HashMap<String, Object>();	 
                        	     
                        	 	 map.put("imageview1", "mnt/sdcard/wwwmyeater/"+bitname+".jpeg");
                                 map.put("username",username);
                                 map.put("foodinfo", "beijing haidian street zhongguan");
                                 map.put("button", "this is button place");
                                 
                                 map.put("imageview2", "mnt/sdcard/wwwmyeater/"+s1+".jpeg");
                                 map.put("comment", speak);
                                 listItem.add(map);  
                    		 //    System.out.println("-------info finished:"+k);
                                // handler.post(r1);
                                // Handler handler = new Handler(Looper.getMainLooper());
                                // handler.post(r1); 
                                 
                    		    item_listen();  */
                        	 /*new Thread(){  
                                 public void run(){    
                                         
                                     handler.post(r4);   
                                     }                     
                             }.start();*/   
                    		}
                    		//-----------------------
                    		 new Thread(){  
                                 public void run(){    
                                         
                                     handler.post(r3);   
                                     }                     
                             }.start();     
                       
                    		//-----------------------
                    		if(lackingsum!=0){
                    			out = new PrintWriter(new OutputStreamWriter(sd.get_mysocket().getOutputStream(),"ISO-8859-1"));
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
                    		                   
                  
                             file_already=file_already+end;
                    		
                    	}
                    	if(str.equals("%%newinfo")){
                    		
                    		System.out.println("----------========--------=-=-----");
                    		new AlertDialog.Builder(Forage.this)    
                    		  
        	                .setTitle("")  
        	  
        	                .setMessage("���˵��������")  
        	  
        	                .setPositiveButton("ȷ��", null)  
        	  
        	                .show();  
                    	}
                    	
                    	
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
	            		 int numb=Integer.parseInt(in.readLine());//û�л������������ͼƬ��Ŀ 
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
                    			 Handler handler = new Handler(Looper.getMainLooper());
                                 handler.post(r1); 
                    			/* new Thread(){  
                    	             public void run(){    
                    	                     
                    	                 handler.post(r1);   
                    	                 }                     
                    	         }.start();*/   
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
//�������
class sent_newwanted implements Runnable{
	public void run(){
		try {
			 if (sd.get_mysocket().isConnected()) {  
                  if (!sd.get_mysocket().isInputShutdown()) { 
                  	
               	   
               	     out =new PrintWriter(new OutputStreamWriter(sd.get_mysocket().getOutputStream(),"ISO-8859-1"));
               	  
               		 out.println("%%w\n"+item_tag[itemclickposition-1]);
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

//��������
class sent_newcomment implements Runnable{
	public void run(){
		try {
			 if (sd.get_mysocket().isConnected()) {  
                if (!sd.get_mysocket().isInputShutdown()) { 
                	
             	    String s =new String(comments.getBytes("utf-8"),"ISO-8859-1");
                	
             	     out =new PrintWriter(new OutputStreamWriter(sd.get_mysocket().getOutputStream(),"ISO-8859-1"));
             	  
             		 out.println("%%c\n"+item_tag[itemclickposition-1]+"\n"+s+"\n");
                 	 out.flush();
                 	 
                 	 
                 	 System.out.println("sent insert new comment request successful!");
                
             	   
                 	
             	   
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
//�ظ�����
class sent_backcomment implements Runnable{
	public void run(){
		try {
			 if (sd.get_mysocket().isConnected()) {  
              if (!sd.get_mysocket().isInputShutdown()) { 
              	
           	    String s =new String(comments.getBytes("utf-8"),"ISO-8859-1");
              	
           	     out =new PrintWriter(new OutputStreamWriter(sd.get_mysocket().getOutputStream(),"ISO-8859-1"));
           	  
           		 out.println("%%cb\n"+backuser+"\n"+s+"\n");
               	 out.flush();
               	 
               	 
               	 System.out.println("sent insert new back-comment request successful!");
              
           	   
               	
           	   
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


class sent_unreadcoment_request implements Runnable{
	public void run(){
		try {
			 if (sd.get_mysocket().isConnected()) {  
                  if (!sd.get_mysocket().isInputShutdown()) { 
                  	
               	   
               	     out =new PrintWriter(new OutputStreamWriter(sd.get_mysocket().getOutputStream(),"ISO-8859-1"));
               	  
               		 out.println("%%unreadcoment\n");
                   	 out.flush();
                   	 
                   	 
                   	 System.out.println("sent insert unreadcoment request successful!");
                  
               	   
                   	
               	   
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

class sent_unreadmessage_request implements Runnable{
	public void run(){
		try {
			 if (sd.get_mysocket().isConnected()) {  
                  if (!sd.get_mysocket().isInputShutdown()) { 
                  	
               	   
               	     out =new PrintWriter(new OutputStreamWriter(sd.get_mysocket().getOutputStream(),"ISO-8859-1"));
               	  
               		 out.println("%%unreadmessage\n");
                   	 out.flush();
                   	 
                   	 
                   	 System.out.println("sent insert unreadcoment request successful!");
                  
               	   
                   	
               	   
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

//����˽��
class sent_newmessage implements Runnable{
	public void run(){
		try {
			 if (sd.get_mysocket().isConnected()) {  
              if (!sd.get_mysocket().isInputShutdown()) { 
              	
           	    String s =new String(messages.getBytes("utf-8"),"ISO-8859-1");
              	
           	     out =new PrintWriter(new OutputStreamWriter(sd.get_mysocket().getOutputStream(),"ISO-8859-1"));
           	  
           		 out.println("%%msg\n"+item_tag[itemclickposition-1]+"\n"+s+"\n");
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
//�ظ�˽��
class sent_backmessage implements Runnable{
	public void run(){
		try {
			 if (sd.get_mysocket().isConnected()) {  
            if (!sd.get_mysocket().isInputShutdown()) { 
            	
         	    String s =new String(messages.getBytes("utf-8"),"ISO-8859-1");
            	
         	     out =new PrintWriter(new OutputStreamWriter(sd.get_mysocket().getOutputStream(),"ISO-8859-1"));
         	  
         		 out.println("%%msgb\n"+backuser+"\n"+s+"\n");
             	 out.flush();
             	 
             	 
             	 System.out.println("sent insert new back-messages request successful!");
            
         	   
             	
         	   
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
//����˽������ȷ��˽�Ŷ���
class sent_findposter implements Runnable{
	public void run(){
		try {
			 if (sd.get_mysocket().isConnected()) {  
            if (!sd.get_mysocket().isInputShutdown()) { 
            	
         	   
            	
         	     out =new PrintWriter(new OutputStreamWriter(sd.get_mysocket().getOutputStream(),"ISO-8859-1"));
         	  
         		 out.println("%%msgf\n"+item_tag[itemclickposition-1]+"\n");
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
                    		   
                   //***********type-1**************************************************
                    		   if((str).equals("%%r")){//loading forage listview item
                           		int end=Integer.parseInt(in.readLine());
                           		//------clean----------------
                           		sum=end;
                           		foodname=new String[end];
                           		foodspeak=new String[end];
                           		user=new String[end];
                           		avatarname=new String[end];
                           		lackingsum=0;
                           		for(int i=0;i<10;i++){
                           			lackingpic[i]=null;
                           		}
                           		//---------clean---------
                           		for(int k =0;k<end;k++){
                           			in.readLine();
                           		 System.out.println("now--strat---to---load---the---info");
                           		 
                           		 String username=in.readLine();
                           		 username=new String(username.getBytes("iso-8859-1"),"utf-8");
                           		 System.out.println(username);
                           		 user[k]=username;
                           		 use=username;
                           		 
                           		 String s=in.readLine();
                           		 
                           		 int len=Integer.parseInt(s);
                           		 System.out.println("image-length is----"+len);
                           		 
                               	 int c=0;
                               	 int count=0;
                               	 byte[] im =new byte[1024*1024*4];
                               	 
                               	 while((c=in.read())!=-1){
                               		 im[count]=(byte)c;
                               		 count++;
                               		
                               		 if(count==(len))break;
                               	 }
                               	 byte[] ima=new byte[len];
                               	 for(int h=0;h<len;h++){
                               		 ima[h]=im[h];
                               	 }
                               	 	 int a=k+file_already;
                           			 String bitname ="avatar"+a;
                           			 
                           			 saveMyBitmap(bitname,getbitmap(ima));
                           			 avatarname[k]=bitname;
                           			 avat=bitname;
                           		//-----------------------------
                           			 in.readLine();
                           			 String s1=in.readLine();
                           			 foodname[k]=s1;
                           			 
                           			 //-------Ϊÿ��item����һ��foodid��tag���Ա�ʵ�ֵ��޵����ݿ�洢
                           			 
                           			 item_tag[tagnumber]=Integer.parseInt(s1);
                           			 tagnumber++;
                           			 //-------Ϊÿ��item����һ��foodid��tag���Ա�ʵ�ֵ��޵����ݿ�洢
                           			 
                           			 fname=s1;
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
                               	 fspeak=speak;
                              
                           		}
                           		//-----------------------
                           		 new Thread(){  
                                        public void run(){    
                                                
                                            handler.post(r3);   
                                            }                     
                                    }.start();     
                              
                           		//-----------------------
                           		if(lackingsum!=0){
                           			out = new PrintWriter(new OutputStreamWriter(sd.get_mysocket().getOutputStream(),"ISO-8859-1"));
                           			String lackingpicname=lackingpic[0]+"\n";
                           			for(int j=1;j<lackingsum;j++){
                           			
                           			String pic=lackingpic[j];
                           			lackingpicname=lackingpicname+pic+"\n";
                           			}
                           			out.println("%%hj\n"+lackingsum+"\n"+lackingpicname);
                           			out.flush();
                           			System.out.println("sent lacking pic id successful!");
                           			//--------------------
                                   //    runnable_load_lackingpic rl =new runnable_load_lackingpic();
                              	//	 	Thread t=new Thread(rl);
                              	//	 	t.start();
                           			
                           		}
                           		                   
                         
                                    file_already=file_already+end;
                                    
                                    //new Thread(new sent_unreadcoment_request()).start();
                           		
                           	}
                   //***********type-2***********************************888  
                    		   if((str).equals("%%hj")){
          	            		 int numb=Integer.parseInt(in.readLine());//û�л������������ͼƬ��Ŀ 
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
                              			 Handler handler = new Handler(Looper.getMainLooper());
                                           handler.post(r1); 
                              			/* new Thread(){  
                              	             public void run(){    
                              	                     
                              	                 handler.post(r1);   
                              	                 }                     
                              	         }.start();*/   
          	            		}
          	            		
          	            	}
                    		   
                   //***********type-1**************************************************
                    		   if((str).equals("%%c")){//����������Ϣʱ		   
                    		   
                    			   String setid=in.readLine();
                    			   final String setuser=new String(in.readLine().getBytes("ISO-8859-1"),"utf-8");
                    			   final String context =new String(in.readLine().getBytes("ISO-8859-1"),"utf-8");
                    			   backuser=setid;
                    			 
                    			  
                    				 //  handler.sendEmptyMessage(0); 
                    			   handler.post(new Runnable(){
                                       public void run(){
                                    	   new AlertDialog.Builder(Forage.this)    
                                  		  
                          	                .setTitle("")  
                          	  
                          	                .setMessage(setuser+"�����������Ƭ�� "+context)  
                          	  
                          	                 .setPositiveButton("�ظ�",new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                   	//=--------------
                                	  
                                	   final EditText edittext =new EditText(Forage.this);
                                	   
                                	   new AlertDialog.Builder(Forage.this)  
                                      	.setTitle("�ظ�����")  
                                      	.setIcon(android.R.drawable.ic_dialog_info)  
                                      	.setView(edittext)  
                                      	.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
                                              @Override
                                              public void onClick(DialogInterface dialog, int which) {
                                              	
                                              	comments=edittext.getText().toString();
                                              
                                              	sent_backcomment sw = new sent_backcomment();
                                                  Thread t=new Thread(sw);
                                                  t.start();
                                              	
                              			    		 
                                                
                                              }
                                          })  
                                      	.setNegativeButton("ȡ��", null)  
                                      	.show();  
                                	   
                                	   
                                //---=================
                                   	
                                     
                                   }
                                   })  
                           					.setNegativeButton("����", null) 
                          	  
                          	                .show();  
                                       }
                                       });   
                    			   
                    		   }
                    		   
                    		   if((str).equals("%%cb")){//���»ظ�������Ϣʱ		   
                        		   
                    			   String setid=in.readLine();
                    			   final String setuser=new String(in.readLine().getBytes("ISO-8859-1"),"utf-8");
                    			   final String context =new String(in.readLine().getBytes("ISO-8859-1"),"utf-8");
                    			   backuser=setid;
                    			 
                    			  
                    				 //  handler.sendEmptyMessage(0); 
                    			   handler.post(new Runnable(){
                                       public void run(){
                                    	   new AlertDialog.Builder(Forage.this)    
                                  		  
                          	                .setTitle("")  
                          	  
                          	                .setMessage(setuser+"�ظ���������ۣ� "+context)  
                          	  
                          	                 .setPositiveButton("�ظ�",new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                   	//=--------------
                                	  
                                	   final EditText edittext =new EditText(Forage.this);
                                	   
                                	   new AlertDialog.Builder(Forage.this)  
                                      	.setTitle("�ظ�����")  
                                      	.setIcon(android.R.drawable.ic_dialog_info)  
                                      	.setView(edittext)  
                                      	.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
                                              @Override
                                              public void onClick(DialogInterface dialog, int which) {
                                              	
                                              	comments=edittext.getText().toString();
                                              
                                              	sent_backcomment sw = new sent_backcomment();
                                                  Thread t=new Thread(sw);
                                                  t.start();
                                              	
                              			    		 
                                                
                                              }
                                          })  
                                      	.setNegativeButton("ȡ��", null)  
                                      	.show();  
                                	   
                                	   
                                //---=================
                                   	
                                     
                                   }
                                   })  
                           					.setNegativeButton("����", null) 
                          	  
                          	                .show();  
                                       }
                                       });   
                    			   
                    		   } 
                    		   
                  //***********type-unread-comment***********************************888     
                      		  if(str.equals("%%unreadcoment")){
                      			  
                      			   BufferedWriter br =new BufferedWriter(new FileWriter("mnt/sdcard/wwwmyeater/loginuser/"+sd.get_loginid()+"/socialcache/newinfo",true));
 	                    		   
                      			  
                      			  int sum =Integer.parseInt(in.readLine());
                      			  
                      			  for(int i=0;i<sum;i++){
                      				  
                      				  String s =new String(in.readLine().getBytes("iso-8859-1"),"utf-8");
                      				  System.out.println("***:"+s);
                      				//  String[] ss = s.split("%%*");
                      				  
                      				  br.write(s+"\n"); 
                      			  }
                      		  
                      			  br.close();
                      			  
                      			  if(sum==0){
                      				  if_have_unreadcomment=no;
                      				  
                      			  }else{
                      				  if_have_unreadcomment=yes;
                      				  System.out.println("д��ɹ�δ�����۳ɹ�");
                      				  handler.post(r5);
                      			  }
                      		  }
                 //***********type-unread-message***********************************888     
                      		  if(str.equals("%%unreadmessage")){
                      			  if(if_have_unreadcomment==no){
                      			   BufferedWriter br =new BufferedWriter(new FileWriter("mnt/sdcard/wwwmyeater/loginuser/"+sd.get_loginid()+"/socialcache/newinfo",true));
 	                    		   
                      			  
                      			  int sum =Integer.parseInt(in.readLine());
                      			  
                      			  for(int i=0;i<sum;i++){
                      				  
                      				  String s =new String(in.readLine().getBytes("iso-8859-1"),"utf-8");
                      				  System.out.println("***:"+s);
                      			
                      				  br.write(s+"\n"); 
                      			  }
                      		  
                      			  br.close();
                      			  System.out.println("д��ɹ�δ��˽�ųɹ�");
                      			  handler.post(r5);
                      			  }
                      			  else{
                      				 BufferedWriter br =new BufferedWriter(new FileWriter("mnt/sdcard/wwwmyeater/loginuser/"+sd.get_loginid()+"/socialcache/newinfo",true));
   	                    		   
                         			  
                         			  int sum =Integer.parseInt(in.readLine());
                         			  
                         			  for(int i=0;i<sum;i++){
                         				  
                         				  String s =new String(in.readLine().getBytes("iso-8859-1"),"utf-8");
                         				  System.out.println("***:"+s);
                         				//  String[] ss = s.split("%%*");
                         				  
                         				  br.write(s+"\n"); 
                         			  }
                         		  
                         			  br.close();
                         			  System.out.println("д��ɹ�δ��˽�ųɹ�");
                         			  handler.post(r5);
                      			  }
                      		  }
             
           //***********type-last***********************************888     
                    		  if(str.equals("%%msgf")){
                    			  sd.set_to_userid(in.readLine());
                    			  sd.set_to_username(new String(in.readLine().getBytes("iso-8859-1"),"utf-8"));
                    		  }
                    		   
                    		  if(str.equals("%%msg")){
	                    		   
                    			   
	                    		   String from_userid=in.readLine();
	                    		   String from_username=new String(in.readLine().getBytes("iso-8859-1"),"utf-8");
	                    		   rec_msg=new String(in.readLine().getBytes("iso-8859-1"),"utf-8");
	                    		   
	                    		   sd.set_to_userid(from_userid);
	                    		   sd.set_to_username(from_username);
	                    		   
	                    		   
	                    		   SimpleDateFormat formatter = new SimpleDateFormat ("HH:mm:ss ");
	                   			   Date curDate = new Date(System.currentTimeMillis());//��ȡ��ǰʱ��
	                   			   String date = formatter.format(curDate);
	                    		   
	                    		   
	                    		   BufferedWriter br =new BufferedWriter(new FileWriter("mnt/sdcard/wwwmyeater/loginuser/"+sd.get_loginid()+"/"+from_userid));
	                    		   br.write(date+"������Ϣ��"+rec_msg+"\n");
	                    		   br.close();
	                    		   
	                    		   handler.post(r5);
	                    		   
	                    	   }   
            
             
                    		
                     		  
                     		  
                     		  
                    	   }
                    	   
                 
                    	   
                    	   
                    	   
                    	   
                    	   
                    	   
                    	   
                    	   
                    	   
                    	   
                    	   
                    	   
                    	   
                    	   if(isrun!=1){System.out.println("isrun="+isrun);break;}
                    	   
                    	   
                       }
                    
                    
                    
                    }
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








}
