package com.example.eater;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Post_pic extends Activity{
	ShareData sd ;
	BufferedReader in        = null;
    PrintWriter    out       = null;
    String imagepath=null;
	Button selectbutton=null;
	Button postbutton=null;
	ImageView imageview=null;
	EditText  edittext1=null;
	EditText  edittext2=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);
		
		init();
		
	}
	
	
	
	//----------------------------------------------------------------
	public void init(){
		sd = (ShareData)getApplication();
		selectbutton = (Button)findViewById(R.id.button1);
		postbutton   = (Button)findViewById(R.id.button2);
		edittext1     = (EditText)findViewById(R.id.edittext1);
		edittext2     = (EditText)findViewById(R.id.edittext2);
		
		selectbutton.setOnClickListener(new Button.OnClickListener(){  
            @Override  
            public void onClick(View v) {  
                Intent intent = new Intent();  
                /* ����Pictures����Type�趨Ϊimage */  
                intent.setType("image/*");  
                /* ʹ��Intent.ACTION_GET_CONTENT���Action */  
                intent.setAction(Intent.ACTION_GET_CONTENT);   
                /* ȡ����Ƭ�󷵻ر����� */  
                startActivityForResult(intent, 1);  
            }  
              
        });  
		
		postbutton.setOnClickListener(new Button.OnClickListener(){  
            @Override  
            public void onClick(View v) {  
            	post_image();
            }  
              
        }); 
		
	}
	
	
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (resultCode == RESULT_OK) {  
            Uri uri = data.getData();  
            Log.e("uri", uri.toString());  
            ContentResolver cr = this.getContentResolver();  
            try {
            	 Bitmap bitmap=null;
            	 BitmapFactory.Options opt =new BitmapFactory.Options();
     			opt.inSampleSize=8;
     			opt.inPurgeable = true; 
            	 try{
                      bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri),null,opt);  
            	 }catch (OutOfMemoryError e) {
     				
        			 System.out.println("------------imageview of getbitmap out of memery-------");   

        			}
                 imageview= (ImageView) findViewById(R.id.imageview1);  
                /* ��Bitmap�趨��ImageView */  
                imageview.setImageBitmap(bitmap);
                /*��ͼƬ·�������ȫ�ֱ���image path*/
                imagepath=getAbsoluteImagePath(uri);
                sd.set_imagepath(imagepath);
            } catch (FileNotFoundException e) {  
                Log.e("Exception", e.getMessage(),e);  
            }  
        }  
        super.onActivityResult(requestCode, resultCode, data);  
    }  

	
	
	//ת������
	public void turn_to_Forage_page(){
		//when the image upload successful show a dialog to user
		//new AlertDialog.Builder(this).setTitle("").setMessage("ͼƬ�ϴ��ɹ�").setPositiveButton("ȷ��", null).show();
		    Intent GoToForage = new Intent();
		    GoToForage.setClass(Post_pic.this, Forage.class);
			startActivity(GoToForage);
			finish();
	}
	
	//�ϴ���Ƭ
	public void post_image(){
		runnable_post_image tpi = new runnable_post_image();
		Thread t =new Thread(tpi);
		t.start();
		
		
		
	}
	
     //��uriת��ΪͼƬ����·��
	 protected String getAbsoluteImagePath(Uri uri)  
     {  
         // can post image  
         String [] proj={MediaStore.Images.Media.DATA};  
         Cursor cursor = managedQuery( uri,  
                         proj,                 // Which columns to return  
                         null,       // WHERE clause; which rows to return (all rows)  
                         null,       // WHERE clause selection arguments (none)  
                         null);                 // Order-by clause (ascending by name)  
          
         int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);  
         cursor.moveToFirst();  
            
         return cursor.getString(column_index);  
     }
	
	//Bitmapתbyte[]  
	    public static byte[] BitmapToBytes(Bitmap Bitmap) 
	    { 
	    	ByteArrayOutputStream ms = null; 
	       
	            ms = new ByteArrayOutputStream(); 
	            Bitmap.compress(CompressFormat.JPEG, 100, ms);//��bitmap100%������ѹ�� �� ms������
	            
	            
	            byte[] byteImage = ms.toByteArray();//ת��
	           
	         
	            //�����ڴ�
	            if(Bitmap != null && !Bitmap.isRecycled()){ 

	                    // ���ղ�����Ϊnull

	                    Bitmap.recycle(); 

	                    Bitmap = null; 

	            } System.gc();


	        
	            try {
					ms.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
	        
	            return byteImage;
	    } 
	  //byteתbitmap
		public Bitmap getbitmap(byte[] a){
			Bitmap bitmap=null;
			try{
			BitmapFactory.Options opt =new BitmapFactory.Options();
			opt.inSampleSize=4;
			opt.inPurgeable = true;  
			//bitmap = BitmapFactory.decodeByteArray(a, 0, a.length);
			bitmap=BitmapFactory.decodeFile(imagepath,opt);
			}catch (OutOfMemoryError e) {
				
			 System.out.println("------------getbitmap out of memery-------");   

			}

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
	        
	        //�����ڴ�
            if(image != null && !image.isRecycled()){ 

                    // ���ղ�����Ϊnull

            	image.recycle(); 

            	image = null; 

            } System.gc();
            

	        
	        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//��ѹ���������baos��ŵ�ByteArrayInputStream��  
	        Bitmap bitmap=null;
	        BitmapFactory.Options newOpts = new BitmapFactory.Options();
	        newOpts.inPurgeable = true;  //�ڴ治����ϵͳ���ղ����ڴ�
	        try{
	        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);//��ByteArrayInputStream��������ͼƬ  
	        }catch (OutOfMemoryError e) {
				
			 System.out.println("------------three bitmap out of memery-------");   

			}
	        
	        return bitmap;  
	    }  
		private Bitmap compressBitmap(Bitmap image) {  
		      
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();         
		    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
		    if( baos.toByteArray().length / 1024>1024) {//�ж����ͼƬ����1M,����ѹ������������ͼƬ��BitmapFactory.decodeStream��ʱ���    
		        baos.reset();//����baos�����baos  
		        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//����ѹ��50%����ѹ��������ݴ�ŵ�baos��  
		    }
		  //�����ڴ�
            if(image != null && !image.isRecycled()){ 

                    // ���ղ�����Ϊnull

            	image.recycle(); 

            	image = null; 

            } 
            System.gc();

		    ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());  
		    BitmapFactory.Options newOpts = new BitmapFactory.Options();
		    //��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��  
		    newOpts.inJustDecodeBounds = true;
		    newOpts.inPurgeable = true;  //�ڴ治����ϵͳ���ղ����ڴ�
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
		    try{
		    isBm = new ByteArrayInputStream(baos.toByteArray());  
		    bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);  
		    }catch (OutOfMemoryError e) {
				
			 System.out.println("------------second getbitmap out of memery-------");   

			}
		    
		    return compressImage(bitmap);//ѹ���ñ�����С���ٽ�������ѹ��  
		}  
//---------------------------------------------------
	class runnable_post_image implements Runnable{
		
		public void run(){
			try {
				 if (sd.get_mysocket().isConnected()) {  
	                   if (!sd.get_mysocket().isInputShutdown()) { 
	                   	
	                	   in = new BufferedReader(new InputStreamReader(sd.get_mysocket().getInputStream(),"ISO-8859-1"));
	                	   out =new PrintWriter(new OutputStreamWriter(sd.get_mysocket().getOutputStream(),"ISO-8859-1"));
	                	   
	                	   System.out.println("start to upload the image...");
	                	   System.out.println("uri:"+imagepath);
	                	   String image="";
                  		
                  	 		 
                      		 FileInputStream f = new FileInputStream (imagepath);
   	                    	 byte[] buffer = new byte[1024*1024*4]; 
   	                    	 int len=f.read(buffer);
   	                		 byte[] im =new byte[len];
   	                		 for(int j =0;j<len;j++){
   	                			 im[j]=buffer[j];
   	                		 }
   	                		 f.close();
   	                		 //��byteתΪbitmap���ٽ�bitmapѹ��������bitmapתΪbyte����]
   	                		 byte[] ima=BitmapToBytes(compressBitmap(getbitmap(im)));
   	                		 
   	                		 
   	                		 //----------------------------------------------
   	                		 FileOutputStream os = new FileOutputStream("mnt/sdcard/10.jpg");
   	                		 os.write(ima);
   	                		 os.close();
   	                		 
   	                    	 String s = new String(ima,"ISO-8859-1");
   	                		 
   	                    	 String image_length =ima.length+"\n";
   	                    	 //����ת�룬�������ķ�����
   	                    	 String foodname=new String(edittext1.getText().toString().getBytes("utf-8"),"iso-8859-1");
   	                    	 String foodspeak=new String(edittext2.getText().toString().getBytes("utf-8"),"iso-8859-1");
   	                    	 image="%%%"+"\n"+foodname+"\n"+foodspeak+"\n"+image_length+s; 
   	                    	 //image="%%%"+"\n"+edittext1.getText().toString()+"\n"+edittext2.getText().toString()+"\n"+image_length+s; 
         
	                		 out.println(image);
	                    	 out.flush();
	                    	 
	                    	 
	                    	 System.out.println("up-load_image finished");
	                   
	                	   
	                    	 turn_to_Forage_page();
	                    	 
	                	   
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
}
