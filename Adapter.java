package com.codeshastra.coderr.provideameal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.CustomViewHolder> {

    private Context context;
    private List<Message> data;
    public String name;
    private ClickListener clickListener;
    public static int positionSelected;

    private LayoutInflater layoutInflater;

    public Adapter(Context c, List<Message> d) {
        context = c;
        this.data = d;
        layoutInflater = LayoutInflater.from(c);
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(Adapter.CustomViewHolder holder, int position) {
        Message current = data.get(position);
        holder.textviewMain.setText(current.getName());
        String tempNumber = "Number of Meals: " + current.getMeals();
        holder.textViewSub.setText(tempNumber);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.cardview_for_list, parent, false);
        return (new CustomViewHolder(view));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textviewMain, textViewSub, linearLayout;

        public CustomViewHolder(View view) {
            super(view);
            textviewMain = (TextView) view.findViewById(R.id.text_view_list_name);
            textViewSub = (TextView) view.findViewById(R.id.text_view_list_number);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(clickListener !=null)
                clickListener.itemClicked(v, getAdapterPosition());
        }
    }

    public interface ClickListener {
        void itemClicked(View view, int position);
    }
}
