/** SecurityMD5 */
package com.mk4droid.IMC_Services;

import org.json.JSONException;
import org.json.JSONObject;

import com.mk4droid.IMCity_PackDemo.R;
import com.mk4droid.IMC_Store.Constants_API;
import com.mk4droid.IMC_Store.Phptasks;
import com.mk4droid.IMC_Utils.My_Crypt_Utils;
import com.mk4droid.IMC_Utils.RestClient;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Authenticate the user using a username and password. Password is sent encrypted using the Constants.EncKey
 * See EncWrapper.
 * 
 * @copyright   Copyright (C) 2012 - 2013 Information Technology Institute ITI-CERTH. All rights reserved.
 * @license     GNU Affero General Public License version 3 or later; see LICENSE.txt
 * @author      Dimitrios Ververidis for the Multimedia Group (http://mklab.iti.gr). 
 *
 */
public class Security {
	static int tlv = Toast.LENGTH_LONG;
    
	//========================  AuthFun       ==============================================
	/**
	 * Authenticate user using username and password. Password is encrypted and then sent. 
	 * 
	 * @param Username   latin characters
	 * @param Password   actual password up to 16 chars (latin characters)
	 * @param resources  for UI
	 * @param ctx        for current Android activity
	 * @return
	 */
	public static boolean AuthFun(String Username, String Password, Resources resources, Context ctx){
		
		if (Username.equals(""))
			return false;
		
		boolean AuthFlag = false;
		
		String ServerSTR = Constants_API.ServerSTR;
		
		if (InternetConnCheck.getInstance(ctx).isOnline(ctx)) {
			
			String result =  Download_Data.RestCaller(
	    			Constants_API.COM_Protocol + ServerSTR + Constants_API.phpExec,
	    			                 RestClient.RequestMethod.GET, 
	    			                 new String[]{ "option","com_improvemycity",
	    					                       "task", Phptasks.TASK_AUTH_USER,
	        		                               "format",    "json",
	    					                       "username",   Username,
	    					                       "password",   EncWrapper(Password)}, "UTF-8");
			JSONObject jO = null;
			int UserID = 0; 
			
			if ( result != null ){
				try {
					jO = new JSONObject(result);
					UserID = jO.getInt("id");
				} catch (JSONException e1) {
					Log.d(Constants_API.TAG, "SecurityMD5:AuthFun:"+e1.getMessage());
				}
			}
			
			if (UserID != 0)
				AuthFlag = true;
			
	    	//	--------------- parse json data --------------------------
	    	if (!AuthFlag){
	    		savePreferences("AuthFlag", false, "Boolean", ctx);
	    		return false;
	    	} else {
	    		String UserID_STR, UserRealName;
				try {
	            	UserID_STR     = jO.getString("id"); 
	            	UserRealName   = jO.getString("fullname");
	            	
	        		DatabaseHandler dbHandler = new DatabaseHandler(ctx);
	            	
		    	    if (!dbHandler.db.isOpen())
		      	    	dbHandler = new DatabaseHandler(ctx);
		    	    
		    		dbHandler.AddUpdUserVotes(Username, Password);
		    		dbHandler.db.close();
		    		
		    		savePreferences("UserID_STR", UserID_STR, "String", ctx);
		    		savePreferences("AuthFlag", true, "Boolean", ctx);
		    		savePreferences("UserRealName", UserRealName, "String", ctx);
		    		savePreferences("PasswordAR", Password, "String", ctx);
		    		savePreferences("UserNameAR", Username, "String", ctx);
		    		
		    		return true;
				} catch (JSONException e) {
					Log.e(Constants_API.TAG, "SecurityMD5:AuthFun:"+e.getMessage());
				}
      	   } 	    
    	} else {
	    	Toast.makeText(ctx, resources.getString(R.string.NoInternet), tlv).show();
	    	return false;
	    }
		
		return false;
	}
           
	//========================  EncWrapper       ==============================================
    /**
     * Encrypt Password using the EncKey and transform into string of hexadecimal chars 
     * 
     * @param Password
     * @return
     */
    public static String EncWrapper(String Password){
	    My_Crypt_Utils mcrypt = new My_Crypt_Utils();
	    String encrypted = null;
		try {
			encrypted = bytesToHex( mcrypt.encrypt(Password) );
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return encrypted;		
    }
    
  //======================== bytesToHex  ==============================================
    /**
     * Convert an array of bytes to a string of hexadecimals
     * 
     * @param data
     * @return
     */
    public static String bytesToHex(byte[] data){
    	if (data==null)
    		return null;

    	int len = data.length;
    	String str = "";
    	for (int i=0; i<len; i++) {
    		if ((data[i]&0xFF)<16)
    			str = str + "0" + java.lang.Integer.toHexString(data[i]&0xFF);
    		else
    			str = str + java.lang.Integer.toHexString(data[i]&0xFF);
    	}
    	return str;
    }
    
    //================== savePreferences =============================
    /**
	 * save in preferences if user is authenticated or not
	 * 
	 * @param key    "AuthFlag"
	 * @param value  boolean, true or false
	 * @param type "Boolean"
	 */
	private static void savePreferences(String key, Object value, String type, Context ctx){
    	SharedPreferences       shPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
    	SharedPreferences.Editor editor = shPrefs.edit();
    	
    	if (type.equals("String")) 
    		editor.putString(key, (String) value);
    	else 
    		editor.putBoolean(key, (Boolean) value);
    	
    	editor.commit();
    }

    
}


///**                Make_DBST
// *  Make DB stored md5+salt from the Password type in the registration Phase
// */
//public static String Make_DBST(String RegPass){
//	String salt  = Salt(32); 
//	String mymd5 = md5(RegPass+salt);
//	return mymd5+":"+salt;
//}
//
///** Make random character string of N chars from the pool of number and small case letters */   
//public static String Salt(int N){
//	//------------ chars pool in Salt -----------
//	char[] symbols = new char[36];
//	for (int idx = 0; idx < 10; ++idx)
//		symbols[idx] = (char) ('0' + idx);
//	for (int idx = 10; idx < 36; ++idx)
//		symbols[idx] = (char) ('a' + idx - 10);
//	//------------------------------------------
//	Random random = new Random();
//	char[] buf = new char[N];
//	for (int i = 0; i <N; ++i) 
//	      buf[i] = symbols[random.nextInt(symbols.length)];
//	
//	return new String(buf);
//}


///** Make random character string of N chars from the pool of number */
//public String SaltNumber(int len){
//	//------------ chars pool in Salt -----------
//	char[] symbols = new char[10];
//	for (int idx = 0; idx < 10; ++idx)
//		symbols[idx] = (char) ('0' + idx);
//	
//	//------------------------------------------
//	Random random = new Random();
//	char[] buf = new char[len];
//	for (int i = 0; i <len; ++i) 
//	      buf[i] = symbols[random.nextInt(symbols.length)];
//	return new String(buf);
//}

////---------------------------------------------------------------------
///**         Compare Typed Password With DB STored password          
// * ---------------------------------------------------------------------*/
//public static boolean Compare(String myTypedPass, String DBST){
//	
//    String[] hashparts = DBST.split(":");
//    String estimatedPartZero = md5(myTypedPass + hashparts[1]);
//    boolean FlagAuth = false;
//    
//    if (estimatedPartZero.equals(hashparts[0]))
//          FlagAuth = true;	
//	
//	return FlagAuth;
//}


////--------------------------------------
///**              Generate MD5            */
////--------------------------------------
//public static String md5(String myPass){
//	
//	MessageDigest digest;
//	String hash = "not ready";
//	
//    try {
//        digest = MessageDigest.getInstance("MD5");
//        byte utf8_bytes[] = myPass.getBytes();
//        digest.update(utf8_bytes,0,utf8_bytes.length);
//        hash = new BigInteger(1, digest.digest()).toString(16);
//    } 
//    catch (NoSuchAlgorithmException e) {
//        e.printStackTrace();
//    }
//	
//    
//    return hash;
//}
