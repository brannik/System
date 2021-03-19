package com.brannik.system;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class MyCustomAdapterDocuments extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private Integer type;

    public MyCustomAdapterDocuments(ArrayList<String> list, Context context,Integer type) {
        this.list = list;
        this.context = context;
        this.type = type;
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


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_listview_documents, null);
        }

        //Handle TextView and display string from your list
        TextView txtDocNumber= (TextView)view.findViewById(R.id.txtDocNumber);
        TextView txtDocDate= (TextView)view.findViewById(R.id.txtDocDate);
        TextView txtDocStatus= (TextView)view.findViewById(R.id.docStatus);

        TextView labelNumber = (TextView) view.findViewById(R.id.labelNumber);
        TextView labelDate = (TextView) view.findViewById(R.id.labelDate);
        TextView labelStatus = (TextView) view.findViewById(R.id.labelStatus);

        TextView ownerNames = (TextView) view.findViewById(R.id.textOwner);
        TextView ownerSklad = (TextView) view.findViewById(R.id.textSklad);
        LinearLayout hidden = (LinearLayout) view.findViewById(R.id.hiddenList);
        if(type == 2){
            if(position == 0){
                labelDate.setText("НЕ ОСТРАЗЕНИ: ");
                labelNumber.setText("ОТРАЗЕНИ: ");
                labelStatus.setText("ВСИЧКИ: ");
                ArrayList<String> listS = new ArrayList<String>(Arrays.asList(list.get(position).split("##")));
                txtDocNumber.setText(listS.get(0));
                txtDocDate.setText(listS.get(1));
                txtDocStatus.setText(listS.get(2));
            }else{
                labelDate.setText("Дата: ");
                labelNumber.setText("№ ");
                labelStatus.setText("Статус: ");
                ArrayList<String> listS = new ArrayList<String>(Arrays.asList(list.get(position).split("##")));
                txtDocNumber.setText(listS.get(0));
                txtDocDate.setText(listS.get(1));
                txtDocStatus.setText(listS.get(2));

            }
        }else if(type == 1){
            hidden.setVisibility(View.VISIBLE);
            labelDate.setText("Дата: ");
            labelNumber.setText("№ ");
            labelStatus.setText("Статус: ");
            ArrayList<String> listS = new ArrayList<String>(Arrays.asList(list.get(position).split("##")));
            txtDocNumber.setText(listS.get(0));
            ownerNames.setText(listS.get(1));
            ownerSklad.setText(listS.get(2));
            txtDocDate.setText(listS.get(3));
            txtDocStatus.setText(listS.get(4));
        }



        return view;
    }
}
