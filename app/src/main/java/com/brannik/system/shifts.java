package com.brannik.system;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarUtils;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link shifts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class shifts extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        DatePickerBuilder builder = new DatePickerBuilder(inf.getContext(), listener)
                .pickerType(CalendarView.ONE_DAY_PICKER);
        DatePicker datePicker = builder.build();
        //datePicker.show();

        // get current date
        Calendar calendar = Calendar.getInstance();


        CalendarView calendarView = (CalendarView) inf.findViewById(R.id.calendarView);

        // set current date
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        buildEvents(inf,calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR));

        try {
            calendarView.setDate(calendar);
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                int dateMonth = clickedDayCalendar.get(Calendar.MONTH)+1;
                int dateDay = clickedDayCalendar.get(Calendar.DAY_OF_MONTH);
                int dateYear = clickedDayCalendar.get(Calendar.YEAR);
                Toast.makeText(inf.getContext(),"Кликнахте дата-" + dateMonth + "/" + dateDay + "/" + dateYear,Toast.LENGTH_SHORT).show();
            }
        });

        calendarView.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                // previous page listener
            }
        });
        calendarView.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                // next page listener
            }
        });

        return inf;
    }
    private void buildEvents(View view,int month,int year){
        List<EventDay> events = new ArrayList<>();
        // get date
        Calendar calendar = Calendar.getInstance();
        // get all events relayed to me and set them in loop
        calendar.set(Calendar.DAY_OF_MONTH,22);
        // add events

        events.add(new EventDay(calendar, R.drawable.ic_arrow_left));

        // show calendar view
        CalendarView calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        calendarView.setEvents(events);
    }

    private OnSelectDateListener listener = new OnSelectDateListener() {
        @Override
        public void onSelect(List<Calendar> calendars) {
            // date picker listener
        }
    };
}