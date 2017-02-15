package info.bhtours.bhtours.adapter;

/**
 * Created by Ravi on 29/07/15.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import info.bhtours.bhtours.R;
import info.bhtours.bhtours.model.NavDrawerItem;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);
        holder.title.setText(current.getTitle());
        if(current.getTitle().equals(""))
            holder.title.setHeight(0);
        holder.icon.setImageBitmap(current.getIcon());

        /*CALCULATING GRADIENT COLOR FOR MENU ITEMS*/
        int mainColor = ContextCompat.getColor(context, R.color.colorMenuPrimary);
        int r1 = (mainColor>> 16) & 0xFF;
        int g1 = (mainColor>> 8) & 0xFF;
        int b1 = (mainColor>> 0) & 0xFF;
        Log.d("r1",Integer.toString(r1));
        Log.d("g1",Integer.toString(g1));
        Log.d("b1",Integer.toString(b1));
        int darkColor = ContextCompat.getColor(context, R.color.colorMenuDarkest);
        int r2 = (darkColor>> 16) & 0xFF;
        int g2 = (darkColor>> 8) & 0xFF;
        int b2 = (darkColor>> 0) & 0xFF;

        int redStep = (r1 - r2) / data.size();
        int greenStep = (g1 - g2) / data.size();
        int blueStep = (b1 - b2) / data.size();


        holder.itemView.setBackgroundColor(Color.argb(0xFF,r1 - redStep * position, g1 - greenStep * position, b1 - blueStep * position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            icon = (ImageView) itemView.findViewById(R.id.icon);
        }
    }
}
