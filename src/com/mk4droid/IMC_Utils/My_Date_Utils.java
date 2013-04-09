/** My_Date_Utils */
package com.mk4droid.IMC_Utils;

import java.util.Date;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.content.res.Configuration;
import android.text.format.DateUtils;

/**
 *    Transform date or time to various formats
 * 
 * @author Dimitrios Ververidis, Dr.
 *         Post-doctoral Researcher, 
 *         Information Technologies Institute, ITI-CERTH,
 *         Thermi, Thessaloniki, Greece      
 *         ververid@iti.gr,  
 *         http://mklab.iti.gr
 *
 */

public class My_Date_Utils {
	
	//---------------- Convert date object to String --------------------
	/**
	 * Convert  date object  to  yyyy-MM-dd HH:mm:ss string  
	 * 
	 * @param date  date object
	 * @return
	 */
	public static String DateToString(Date date)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		StringBuilder sb = new StringBuilder( dateFormat.format( date ) );
		
		return sb.toString();
	}
	
	
	
    //---------------- Convert String to Date object --------------------
	/**
	 *  Convert "yyyy-MM-dd HH:mm:ss" string to date object
	 * 
	 * @param dateString
	 * @return
	 */
	public static Date ConvertToDate(String dateString){

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date convertedDate;
		try {
			convertedDate = dateFormat.parse(dateString);

		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

		return convertedDate;

	}

	
	
	//------------- Subtract CurrDateTime1 - DateTime2 as STR -----------------------
	/**
	 * Subtract old date from current date
	 * 
	 * @param Input_DateTime "yyyy-MM-dd HH:mm:ss"
	 * @return "X days, Y hours, and Z minutes"
	 * 
	 */
	public static String SubtractDate(String Input_DateTime){
		
		Date d1= new Date(Input_DateTime); 
		Date d2= new Date();               
		
		long MinutesLapsed  = calculateMinutes(d1, d2);
		
		if (MinutesLapsed> 0 && MinutesLapsed < 60)
			return Long.toString(MinutesLapsed) + " minutes";
		else if (MinutesLapsed > 60 && MinutesLapsed < 24*60){
			
						
			int Hours = (int) MinutesLapsed/60;
			int Minutes = (int) MinutesLapsed%60;
			
			return Integer.toString(Hours) + " hours and " + 
			       Integer.toString(Minutes) + " minutes";
		} else if (MinutesLapsed > 24*60){
			int Days = (int) MinutesLapsed/(24*60);
			
			String s="";
			if (Days>1)
				s = "s";
			
			return Integer.toString(Days) + " day" + s;
		} else if (MinutesLapsed <= 0){
			return "0 minutes";
		}
        
		return "";
	}
	

	/*====================  calculateMinutes  ========= */
	/**
	 *  This method is used to find the no of minutes between dates
	 * @param dateEarly
	 * @param dateLater
	 * @return
	 */
	public static long calculateMinutes(Date dateEarly, Date dateLater) {
	  return (dateLater.getTime() - dateEarly.getTime() ) / 1000 / 60; 
	}
	
	//------------- Date Num to STR -----------------------------------
	/**
	 * Convert "YYYY-MM-DD" to "Friday X Month Year"  
	 * 
	 * @param Date_In
	 * @param conf
	 * @return
	 */
	public static String dateNum2STR(String Date_In, Configuration conf){
		
	 String DayNo_STR   = Date_In.substring(8, 10);
   	 int DayNo          = Integer.parseInt(DayNo_STR);
   	 String MonthNo_STR = Date_In.substring(5, 7);
   	 int MonthNo        = Integer.parseInt(MonthNo_STR);
   	 String Month_STR   = formatMonth(Integer.parseInt(MonthNo_STR), conf.locale); 
   	 String Year_STR    = Date_In.substring(0, 4);
   	 int Year           = Integer.parseInt(Year_STR);
   	 
   	 
   	 Calendar myCal = new GregorianCalendar(Year, MonthNo-1, DayNo);
   	 int dayOfWeek = myCal.get(Calendar.DAY_OF_WEEK);    // 6=Friday
   	 	    	 
   	 String dayOfWeek_STR = DateUtils.getDayOfWeekString(dayOfWeek, DateUtils.LENGTH_MEDIUM); 
   	 
   	 return dayOfWeek_STR+ " " + DayNo_STR + " " + Month_STR  + " " + Year_STR;
	}
	
	//------------- Date STR to Num ----------- 
	/**
	 * Convert "Friday X Month Year" to "YYYY-MM-DD"  
	 * 
	 * @param Date_STR
	 * @param conf
	 * @return
	 */
	public static String dateSTR2Num(String Date_STR, Configuration conf){
		
		String[] DateARR   = Date_STR.split(" ");
				
		String DayNo    = DateARR[1];
		String YearSTR  = DateARR[3];
		
		//----- Month problem ------
		String MonthSTR = DateARR[2];
		
		DateFormatSymbols symbols = new DateFormatSymbols(conf.locale);
	    String[] monthNames = symbols.getMonths();
		
	    int iMonth=0;
	    
	    for(int i=0; i< monthNames.length; i++)
	        if(MonthSTR.equals( monthNames[i]))
	        	iMonth = i;
	      
	    String MonthNo = Integer.toString(iMonth+1);
		
	    if (MonthNo.length()<2)
	    	MonthNo = "0"+MonthNo;
		
	    return YearSTR+ "-" + MonthNo + "-" + DayNo;
	}
	//------------- Month Num to STR -----------------
	/**
	 * Convert 2 to February
	 * 
	 * @param month
	 * @param locale
	 * @return
	 */
	public static String formatMonth(int month, Locale locale) {
	    DateFormatSymbols symbols = new DateFormatSymbols(locale);
	    String[] monthNames = symbols.getMonths();
	    return monthNames[month - 1];
	}
	
	//-----------  minutesToTime  --------------------
	/**
	 * Convert 125 minutes to 2 hours and 5 minutes 
	 * 
	 * @param Minutes
	 * @return
	 */
    public static String minutesToTime(String Minutes){
		
		int min = Integer.parseInt(Minutes);
		
		int h = min / 60;
		int m = min % 60; 
		
		String hSTR = Integer.toString(h);
		String mSTR = Integer.toString(m);
		while (hSTR.length()<2) {hSTR = "0" + hSTR;} 
		while (mSTR.length()<2) {mSTR = "0" + mSTR;}
		
		return hSTR + ":" + mSTR;
		
	}

}
