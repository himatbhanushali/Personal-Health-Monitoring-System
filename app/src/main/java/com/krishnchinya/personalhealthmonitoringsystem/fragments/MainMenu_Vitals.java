package com.krishnchinya.personalhealthmonitoringsystem.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.krishnchinya.personalhealthmonitoringsystem.activity.DB_Handler;
import com.krishnchinya.personalhealthmonitoringsystem.activity.VitalSigns;

import com.krishnchinya.personalhealthmonitoringsystem.R;
import com.krishnchinya.personalhealthmonitoringsystem.other.GlobalVars;

import java.util.ArrayList;
import java.util.zip.Inflater;


public class MainMenu_Vitals extends Fragment {
    View view;
    RecyclerView recyclerView;
    FloatingActionButton addVitals;
    GlobalVars globalVars;
    rcvitalAdapater rcAdapter;
    ArrayList<ArrayList<String>> vitalTable = new ArrayList<ArrayList<String>>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_menu_vitals, container, false);



        globalVars = (GlobalVars) getActivity().getApplicationContext();

        addVitals = (FloatingActionButton) view.findViewById(R.id.addVitals);
        recyclerView = (RecyclerView) view.findViewById(R.id.rc_vitals);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayout.VERTICAL,false));

        addVitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),VitalSigns.class);
                //startActivityForResult(intent,1);
                startActivity(intent);
                getActivity().finish();
            }
        });

        DB_Handler db_handler = new DB_Handler(getContext());

        vitalTable = db_handler.getVitalDetails(globalVars.getMailid());

        rcAdapter = new rcvitalAdapater(vitalTable);

        recyclerView.setAdapter(rcAdapter);

        return view;
    }
}



class rcvitalAdapater extends RecyclerView.Adapter<vitalViewholder>{

    ArrayList<ArrayList<String>> vitalTable = new ArrayList<ArrayList<String>>();

    public rcvitalAdapater(ArrayList<ArrayList<String>> vitalTable) {
        this.vitalTable = vitalTable;
    }

    @Override
    public vitalViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_vital_cardview,parent,false);
        vitalViewholder vh = new vitalViewholder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(vitalViewholder holder, int position) {
      //  for (int j = 0; j < vitalTable.size(); j++) {
                holder.tvbloodtype.setText("Blood Type: "+vitalTable.get(position).get(0));
                holder.tvgluocoselevel.setText("Glucose Level:"+vitalTable.get(position).get(1));
                holder.tvbmi.setText("BMI: "+vitalTable.get(position).get(2));
                holder.tvchollevel.setText("Cholesterol Level: "+vitalTable.get(position).get(3));
        //    }
    }
    @Override
    public int getItemCount() {
        return vitalTable.size();
    }
}

class vitalViewholder extends RecyclerView.ViewHolder{
    TextView tvbloodtype, tvgluocoselevel, tvbmi, tvchollevel;

    public vitalViewholder(View itemView) {
        super(itemView);
        tvbloodtype = (TextView) itemView.findViewById(R.id.tvbloodtype);
        tvgluocoselevel = (TextView) itemView.findViewById(R.id.tvglucoselevel);
        tvbmi = (TextView) itemView.findViewById(R.id.tvbmi);
        tvchollevel = (TextView) itemView.findViewById(R.id.tvchollevel);

    }
}