/** Constants_API */
package com.mk4droid.IMC_Store;

/**
 * Customization for your application is feasible here.
 * HINT: Be careful when to use http:// or https:// in downloading and uploading data. It highly depends on your platform
 * HINT2: Be careful with the paths of your remote server. Paths may differ per implementation.

 * Constants visible everywhere in the app.
 * 
 * @author Dimitrios Ververidis, Dr.
 *         Post-doctoral Researcher, 
 *         Information Technologies Institute, ITI-CERTH,
 *         Thermi, Thessaloniki, Greece      
 *         ververid@iti.gr,  
 *         http://mklab.iti.gr
 *
 */
public class Constants_API {
	
	/** TAG for Log of messages to alleviate debugging */
	public static String TAG = "ImproveMyCity";
		
	//==================== Communication parameters =================
	/** Transmitting protocol */
	public static String COM_Protocol   = "http://";
	
	/** Secure transmitting protocol */
	public static String COM_Protocol_S = "https://";
	
    /** Server address. It can be a XXX.XXX.XXX.XXX address instead */
	public static String ServerSTR          =  "smartcity.thermi.gov.gr";
	
	/** Server path of application */
	public static String phpExec            = "/improve/el";
	
	/** Server path of issue images */ 
	public static String remoteImages      = "/improve/";
	
	/** Encryption key for transmitting password */
	public static String EncKey =  "0000000000000000"; 
		
	
	
	
	//====== GEOGRAPHIC  Limits ============
	/** Geographical limits (rectangle) from where issues can be sent */
	                                        // North   South    East      West              
    public static double[] AppGPSLimits =   {40.57,  40.41,    23.24,   22.93};

    //============ Gather usage analytics ================
	/** Key for Flurry analytics that monitors usage of application see www.flurry.com 
	 *  Use your own key. 
	 *  
	 *  To set default value (enable or disable) Flurry analytics see at res/myprefs.xml go to 
	 *     
	 * android:key="AnalyticsSW"
	 * 
	 * and set 
	 * android:defaultValue="true" or "false"
	 *     
	 */
	 public static String Flurry_Key = "00000000000000000000";

	
    //====== Google map api key ====================
    /**
     * Insert your key in res/string.xml -> google_maps_api_key
     * 
     * You should have two map api keys: 1) For debugging related to your android debug key 
     * 2) For release version related to your android release key. 
     * 
     * See in android developer about how to generate map api keys according to your android key.  
     */
	
	//===== UI default language ==========
	/**
	 * 	 To set UI default language see at res/myprefs.xml go to
	 * 
	 *    android:key="LanguageAR"
	 *    
	 *    and set 
	 *    
	 *   android:defaultValue="el"  or "en"
	 * 
	 */

}
