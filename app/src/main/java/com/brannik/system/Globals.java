package com.brannik.system;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.UUID;

public class Globals extends AppCompatActivity {

    private final SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static String URL = "http://app-api.redirectme.net/api.php";
    public static String URLADMIN = "http://app-api.redirectme.net/admin/admin.php";
    //public static String URL = "http://192.168.0.105/api.php";

    public static String[] RANKS;
    public static String[] SKLAD;
    public static int newVersion;


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

    public int getCurrVersion(){
        return sharedPreferences.getInt("version",0);
    }


    // setters
    public void setVersion(int ver){
        editor = sharedPreferences.edit();
        editor.putInt("version",ver);
        editor.apply();
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

    public void setNotificationsCount(int count){
        editor = sharedPreferences.edit();
        editor.putInt("NOT_COUNT",count);
        editor.apply();
    }
    public int getNotificationsCount(){
        return sharedPreferences.getInt("NOT_COUNT",0);
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
