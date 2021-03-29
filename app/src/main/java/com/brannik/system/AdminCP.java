package com.brannik.system;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import static java.lang.Integer.parseInt;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminCP#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminCP extends Fragment {
    GlobalVariables GLOBE = new GlobalVariables(MainActivity.getAppContext());
    Dialog messageDialog;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AdminCP() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment admin.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminCP newInstance(String param1, String param2) {
        AdminCP fragment = new AdminCP();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inf = inflater.inflate(R.layout.fragment_admin_main, container, false);
        messageDialog = new Dialog(this.getContext());
        AdminAccountsFunctions admin_accounts = new AdminAccountsFunctions();
        AdminDocumentsFunctions admin_documents = new AdminDocumentsFunctions();
        Spinner spinnerAcc = (Spinner) inf.findViewById(R.id.spinnerAccManagement);

        String[] ranks =  {"Зареди потребителите"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (inf.getContext(), android.R.layout.simple_spinner_item,
                        ranks); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinnerAcc.setAdapter(spinnerArrayAdapter);
        Button btnLoad = (Button) inf.findViewById(R.id.btnAdminAccountLoad);
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admin_accounts.buildAccAdminSection(inf,spinnerAcc,null);
            }
        });

        Button btnFindDoc = (Button) inf.findViewById(R.id.btnDocumentFind);
        EditText editTxtFind = (EditText) inf.findViewById(R.id.editTxtDocumentFind);
        btnFindDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int textToFind = parseInt(editTxtFind.getText().toString());
                if(validateData(textToFind)){
                    admin_documents.buildDocumentsControllPage(textToFind);
                }else{
                    int count = 0;
                    while (textToFind != 0) {
                        // num = num/10
                        textToFind /= 10;
                        ++count;
                    }
                    showMessage("Не е въведен валиден номер. Трябва да съдържа 7 цифри а са въведени - " + count);
                }
            }
        });



        //admin.populateAccountSpinner(spinner,inf,null); // this -> ADMIN_VAR



        return inf;
    }

    private Boolean validateData(int data){
        Boolean check = false;
        if(data != 0 ) {
            int length = (int) (Math.log10(data) + 1);
            //Log.d("DEBUG","LENGTH ->" + length);
            if (length == 7) {
                check = true;
            } else {
                check = false;
            }
        }else{
            check = false;
        }

        return check;
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
        });
        messageDialog.show();

    }



}