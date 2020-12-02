package com.brannik.system;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.UUID;

public class Globals extends AppCompatActivity {

    private final SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static String URL = "http://app-api.servehttp.com/api.php";
    //public static String URL = "http://192.168.0.101/api.php";
    public static String[] RANKS;
    public static String[] SKLAD;

    public Globals(Context context) {
        sharedPreferences = context.getSharedPreferences("SystemData", Context.MODE_PRIVATE);
        RANKS = new String[3];
        RANKS[0] = "Гост";
        RANKS[1] = "Потребител";
        RANKS[2] = "Администратор";

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
    public int needUpdate(){
        return sharedPreferences.getInt("upd_state",0);
    }
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

    public int getCurrVersion(){
        return sharedPreferences.getInt("version",0);
    }


    // setters
    public void setVersion(int ver){
        editor = sharedPreferences.edit();
        editor.putInt("version",ver);
        editor.apply();
    }

    public void setCredintials(int id,String usrName,String fName,String sName,int rank,int notifyMsg,int notifyReq,int active,int sklad){
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
