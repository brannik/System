package com.brannik.system;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import java.util.ArrayList;
import java.util.Arrays;

import static android.view.View.GONE;
import static java.lang.Integer.parseInt;

public class DocumentsListAdaptor extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private final Context context;
    private final Integer type;

    public DocumentsListAdaptor(ArrayList<String> list, Context context, Integer type) {
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


    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_listview_documents, null);
        }

        Drawable checked = AppCompatResources.getDrawable(context, R.drawable.document_checked);
        Drawable unchecked = AppCompatResources.getDrawable(context, R.drawable.unchecked_document);

        //Handle TextView and display string from your list
        TextView rowOneText= (TextView)view.findViewById(R.id.dataNumberLabel);
        TextView rowTwoText = (TextView) view.findViewById(R.id.dataOwnerLabel);
        TextView rowTreeText = (TextView) view.findViewById(R.id.dataSkladLabel);

        RelativeLayout rowTwo = (RelativeLayout) view.findViewById(R.id.dataRowTwo);
        RelativeLayout rowOne = (RelativeLayout) view.findViewById(R.id.dataRowOne);
        RelativeLayout rowTree = (RelativeLayout) view.findViewById(R.id.dataRowTree);

        ImageView iconSmall = (ImageView) view.findViewById(R.id.iconStatusOneRow);
        ImageView iconBig = (ImageView) view.findViewById(R.id.iconStatusTwoRow);


        LinearLayout container = (LinearLayout) view.findViewById(R.id.layoutMainFrame);


        //rowTree.setVisibility(GONE);

        if(type==2){
            if(position > 0) {
                ArrayList<String> listS = new ArrayList<String>(Arrays.asList(list.get(position).split("##")));
                iconBig.setVisibility(GONE);
                iconSmall.setVisibility(View.VISIBLE);
                rowTwo.setVisibility(GONE);
                rowTree.setVisibility(GONE);
                rowOne.setVisibility(View.VISIBLE);
                if((position % 2) == 0 ){

                    container.setBackgroundColor(Color.parseColor("#737373"));
                }else{
                    container.setBackgroundColor(Color.parseColor("#424242"));
                }
                String text = "№ " + listS.get(0) + "  Дата  " + listS.get(1);
                rowOneText.setText(text);
                if (parseInt(listS.get(2)) == 1) {
                    iconSmall.setImageDrawable(checked);
                } else {
                    iconSmall.setImageDrawable(unchecked);
                }
            }
        }else if(type==1){
                ArrayList<String> listS = new ArrayList<String>(Arrays.asList(list.get(position).split("##")));
                iconBig.setVisibility(View.VISIBLE);
                iconSmall.setVisibility(GONE);
                rowTwo.setVisibility(View.VISIBLE);
                rowOne.setVisibility(View.VISIBLE);
                rowTree.setVisibility(View.VISIBLE);
                if((position % 2) == 0 ){
                    container.setBackgroundColor(Color.parseColor("#737373"));
                }else{
                    container.setBackgroundColor(Color.parseColor("#424242"));
                }
                String oneT = "№ " + listS.get(0) + "  Дата  " + listS.get(3);
                String twoT = "Отразил " + listS.get(1);
                String treeT = "Склад " + listS.get(2);
                rowOneText.setText(oneT);
                rowTwoText.setText(twoT);
                rowTreeText.setText(treeT);
                if (parseInt(listS.get(4)) == 1) {
                    iconBig.setImageDrawable(checked);
                } else {
                    iconBig.setImageDrawable(unchecked);
                }
        }

        return view;
    }
}
