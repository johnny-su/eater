package com.example.eater;

import java.net.Socket;

import android.app.Application;

public class ShareData extends Application {
 private Socket mysocket;
 private int forage_page_isloaded=-1;
 private String imagepath=null;
 
 
 private String login_user=null;
 private String login_id=null;
 
 private String to_userid=null;
 private String to_username=null;
 
 private String cm_foodid=null;
 public void set_mysocket(Socket s){
	 mysocket = s;
 }
 public Socket get_mysocket(){
	 return mysocket;
 }
 
 
 public void set_forage_page_isloaded(int i){
	 forage_page_isloaded=i;
 }
 public int get_forage_page_isloaded(){
	 return forage_page_isloaded;
 }
 
 
 public void set_imagepath(String s){
	 imagepath=s;
 }
 public String get_imagepath(){
	 return imagepath;
 }
 
 public void set_login_user(String s){
	 login_user=s;
 }
 public String get_loginuser(){
	 return login_user;
 }
 public void set_login_id(String s){
	 login_id=s;
 }
 public String get_loginid(){
	 return login_id;
 }
 public void set_to_userid(String s){
	 to_userid=s;
 }
 public String get_to_userid(){
	 return to_userid;
 }
 public void set_to_username(String s){
	 to_username=s;
 }
 public String get_to_username(){
	 return to_username;
 }
 public void set_cm_foodid(String s){
	 cm_foodid=s;
 }
 public String get_cm_foodid(){
	 return cm_foodid;
 }
}
