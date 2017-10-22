package com.krishnchinya.personalhealthmonitoringsystem.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.krishnchinya.personalhealthmonitoringsystem.R;

import java.util.ArrayList;

public class Medication_Confirmation extends Activity {



    RecyclerView recyclerView;
    myAdapter adapter;
    ArrayList<String> medDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_confirmation);

        Bundle bundle = getIntent().getExtras();
        medDetails = bundle.getStringArrayList("Meddetails");

//
//        for(int i=0;i<100;i++)
//            list.add(i);

        recyclerView = (RecyclerView) findViewById(R.id.rcview);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL,false));

        adapter = new myAdapter(medDetails);
        recyclerView.setAdapter(adapter);
    }
}

class myAdapter extends RecyclerView.Adapter<ViewHolder>
{
    ArrayList<String> medDetails;
    int count=0;

    public myAdapter(ArrayList medDetails) {
        this.medDetails = medDetails;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public int getItemCount() {
        return medDetails.size()/4;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.cfrmedname.setText("Medicine Name: "+medDetails.get(count++).substring(0,25));
        holder.cfrmmedtype.setText("Medicine Type: "+medDetails.get(count++));
        holder.cfrmmedtime.setText("Medicine Time: "+medDetails.get(count++));
        count++;

        holder.skipMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("+15512411299",null,"Medication Intake is Skipped!!. Please Check",null,null);
                    //smsManager.sendTextMessage(contacts.get(0), null, "Test SMS Message", null, null);
                } catch (Exception E) {
                    int a=0;
                }
            }
        });

        holder.takeMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.takeMed.getText().equals("Take")) {
                    holder.takeMed.setText("Un-Take");
                }else
                {
                    holder.takeMed.setText("Take");
                }
            }
        });

    }
}

class ViewHolder extends RecyclerView.ViewHolder{
    TextView cfrmedname,cfrmmedtype,cfrmmedtime;
    Button skipMed,takeMed;
    public ViewHolder(View itemView) {
        super(itemView);
       // CardView cv = (CardView) itemView.findViewById(R.id.cardview);
        cfrmedname = (TextView) itemView.findViewById(R.id.cfrmedname);
        cfrmmedtype = (TextView) itemView.findViewById(R.id.cfrmmedtype);
        cfrmmedtime = (TextView) itemView.findViewById(R.id.cfrmmedtime);

        skipMed = (Button) itemView.findViewById(R.id.skipMed);
        takeMed = (Button) itemView.findViewById(R.id.takeMed);
    }
}