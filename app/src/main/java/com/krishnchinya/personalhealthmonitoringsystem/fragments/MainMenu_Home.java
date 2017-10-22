package com.krishnchinya.personalhealthmonitoringsystem.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.krishnchinya.personalhealthmonitoringsystem.R;
import com.krishnchinya.personalhealthmonitoringsystem.other.HandleXML;

import java.util.ArrayList;


public class MainMenu_Home extends Fragment{
    View view;

    private String finalUrl="http://rssfeeds.webmd.com/rss/rss.aspx?RSSSource=RSS_PUBLIC";
    private HandleXML obj;
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> link = new ArrayList<>();
    ArrayList<String> description = new ArrayList<>();
    ArrayList<String> images = new ArrayList<>();

    RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_menu_home, container, false);

            obj = new HandleXML(finalUrl);
            obj.fetchXML();

            while(obj.parsingComplete);
            title = obj.getTitle();//remove 2 elements
            link = obj.getLink();//remove 2 elements
            description = obj.getDescription(); //remove 1 ele
            images = obj.getImages(); //remove 0

        title.remove(0);
        title.remove(0);

        link.remove(0);
        link.remove(0);

        description.remove(0);

        recyclerView = (RecyclerView) view.findViewById(R.id.mainmenu_recycler_view);
        recyclerView.setHasFixedSize(true);

        // mLayoutManager=new LinearLayoutManager();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL,false));

        myNewsAdapter newsAdapter = new myNewsAdapter(title,link,description,images);
        recyclerView.setAdapter(newsAdapter);




        return view;
    }

}

class myNewsAdapter extends RecyclerView.Adapter<viewHolder>
{
    ArrayList<String> title;
    ArrayList<String> link;
    ArrayList<String> description;
    ArrayList<String> images;

    public myNewsAdapter(ArrayList<String> title, ArrayList<String> link, ArrayList<String> description, ArrayList<String> images) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.images = images;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_mainmenu,parent,false);
        viewHolder vh = new viewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, final int position) {
        Glide.with(holder.imageView.getContext()).load(images.get(position)).crossFade(1000).
        error(R.drawable.webmd).into(holder.imageView);
        holder.Title.setText(title.get(position));
        holder.description.setText(description.get(position));

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserintent = new Intent(Intent.ACTION_VIEW, Uri.parse(link.get(position)));
                v.getContext().startActivity(browserintent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return title.size();
    }
}

class viewHolder extends RecyclerView.ViewHolder
{
    ImageView imageView;
    TextView Title;
    TextView description;
    public viewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.newsimage);
        Title = (TextView) itemView.findViewById(R.id.tvnewstitle);
        description = (TextView) itemView.findViewById(R.id.tvnewsdesc);
    }
}
