package com.brannik.system;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Integer.parseInt;

public class DatesOptionsAdaptor extends BaseAdapter {

    private ArrayList<String> list = new ArrayList<String>();
    private final Context context;
    private final View view;
    GlobalVariables globals = new GlobalVariables(MainActivity.getAppContext());
    Dialog messageDialog;
    public DatesOptionsAdaptor(ArrayList<String> list, Context context, View view) {
        this.list = list;
        this.context = context;
        this.view = view;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public String message = null;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_listview_extras, null);
        }

        TextView tvContact= (TextView)view.findViewById(R.id.txtDateText);

        ArrayList<String> listS = new ArrayList<String>(Arrays.asList(list.get(position).split("##")));
        if(parseInt(listS.get(1)) == 2){ // ako e izvunredni dni
            String message = listS.get(4) + " часа на дата " + listS.get(3);
            tvContact.setText(message);
        }
        else if(parseInt(listS.get(1)) == 1){ // ako e izvunredni chasove
            String message = "1 ден на дата " + listS.get(3);
            tvContact.setText(message);
        }




        CardView options= (CardView)view.findViewById(R.id.btnOptions);

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show options dialog for current record and do request after this
                ArrayList<String> currData = new ArrayList<String>(Arrays.asList(list.get(position).split("##")));
                String msg = "Опции за тази дата -> използвай частично, използвай всичко ако е ден да използва N часа и да смени типа на датата на часове (оставащи)";
                showMessage(msg);
                //Toast.makeText(v.getContext(),msg,Toast.LENGTH_SHORT).show();
                //doRequest(type,dateId,sender,not_id,nameS,message,dateStr);
            }
        });

        messageDialog = new Dialog(view.getContext());
        return view;
    }

    public void showMessage(String msg) {
        messageDialog.setContentView(R.layout.dialog_message);
        TextView text = (TextView) messageDialog.findViewById(R.id.txtMessage);
        text.setText(msg);
        Button btnClose = (Button) messageDialog.findViewById(R.id.btnOk);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                messageDialog.dismiss();
            }

            @Override
            protected void finalize() throws Throwable {
                super.finalize();

            }
        });
        messageDialog.show();

    }

}
