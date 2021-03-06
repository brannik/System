package com.brannik.system;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DocumentsMainFrame#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("ALL")
public class DocumentsMainFrame extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<String> array = new ArrayList<>();
    private String[][] documents;
    private Integer documentsCount = 0;
    private Integer i = 0;
    private Boolean allowToSend = true;

    GlobalVariables GLOBE = new GlobalVariables(MainActivity.getAppContext());
    Dialog myDialog;
    Dialog messageDialog;
    Dialog datePicker;
    Dialog cameraCapture;
    Dialog cameraView;
    Dialog customCamera;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static Bitmap imageBitmap;

    public DocumentsMainFrame() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment sundays.
     */
    // TODO: Rename and change types and number of parameters
    public static DocumentsMainFrame newInstance(String param1, String param2) {
        DocumentsMainFrame fragment = new DocumentsMainFrame();
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
        View inf = inflater.inflate(R.layout.fragment_documents_main, container, false);
        RelativeLayout btnAdd = (RelativeLayout) inf.findViewById(R.id.btnNewDocument);
        RelativeLayout btnDel = (RelativeLayout) inf.findViewById(R.id.btnDelete);
        RelativeLayout btnFind = (RelativeLayout) inf.findViewById(R.id.btnFindDocument);
        RelativeLayout btnList = (RelativeLayout) inf.findViewById(R.id.btnListDocuments);
        RelativeLayout btnMode = (RelativeLayout) inf.findViewById(R.id.btnDocumentEnter);
        ImageView newBtnIconCamera = (ImageView) inf.findViewById(R.id.btnNewDocumentIconCamera);
        ImageView newBtnIconText = (ImageView) inf.findViewById(R.id.btnNewDocumentIconText);
        EditText docNumber = (EditText) inf.findViewById(R.id.editDocumentNumber);

        docNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                if(str.matches("")){
                    newBtnIconCamera.setVisibility(View.VISIBLE);
                    newBtnIconText.setVisibility(View.GONE);
                }else{
                    newBtnIconCamera.setVisibility(View.GONE);
                    newBtnIconText.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if(str.matches("")){
                    newBtnIconCamera.setVisibility(View.VISIBLE);
                    newBtnIconText.setVisibility(View.GONE);
                }else{
                    newBtnIconCamera.setVisibility(View.GONE);
                    newBtnIconText.setVisibility(View.VISIBLE);
                }
            }
        });

        btnAdd.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnFind.setOnClickListener(this);
        btnList.setOnClickListener(this);
        btnMode.setOnClickListener(this);
        myDialog = new Dialog(this.getContext());
        messageDialog = new Dialog(this.getContext());
        datePicker = new Dialog(this.getContext());
        cameraCapture = new Dialog(this.getContext());
        cameraView = new Dialog(this.getContext());
        customCamera = new Dialog(this.getContext());

        return inf;
    }

    public void sendItems(View view,Integer type) {
        ListView listView = getView().findViewById(R.id.listDocuments);
        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.getAppContext(),
                android.R.layout.simple_list_item_1,
                array);
        listView.setAdapter(new DocumentsListAdaptor(array, view.getContext(),type) );
        //listView.setAdapter(arrayAdapter);
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

    public void showDatePicker() {
        datePicker.setContentView(R.layout.dialog_select_month_documents);
        TextView txtDate = (TextView) datePicker.findViewById(R.id.txtDate);
        TextView txtYear = (TextView) datePicker.findViewById(R.id.txtYear);
        DateFormat dateFormat = new SimpleDateFormat("MM");
        DateFormat yearFormat = new SimpleDateFormat("Y");
        Date date = new Date();
        //Log.d("Month",dateFormat.format(date));
        txtDate.setText(dateFormat.format(date));
        txtYear.setText(yearFormat.format(date));

        Button btnSend = (Button) datePicker.findViewById(R.id.btnSendRequest);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = valueOf(txtDate.getText());
                String year = valueOf(txtYear.getText());
                SendRequest("LIST_ALL", parseInt(date), parseInt(year),v);
                datePicker.dismiss();
            }
        });


        datePicker.show();
    }

    public void ShowPopup() {
        // get first element from documents array and display it
        i = 0;
        myDialog.setContentView(R.layout.dialog_documents_action);
        TextView text = (TextView) myDialog.findViewById(R.id.txtShowDocument);
        TextView txtCounter = (TextView) myDialog.findViewById(R.id.txtCounter);
        String temp = valueOf(i + 1) + " ???? " + valueOf(documentsCount - 1);
        txtCounter.setText(temp);
        text.setText("No -> " + documents[i][0]);
        Button btnSkip = myDialog.findViewById(R.id.btnSkip);
        Button btnClose = myDialog.findViewById(R.id.btnClose);
        Button btnEnter = myDialog.findViewById(R.id.btnEnter);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = i + 1;
                if (i < documentsCount - 1) {
                    text.setText("No -> " + documents[i][0]);
                    String temp = valueOf(i + 1) + " ???? " + valueOf(documentsCount - 1);
                    txtCounter.setText(temp);
                } else {
                    text.setText("???????? !!!");
                }

            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i < documentsCount) {
                    SendRequest("CHECK_DOCUMENT", parseInt(documents[i][1]), 0,v);
                    i = i + 1;
                    if (i < documentsCount - 1) {
                        text.setText("No -> " + documents[i][0]);
                        String temp = valueOf(i + 1) + " ???? " + valueOf(documentsCount - 1);
                        txtCounter.setText(temp);
                    } else {
                        text.setText("???????? !!!");
                    }
                }

            }
        });
        myDialog.show();
    }

    public void showConfirmDialog(String msg) {
        cameraCapture.setContentView(R.layout.dialog_confirm_document_number);
        TextView docNumber = (TextView) cameraCapture.findViewById(R.id.editNumber);
        docNumber.setText(msg);
        Button btnDone = (Button) cameraCapture.findViewById(R.id.btnDone);
        //btnDone.setEnabled(false);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = docNumber.getText().toString();
                Integer finalData = parseInt(txt);
                SendRequest("NEW_DOC", finalData, 0,v);
                cameraCapture.dismiss();
            }
        });
        cameraCapture.show();

    }
    Camera camera;
    FrameLayout frameLayout;
    public static boolean previewing = false;


    private Bitmap bitmap;



    private void customCameraDialog(){
        customCamera.setContentView(R.layout.custom_camera);
        Button btn = (Button) customCamera.findViewById(R.id.captBtn);
        Button btnClose = (Button) customCamera.findViewById(R.id.closeBtn);

        frameLayout = (FrameLayout) customCamera.findViewById(R.id.surfaceView);

        //Overlay overl = new Overlay(getContext());
        FrameLayout overlay = (FrameLayout) customCamera.findViewById(R.id.overlay);
        //overlay.addView(overl);
        overlay.bringToFront();
        ViewCompat.setTranslationZ(overlay, 1);


        camera = Camera.open();
        CameraCallback cam = new CameraCallback(getContext(),camera);
        frameLayout.addView(cam);

        ButterKnife.bind(this, customCamera);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // capture image
                camera.autoFocus(new Camera.AutoFocusCallback() {
                    public void onAutoFocus(boolean success, Camera camera) {
                        if(success){
                            takeAPicture();
                        }
                    }
                });
                // after pic capture go to confirm dialog
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG","DISMISSED");
                customCamera.dismiss();
                camera.release();
            }
        });
        customCamera.show();
    }


    public void takeAPicture(){

        Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Bitmap bitmapPicture
                        = BitmapFactory.decodeByteArray(data, 0, data.length);

                Bitmap croppedBitmap = null;


                    //rotate bitmap, because camera sensor usually in landscape mode
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(bitmapPicture, 0, 0,
                            bitmapPicture.getWidth(), bitmapPicture.getHeight(), matrix, true);
                    //save file
                    FrameLayout previewLayout = (FrameLayout) customCamera.findViewById(R.id.surfaceView);
                    FrameLayout borderCamera = (FrameLayout) customCamera.findViewById(R.id.overlay);

                    //calculate aspect ratio
                    float koefX = (float) rotatedBitmap.getWidth() / (float) previewLayout.getWidth();
                    float koefY = (float) rotatedBitmap.getHeight() / (float) previewLayout.getHeight();
                    //Log.d("DEBUG",koefX + " - " + koefY);
                    //get viewfinder border size and position on the screen
                    int x1 = borderCamera.getLeft();
                    int y1 = borderCamera.getTop();

                    int x2 = borderCamera.getWidth();
                    int y2 = borderCamera.getHeight();

                    //Log.d("DEBUG",x1 + " - " + x2 + " | " + y1 + " - " + y2);

                    //calculate position and size for cropping
                    int cropStartX = Math.round(x1 * koefX);
                    int cropStartY = Math.round(y1 * koefY);

                    int cropWidthX = Math.round(x2 * koefX);
                    int cropHeightY = Math.round(y2 * koefY);

                    //check limits and make crop
                    if (cropStartX + cropWidthX <= rotatedBitmap.getWidth() &&
                            cropStartY + cropHeightY <= rotatedBitmap.getHeight()) {
                        croppedBitmap = Bitmap.createBitmap(rotatedBitmap, cropStartX,
                                cropStartY, cropWidthX, cropHeightY);
                    } else {
                        croppedBitmap = null;
                    }

                    //save result
                    if (croppedBitmap != null) {
                        imageBitmap = croppedBitmap;
                        detectTextFromImage();
                        //camera.release();
                    }

            }
        };
        camera.takePicture(null, null, mPictureCallback);


    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            detectTextFromImage();
        }
    }
    private void displayTextFromImage(FirebaseVisionText firebaseVisionText){
        List<FirebaseVisionText.Block> blockList = firebaseVisionText.getBlocks();
        if(blockList.size() == 0){
            // no text detected
            showMessage("???? ?? ?????????????? ?????????? !!!");
            customCamera.dismiss();
        }else{
            for(FirebaseVisionText.Block block: firebaseVisionText.getBlocks()){
                String text = block.getText();
                // cut the text
                String numericOutput;
                Pattern pattern = Pattern.compile("[0-9]{7}");
                Matcher matcher = pattern.matcher(text);
                if(matcher.find()) {
                    showConfirmDialog(text.substring(matcher.start(),matcher.end()));
                    Log.d("DEBUG", text.substring(matcher.start(),matcher.end()));
                    customCamera.dismiss();
                }
                // find pathern in text

            }
        }

    }
    private void detectTextFromImage() {
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextDetector textDetector = FirebaseVision.getInstance().getVisionTextDetector();
        textDetector.detectInImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                displayTextFromImage(firebaseVisionText);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        EditText textBox = getView().findViewById(R.id.editDocumentNumber);
        String value= textBox.getText().toString();
        int finalValue;
            switch (v.getId()) {
                case R.id.btnNewDocument:

                    if(value.matches("")){

                        //dispatchTakePictureIntent();
                        customCameraDialog();
                        // open camera
                        // confirm dialog
                        // send request from dialog
                    }else {
                        finalValue= parseInt(value);
                        SendRequest("NEW_DOC", finalValue,0,v);
                        textBox.setText("");
                    }
                    //Log.d("DEBUG","ADD NEW DOCUMENT");
                    break;
                case R.id.btnDelete:
                    if(value.matches("")){
                        showMessage("???????????????????? ???????? ???? ???????? ???? ?? ???????????? !!!");
                    }else {
                        finalValue = parseInt(value);
                        SendRequest("DELETE_DOC", finalValue,0,v);
                        textBox.setText("");
                    }
                    //Log.d("DEBUG","DELETE DOCUMENT");
                    break;
                case R.id.btnFindDocument:
                    if(value.matches("")){
                        showMessage("???????????????????? ???????? ???? ???????? ???? ?? ???????????? !!!");
                    }else {
                        finalValue = parseInt(value);
                        SendRequest("FIND_DOC", finalValue,0,v);
                        textBox.setText("");
                    }
                    break;
                case R.id.btnListDocuments:
                    showDatePicker();
                    textBox.setText("");
                    break;
                case R.id.btnDocumentEnter:
                    SendRequest("GET_ALL_ENTER_MODE",0,0,v);
                    textBox.setText("");
                    break;
            }

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

    public static int[] getMaxMin(){
        int[] values = new int[2];

        return values;
    }

    public int requestType = 0;
    private void SendRequest(String type,int data,int dataTwo,View view){

        // validate data
        // send volley request
        RequestQueue queue = Volley.newRequestQueue(MainActivity.getAppContext());

        int ID = GLOBE.getAccId();
        int SKLAD = GLOBE.getSklad();
        String url;
        switch(type){
            case "NEW_DOC":
                if(validateData(data)){
                    url = GlobalVariables.URL + "?request=add_new_document&data=" + data + "&acc_id=" + ID + "&sklad=" + SKLAD;
                    requestType = 1;
                }else{
                    url = "";
                    showMessage("?????????????????? ?????????? ???? ?? ?????????????? !!!");
                }
                break;
            case "DELETE_DOC":
                if(validateData(data)){
                    url = GlobalVariables.URL + "?request=delete_document&data=" + data + "&acc_id=" + ID + "&sklad=" + SKLAD;
                    requestType = 2;
                }else{
                    url = "";
                    showMessage("?????????????????? ?????????? ???? ?? ?????????????? !!!");
                }
                break;
            case "FIND_DOC":
                url = GlobalVariables.URL + "?request=find_document&data=" + data + "&acc_id=" + ID + "&sklad=" + SKLAD;
                requestType = 3;
                break;
            case "LIST_ALL":
                // premesti sled dialog window-a
                // purvo pokaji prozorec da se izbere mesec i godina

                url = GlobalVariables.URL + "?request=list_document&data=" + data + "&acc_id=" + ID + "&sklad=" + SKLAD + "&year=" + dataTwo;
                requestType = 4;
                break;
            case "GET_ALL_ENTER_MODE":
                url = GlobalVariables.URL + "?request=entering_mode&acc_id=" + ID + "&sklad=" + SKLAD;
                requestType = 5;
                break;
            case "CHECK_DOCUMENT":
                url = GlobalVariables.URL + "?request=entering_mode_document&doc_id=" + data;
                requestType = 6;
                break;
            default:
                url = GlobalVariables.URL + "?request=test";
        }

        //String url ="http://app-api.servehttp.com/api.php?request=test";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Log.d("DEBUG"," -> " + response);
                        if(requestType == 1 || requestType == 2) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    String notText = data.getString("RESPONSE");
                                    showMessage(notText);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else if(requestType == 3){
                            array.clear();
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0;i<jsonArray.length();i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    String docNumber = data.getString("DOC_NUMBER");
                                    String docOwner = data.getString("OWNER");
                                    String docSklad = data.getString("SKLAD");
                                    String docStatus = data.getString("STATUS");
                                    String docDate = data.getString("DATE");
                                    String finalText =  docNumber + "##" + docOwner + "##" + docSklad + "##" + docDate + "##" + docStatus;
                                    array.add(finalText);
                                }
                                sendItems(view,1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else if(requestType == 4){
                            array.clear();
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0;i<jsonArray.length();i++) {
                                    if(i==0){
                                        JSONObject data = jsonArray.getJSONObject(i);
                                        String nonEnteredDocs = data.getString("NON_ENTERED");
                                        String enteredDocs = data.getString("ENTERED");
                                        String totalDocs = data.getString("TOTAL");
                                        String finalText = nonEnteredDocs + "##" + enteredDocs + "##" + totalDocs;
                                        array.add(finalText);
                                    }else{
                                        JSONObject data = jsonArray.getJSONObject(i);
                                        String docNumber = data.getString("DOC_NUMBER");
                                        String docDate = data.getString("DOC_DATE");
                                        String docStatus = data.getString("DOC_STATUS");
                                        String finalText = docNumber + "##" + docDate + "##" + docStatus;
                                        array.add(finalText);
                                    }

                                }
                                sendItems(view,2);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else if(requestType == 5){
                            // get data and process request to popup window
                            // get results and push to arrDocuments

                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                documents = new String[jsonArray.length()][2];
                                documentsCount = jsonArray.length();
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    String doc_number = data.getString("DOC_NUM");
                                    String doc_id = data.getString("DOC_ID");
                                    //Toast.makeText(MainActivity.getAppContext(), notText, Toast.LENGTH_LONG).show();
                                    documents[i][0] = doc_number;
                                    documents[i][1] = doc_id;
                                }
                                Log.d("DEBUG",response);
                                ShowPopup();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }else if(requestType == 6){
                            // action when document is checked
                            if(i<documentsCount) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject data = jsonArray.getJSONObject(0);
                                    String result = data.getString("RESPONSE");
                                    if (result.matches("DONE")) {
                                        Toast.makeText(MainActivity.getAppContext(), "?????????????????? ?? ????????????????", Toast.LENGTH_SHORT).show();
                                        int count = GLOBE.getUnchecked();
                                        GLOBE.setUnchecked(count-1);
                                    } else {
                                        Toast.makeText(MainActivity.getAppContext(), "?????????????????? ???? ?? ????????????????", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else{
                            Toast.makeText(MainActivity.getAppContext(), response, Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUG", "VOLLEY ERROR -> ACTIONS -> " + error);
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

}