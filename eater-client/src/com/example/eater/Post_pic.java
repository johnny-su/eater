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
                /* 开启Pictures画面Type设定为image */  
                intent.setType("image/*");  
                /* 使用Intent.ACTION_GET_CONTENT这个Action */  
                intent.setAction(Intent.ACTION_GET_CONTENT);   
                /* 取得相片后返回本画面 */  
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
                /* 将Bitmap设定到ImageView */  
                imageview.setImageBitmap(bitmap);
                /*将图片路径保存给全局变量image path*/
                imagepath=getAbsoluteImagePath(uri);
                sd.set_imagepath(imagepath);
            } catch (FileNotFoundException e) {  
                Log.e("Exception", e.getMessage(),e);  
            }  
        }  
        super.onActivityResult(requestCode, resultCode, data);  
    }  

	
	
	//转到界面
	public void turn_to_Forage_page(){
		//when the image upload successful show a dialog to user
		//new AlertDialog.Builder(this).setTitle("").setMessage("图片上传成功").setPositiveButton("确定", null).show();
		    Intent GoToForage = new Intent();
		    GoToForage.setClass(Post_pic.this, Forage.class);
			startActivity(GoToForage);
			finish();
	}
	
	//上传照片
	public void post_image(){
		runnable_post_image tpi = new runnable_post_image();
		Thread t =new Thread(tpi);
		t.start();
		
		
		
	}
	
     //将uri转换为图片绝对路径
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
	
	//Bitmap转byte[]  
	    public static byte[] BitmapToBytes(Bitmap Bitmap) 
	    { 
	    	ByteArrayOutputStream ms = null; 
	       
	            ms = new ByteArrayOutputStream(); 
	            Bitmap.compress(CompressFormat.JPEG, 100, ms);//把bitmap100%高质量压缩 到 ms对象里
	            
	            
	            byte[] byteImage = ms.toByteArray();//转换
	           
	         
	            //回收内存
	            if(Bitmap != null && !Bitmap.isRecycled()){ 

	                    // 回收并且置为null

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
	  //byte转bitmap
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
		//bitmap压缩函数
		private Bitmap compressImage(Bitmap image) {  
			  
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
	        int options = 100;  
	        while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
	            baos.reset();//重置baos即清空baos  
	            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
	            options -= 10;//每次都减少10  
	        }
	        
	        //回收内存
            if(image != null && !image.isRecycled()){ 

                    // 回收并且置为null

            	image.recycle(); 

            	image = null; 

            } System.gc();
            

	        
	        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
	        Bitmap bitmap=null;
	        BitmapFactory.Options newOpts = new BitmapFactory.Options();
	        newOpts.inPurgeable = true;  //内存不足让系统回收部分内存
	        try{
	        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);//把ByteArrayInputStream数据生成图片  
	        }catch (OutOfMemoryError e) {
				
			 System.out.println("------------three bitmap out of memery-------");   

			}
	        
	        return bitmap;  
	    }  
		private Bitmap compressBitmap(Bitmap image) {  
		      
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();         
		    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
		    if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出    
		        baos.reset();//重置baos即清空baos  
		        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中  
		    }
		  //回收内存
            if(image != null && !image.isRecycled()){ 

                    // 回收并且置为null

            	image.recycle(); 

            	image = null; 

            } 
            System.gc();

		    ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());  
		    BitmapFactory.Options newOpts = new BitmapFactory.Options();
		    //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
		    newOpts.inJustDecodeBounds = true;
		    newOpts.inPurgeable = true;  //内存不足让系统回收部分内存
		    Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);  
		    newOpts.inJustDecodeBounds = false;  
		    int w = newOpts.outWidth;  
		    int h = newOpts.outHeight;  
		    //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
		    float hh = 800f;//这里设置高度为800f  
		    float ww = 480f;//这里设置宽度为480f  
		    //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
		    int be = 1;//be=1表示不缩放  
		    if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
		        be = (int) (newOpts.outWidth / ww);  
		    } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
		        be = (int) (newOpts.outHeight / hh);  
		    }  
		    if (be <= 0)  
		        be = 1;  
		    newOpts.inSampleSize = be;//设置缩放比例  
		    //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
		    try{
		    isBm = new ByteArrayInputStream(baos.toByteArray());  
		    bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);  
		    }catch (OutOfMemoryError e) {
				
			 System.out.println("------------second getbitmap out of memery-------");   

			}
		    
		    return compressImage(bitmap);//压缩好比例大小后再进行质量压缩  
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
   	                		 //把byte转为bitmap，再将bitmap压缩，最后把bitmap转为byte发送]
   	                		 byte[] ima=BitmapToBytes(compressBitmap(getbitmap(im)));
   	                		 
   	                		 
   	                		 //----------------------------------------------
   	                		 FileOutputStream os = new FileOutputStream("mnt/sdcard/10.jpg");
   	                		 os.write(ima);
   	                		 os.close();
   	                		 
   	                    	 String s = new String(ima,"ISO-8859-1");
   	                		 
   	                    	 String image_length =ima.length+"\n";
   	                    	 //文字转码，避免中文符出错
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
