package com.krishnchinya.personalhealthmonitoringsystem.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.krishnchinya.personalhealthmonitoringsystem.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class DietActivity extends AppCompatActivity {

    private EditText bfCal;

    private EditText lunchCal;

    private EditText dinnerCal;

    private EditText snacksCal;

    private EditText currentDate;

    double bfc;
    double lnc;
    double dnc;
    double snc;

     EditText TotalCal;
    Button dietSave;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);



        dietSave = (Button) findViewById(R.id.dietSave);


        bfCal=(EditText) findViewById(R.id.BfCal);
        lunchCal=(EditText) findViewById(R.id.LunchCal);
        dinnerCal=(EditText) findViewById(R.id.DinnerCal);
        snacksCal=(EditText) findViewById(R.id.SnacksCal);
        TotalCal=(EditText) findViewById(R.id.TotalCal);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String currentDateandTime = sdf.format(new Date());
        currentDate=(EditText) findViewById(R.id.CurrentDate);
        currentDate.setText(currentDateandTime);

        dietSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                bfCal.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                lunchCal.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                dinnerCal.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                snacksCal.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);

//                double bfc = Double.parseDouble(bfCal.getText().toString());
//                double lnc = Double.parseDouble(lunchCal.getText().toString());
//                double dnc = Double.parseDouble(dinnerCal.getText().toString());
//                double snc = Double.parseDouble(snacksCal.getText().toString());
//
//                double totalCal=bfc+lnc+dnc+snc;
//                TotalCal.setText(String.valueOf(totalCal));

                if((bfCal.getText().toString()).equals(""))
                {
                    Toast t = Toast.makeText(DietActivity.this,"Please enter breakfast calories ",Toast.LENGTH_LONG);
                    t.show();
                    bfCal.requestFocus();
                    return;
                }
                else if(!((bfCal.getText().toString()).matches("\\d+(?:\\.\\d+)?"))){
                    Toast t = Toast.makeText(DietActivity.this,"Input for caalories should be numeric",Toast.LENGTH_LONG);
                    t.show();
                    bfCal.requestFocus();
                    return;

                }
                if((lunchCal.getText().toString()).equals(""))
                {
                    Toast t = Toast.makeText(DietActivity.this,"Please enter lunch calories ",Toast.LENGTH_LONG);
                    t.show();
                    lunchCal.requestFocus();
                    return;
                }
                else if(!((lunchCal.getText().toString()).matches("\\d+(?:\\.\\d+)?"))){
                    Toast t = Toast.makeText(DietActivity.this,"Input for calories should be numeric",Toast.LENGTH_LONG);
                    t.show();
                    lunchCal.requestFocus();
                    return;}

                if((dinnerCal.getText().toString()).equals(""))
                {
                    Toast t = Toast.makeText(DietActivity.this,"Please enter dinner calories ",Toast.LENGTH_LONG);
                    t.show();
                    dinnerCal.requestFocus();
                    return;
                }
                else if(!((dinnerCal.getText().toString()).matches("\\d+(?:\\.\\d+)?"))){
                    Toast t = Toast.makeText(DietActivity.this,"Input for calories should be numeric",Toast.LENGTH_LONG);
                    t.show();
                    dinnerCal.requestFocus();
                    return;}
                if((snacksCal.getText().toString()).equals(""))
                {
                    Toast t = Toast.makeText(DietActivity.this,"Please enter snacks calories ",Toast.LENGTH_LONG);
                    t.show();
                    snacksCal.requestFocus();
                    return;
                }
                else if(!((snacksCal.getText().toString()).matches("\\d+(?:\\.\\d+)?"))){
                    Toast t = Toast.makeText(DietActivity.this,"Input for calories should be numeric",Toast.LENGTH_LONG);
                    t.show();
                    snacksCal.requestFocus();
                    return;
                }

                TotalCal.requestFocus();
//                bfc = Double.parseDouble(bfCal.getText().toString());
//                lnc = Double.parseDouble(lunchCal.getText().toString());
//                dnc = Double.parseDouble(dinnerCal.getText().toString());
//                snc = Double.parseDouble(snacksCal.getText().toString());
//
//                double totalCal=bfc+lnc+dnc+snc;
//                TotalCal.setText(String.valueOf(totalCal));
                  finish();
            }



        });

        bfCal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(bfCal.getText().toString().isEmpty())
                {
                    bfc = 0;
                }else{bfc = Double.parseDouble(bfCal.getText().toString());}

                double totalCal=bfc+lnc+dnc+snc;
                TotalCal.setText(String.valueOf(totalCal));
            }
        });


        lunchCal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(bfCal.getText().toString().isEmpty())
                {
                    bfc = 0;
                }else{bfc = Double.parseDouble(bfCal.getText().toString());}

                if(lunchCal.getText().toString().isEmpty()){
                    lnc=0;
                }else {lnc = Double.parseDouble(lunchCal.getText().toString());}


                double totalCal=bfc+lnc+dnc+snc;
                TotalCal.setText(String.valueOf(totalCal));
            }
        });

        dinnerCal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(bfCal.getText().toString().isEmpty())
                {
                    bfc = 0;
                }else{bfc = Double.parseDouble(bfCal.getText().toString());}

                if(lunchCal.getText().toString().isEmpty()){
                    lnc=0;
                }else {lnc = Double.parseDouble(lunchCal.getText().toString());}

                if(dinnerCal.getText().toString().isEmpty())
                {
                    dnc=0;
                }else{
                    dnc = Double.parseDouble(dinnerCal.getText().toString());
                }


                double totalCal=bfc+lnc+dnc+snc;
                TotalCal.setText(String.valueOf(totalCal));
            }
        });

        snacksCal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(bfCal.getText().toString().isEmpty())
                {
                    bfc = 0;
                }else{bfc = Double.parseDouble(bfCal.getText().toString());}

                if(lunchCal.getText().toString().isEmpty()){
                    lnc=0;
                }else {lnc = Double.parseDouble(lunchCal.getText().toString());}

                if(dinnerCal.getText().toString().isEmpty())
                {
                    dnc=0;
                }else{
                    dnc = Double.parseDouble(dinnerCal.getText().toString());
                }

                if(snacksCal.getText().toString().isEmpty()){
                    snc=0;
                }else {snc = Double.parseDouble(snacksCal.getText().toString());}

                double totalCal=bfc+lnc+dnc+snc;
                TotalCal.setText(String.valueOf(totalCal));
            }
        });


    }
}
