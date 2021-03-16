package com.brannik.system;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.OnNavigationButtonClickedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link shifts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class shifts extends Fragment implements OnNavigationButtonClickedListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    CustomCalendar customCalendar;

    public shifts() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment shifts.
     */
    // TODO: Rename and change types and number of parameters
    public static shifts newInstance(String param1, String param2) {
        shifts fragment = new shifts();
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
        // Inflate the layout for this fragment
        View inf =  inflater.inflate(R.layout.fragment_shifts, container, false);
        customCalendar = inf.findViewById(R.id.custom_calendar);

        HashMap<Object, Property> descHashMap = new HashMap<>();


        Property defaultProperty = new Property();
        defaultProperty.layoutResource = R.layout.calendar_deff;
        defaultProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("default",defaultProperty);

        Property dnes = new Property();
        dnes.layoutResource = R.layout.calendar_dnes;
        dnes.dateTextViewResource = R.id.text_view;
        descHashMap.put("dnes",dnes);

        Property pochivka = new Property();
        pochivka.layoutResource = R.layout.calendar_pochivka;
        pochivka.dateTextViewResource = R.id.text_view;
        descHashMap.put("pochivka",pochivka);

        Property vtora = new Property();
        vtora.layoutResource = R.layout.calendar_vtora_smqna;
        vtora.dateTextViewResource = R.id.text_view;
        descHashMap.put("vtora",vtora);

        Property nedelq = new Property();
        nedelq.layoutResource = R.layout.calendar_nedelq;
        nedelq.dateTextViewResource = R.id.text_view;
        descHashMap.put("nedelq",nedelq);

        Property dnesPochivka = new Property();
        dnesPochivka.layoutResource = R.layout.calendar_dnes_pochivka;
        dnesPochivka.dateTextViewResource = R.id.text_view;
        descHashMap.put("dnesPochivka",dnesPochivka);

        Property dnesVtora = new Property();
        dnesVtora.layoutResource = R.layout.calendar_vtora_dnes;
        dnesVtora.dateTextViewResource = R.id.text_view;
        descHashMap.put("dnesVtora",dnesVtora);

        Property dnesNedelq = new Property();
        dnesNedelq.layoutResource = R.layout.calendar_dnes_nedelq;
        dnesNedelq.dateTextViewResource = R.id.text_view;
        descHashMap.put("dnesNedelq",dnesNedelq);

        Property zaeto = new Property();
        zaeto.layoutResource = R.layout.calendar_zaeto;
        zaeto.dateTextViewResource = R.id.text_view;
        descHashMap.put("zaeto",zaeto);

        Property dnesZaeto = new Property();
        dnesZaeto.layoutResource = R.layout.calendar_dnes_zaeto;
        dnesZaeto.dateTextViewResource = R.id.text_view;
        descHashMap.put("dnesZaeto",dnesZaeto);


        customCalendar.setMapDescToProp(descHashMap);
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.PREVIOUS, this);
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.NEXT, this);

        HashMap<Integer,Object> dateHashMap = new HashMap<>();

        Calendar calendar = Calendar.getInstance();

        dateHashMap.put(calendar.get(Calendar.DAY_OF_MONTH),"dnes");
        // get info for current month and populate it
        dateHashMap.put(1,"dnesVtora");
        dateHashMap.put(2,"dnesNedelq");
        dateHashMap.put(3,"dnesPochivka");
        dateHashMap.put(4,"nedelq");
        dateHashMap.put(5,"vtora");
        dateHashMap.put(6,"pochivka");
        dateHashMap.put(7,"zaeto");
        dateHashMap.put(8,"dnesZaeto");

        customCalendar.setDate(calendar,dateHashMap);

        customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
                String sDate = selectedDate.get(Calendar.DAY_OF_MONTH)
                        + "/" + (selectedDate.get(Calendar.MONTH) + 1)
                        + "/" + selectedDate.get(Calendar.YEAR);
                Toast.makeText(inf.getContext(),"DATE " + sDate,Toast.LENGTH_SHORT).show();
                // dialog to make actions for that date !!!!
            }
        });

        return inf;
    }

    @Override
    public Map<Integer, Object>[] onNavigationButtonClicked(int whichButton, Calendar newMonth) {
        Map<Integer,Object>[] arr = new Map[2];
        switch(newMonth.get(Calendar.MONTH)){
            case Calendar.APRIL:
                arr[0] = new HashMap<>();
                // get info for this month and populate it
                arr[0].put(3,"unavailable");
                arr[0].put(14,"vtora");
                arr[1] = null;
                break;
            case Calendar.MARCH:
                // get info for this month and populate it
                arr[0] = new HashMap<>();
                arr[0].put(13,"pochivka");
                break;
        }
        return arr;
    }
}
