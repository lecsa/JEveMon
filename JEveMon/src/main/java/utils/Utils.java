/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author lecsa
 */
public class Utils {
    
    public static Date getUTC(String dateString){
        Date d = null;
        try{
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            d =  df.parse(dateString);
        }catch(ParseException ex){
            System.out.println("PEX: "+ex.getMessage());
        }
       return d;
    }
    
    public static Date getNowUTC(){
    Date now = null;
        try{
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            now = df.parse(df.format(new Date()));
        }catch(ParseException ex){
            System.out.println("PEX: "+ex.getMessage());
        }
    return now;
    }
    public static String formatTime(int seconds){
        int days = seconds / (3600*24);
        int remainder = seconds - (days * 3600*24);
        int hours = remainder / 3600;
        remainder = remainder - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;
        return days+"d "+hours+ "h " + mins + "m " + secs + "s";
    }
    public static String formatInt(int num){
        String buffer = "";
        String numstr = Integer.toString(num);
        int n=0;
        for(int i=numstr.length()-1;i>=0;i--){
            buffer += Character.toString(numstr.charAt(i));
            n++;
            if( n == 3 && i != 0){
                n=0;
                buffer += ",";
            }
        }
        return new StringBuffer(buffer).reverse().toString();
    }
    public static String formatLong(long num){
        String buffer = "";
        String numstr = Long.toString(num);
        int n=0;
        for(int i=numstr.length()-1;i>=0;i--){
            buffer += Character.toString(numstr.charAt(i));
            n++;
            if( n == 3 && i != 0){
                n=0;
                buffer += ",";
            }
        }
        return new StringBuffer(buffer).reverse().toString();
    }
}
