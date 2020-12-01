package com.brannik.system;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

public class Globals extends AppCompatActivity {

    private final SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static String URL = "http://192.168.0.101/api.php";
    public static String[] RANKS;

    public Globals(Context context) {
        sharedPreferences = context.getSharedPreferences("SystemData", Context.MODE_PRIVATE);
        RANKS = new String[3];
        RANKS[0] = "Гост";
        RANKS[1] = "Потребител";
        RANKS[2] = "Администратор";
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
    // setters
    public void setCredintials(int id,String usrName,String fName,String sName,int rank,int notifyMsg,int notifyReq,int active){
        editor = sharedPreferences.edit();
        editor.putString("username",usrName);
        editor.putString("f_name",fName);
        editor.putString("s_name",sName);
        editor.putInt("acc_id",id);
        editor.putInt("rank",rank);
        editor.putInt("notify_msg",notifyMsg);
        editor.putInt("notify_req",notifyReq);
        editor.putInt("active",active);
        editor.apply();

    }
    public void setUserExs(int j){
        editor = sharedPreferences.edit();
        editor.putInt("userEx",j);
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
