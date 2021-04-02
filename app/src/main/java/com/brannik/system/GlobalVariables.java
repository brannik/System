package com.brannik.system;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.UUID;

import static java.lang.Integer.parseInt;

public class GlobalVariables extends AppCompatActivity {

    private final SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static String URL = "http://app-api.redirectme.net/api.php";
    public static String FILE_URL = "http://app-api.redirectme.net/app/app-debug.apk";
    //public static String URL = "http://192.168.0.105/api.php";

    public static String[] RANKS;
    public static String[] SKLAD;

    public GlobalVariables(Context context) {
        sharedPreferences = context.getSharedPreferences("SystemData", Context.MODE_PRIVATE);
        RANKS = new String[3];
        RANKS[0] = "Потр";
        RANKS[1] = "Мод";
        RANKS[2] = "Админ";

        SKLAD = new String[7];
        SKLAD[0] = "Няма";
        SKLAD[1] = "Първи";
        SKLAD[2] = "Втори";
        SKLAD[3] = "Мострена";
        SKLAD[4] = "Четвърти";
        SKLAD[5] = "Победа";
        SKLAD[6] = "Клетки";
    }

    // getters
    public static String getDevId(){
        return getUniquePsuedoID();
    }
    public int userExsi(){
        return sharedPreferences.getInt("userEx",0);
    }
    public int userRank() {
        return sharedPreferences.getInt("rank",0);
    }
    public String getUsername(){
        return sharedPreferences.getString("username","");
    }
    public int getAccId(){
        return sharedPreferences.getInt("acc_id",0);
    }
    public int getSklad(){
        return sharedPreferences.getInt("sklad",0);
    }
    public int getUnchecked(){
        return sharedPreferences.getInt("neotrazeni",0);
    }
    public int getNotCount(){
        return sharedPreferences.getInt("izvestiq",0);
    }
    public String getNames(){
        String temp="";
        temp += sharedPreferences.getString("f_name","");
        temp += " ";
        temp += sharedPreferences.getString("s_name","");
        return temp;
    }
    public String getankByIndex(int i){
        return RANKS[i];
    }
    public String getSkladByIndex(int y){
        return SKLAD[y];
    }

    public void setDateHours(int hours,int days){
        editor = sharedPreferences.edit();
        editor.putInt("EXTRA_HOURS",hours);
        editor.putInt("EXTRA_DAYS",days);
        editor.apply();
    }
    public String getExtraDays(){
        return String.valueOf(sharedPreferences.getInt("EXTRA_DAYS",0));
    }

    public String getExtraHours(){
        return String.valueOf(sharedPreferences.getInt("EXTRA_HOURS",0));
    }

    // setters

    public void setAppVersion(String ver){
        editor = sharedPreferences.edit();
        editor.putString("VERSION",ver);
        editor.apply();

    }
    public String getAppVersion(){
        return sharedPreferences.getString("VERSION","0");
    }

    public void setAppNewVersion(String ver,@Nullable String info){
        editor = sharedPreferences.edit();
        editor.putString("NEW_VERSION",ver);
        editor.putString("NEW_VERSION_INFO",info);
        editor.apply();
    }

    public String getAppNewVersionInfo(){
        return sharedPreferences.getString("NEW_VERSION_INFO","none");
    }
    public String getAppNewVersion(){
        return sharedPreferences.getString("NEW_VERSION","0");
    }

    public Boolean compareVersions(){
        // if else
        return parseInt(getAppVersion()) < parseInt(getAppNewVersion());
    }

    public String convertCurrVersion(){
        String txtVersion;
        LinkedList<Integer> stack = new LinkedList<Integer>();
        int ver = parseInt(getAppVersion());
        while(ver > 0){
            stack.push(ver % 10);
            ver = ver / 10;
        }
        txtVersion = "V" + stack.get(0) + "." + stack.get(1) + "." + stack.get(2);
        return txtVersion;
    }

    public String convertNewVersion(){
        String txtVersion;
        LinkedList<Integer> stack = new LinkedList<Integer>();
        int ver = Integer.parseInt(getAppNewVersion());
        String info;
        if(!getAppNewVersionInfo().equals("none")){
            info = getAppNewVersionInfo();
        }else{
            info = "";
        }
        while(ver > 0){
            stack.push(ver % 10);
            ver = ver / 10;
        }
        txtVersion = "V" + stack.get(0) + "." + stack.get(1) + "." + stack.get(2) + "\n" + info;
        return txtVersion;
    }

    public String convertNewVersionNoInfo(){
        String txtVersion;
        LinkedList<Integer> stack = new LinkedList<Integer>();
        int ver = Integer.parseInt(getAppNewVersion());
        while(ver > 0){
            stack.push(ver % 10);
            ver = ver / 10;
        }
        txtVersion = "V" + stack.get(0) + "." + stack.get(1) + "." + stack.get(2) ;
        return txtVersion;
    }


    public void setUnchecked(int count){
        editor = sharedPreferences.edit();
        editor.putInt("neotrazeni",count);
        editor.apply();
    }

    public void setNotCount(int count){
        editor = sharedPreferences.edit();
        editor.putInt("izvestiq",count);
        editor.apply();
    }

    public void StoreToken(String token){
        editor = sharedPreferences.edit();
        editor.putString("TOKEN",token);
        editor.apply();
    }

    public String getToken(){
        return sharedPreferences.getString("TOKEN",null);
    }


    public void setCredintials(int id,String usrName,String fName,String sName,int rank,int notifyMsg,int notifyReq,int active,int sklad,int unchecked,int notCount){
        editor = sharedPreferences.edit();
        editor.putString("username",usrName);
        editor.putString("f_name",fName);
        editor.putString("s_name",sName);
        editor.putInt("acc_id",id);
        editor.putInt("rank",rank);
        editor.putInt("notify_msg",notifyMsg);
        editor.putInt("notify_req",notifyReq);
        editor.putInt("active",active);
        editor.putInt("sklad",sklad);
        editor.putInt("neotrazeni",unchecked);
        editor.putInt("izvestiq",notCount);
        editor.apply();

    }
    public void setUserExs(int j){
        editor = sharedPreferences.edit();
        editor.putInt("userEx",j);
        editor.apply();
    }
    public void setNeedUpdate(int state){
        editor = sharedPreferences.edit();
        editor.putInt("upd_state",state);
        editor.apply();
    }


    private static String getUniquePsuedoID() {
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
        String serial = null;
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            serial = "serial"; // some value
        }
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }


}
