package com.brannik.system;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class chat extends Fragment {
    private static final String ARG_PARAM1 = "userName";
    private static final String ARG_PARAM2 = "rank";
    private ArrayList<String> array = new ArrayList<>();

    private String mUserName;
    private String mRank;
    public static chat newInstance(String userName, int rank) {
        chat fragment = new chat();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userName);
        args.putInt(ARG_PARAM2, rank);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserName = getArguments().getString(ARG_PARAM1);
            mRank = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inf = inflater.inflate(R.layout.fragment_chat, container, false);
        // Inflate the layout for this fragment
        Button btnSend = (Button) inf.findViewById(R.id.btnSendMessage);
        ListView messages = (ListView) inf.findViewById(R.id.messages);
        EditText messageNew = (EditText) inf.findViewById(R.id.newMessage);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                array.add(messageNew.getText().toString());
                messageNew.setText("");
                messages.setAdapter(new MyCustomAdapterChat(array, v.getContext()) );
            }
        });
        return inf;
    }
}
