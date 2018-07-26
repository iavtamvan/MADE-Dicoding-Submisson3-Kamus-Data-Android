package com.example.root.kamusdata.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.root.kamusdata.DetailActivity;
import com.example.root.kamusdata.R;
import com.example.root.kamusdata.helper.Config;
import com.example.root.kamusdata.model.KamusDataModel;

import java.util.ArrayList;

public class KamusDataAdapter extends RecyclerView.Adapter<KamusDataAdapter.MyViewHolder> implements Filterable {

    private ArrayList<KamusDataModel> kamusDataModels;
    private ArrayList<KamusDataModel> searchResult;
    private Context context;

    public KamusDataAdapter(ArrayList<KamusDataModel> kamusDataModels, Context context) {
        this.kamusDataModels = kamusDataModels;
        this.context = context;
    }

    public void replaceAll(ArrayList<KamusDataModel> items) {
        kamusDataModels = items;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.tv_list_word.setText(kamusDataModels.get(position).getWord());
        holder.tv_list_translate.setText(kamusDataModels.get(position).getTranslate());
        holder.click_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(Config.BUNDLE_WORD, holder.tv_list_word.getText().toString().trim());
                intent.putExtra(Config.BUNDLE_TRANSLATE, holder.tv_list_translate.getText().toString().trim());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return kamusDataModels.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<KamusDataModel> resultsItems = new ArrayList<>();

                if (searchResult == null)
                    searchResult = kamusDataModels;
                if (constraint != null) {
                    if (kamusDataModels != null & searchResult.size() > 0) {
                        for (final KamusDataModel g : searchResult) {
                            if (g.getWord().toLowerCase().contains(constraint.toString()))
                                resultsItems.add(g);
                        }
                    }
                    oReturn.values = resultsItems;
                }

                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                kamusDataModels = (ArrayList<KamusDataModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_list_word;
        TextView tv_list_translate;
        LinearLayout click_detail;

        MyViewHolder(View itemView) {
            super(itemView);
            click_detail = itemView.findViewById(R.id.click_detail);
            tv_list_word = itemView.findViewById(R.id.tv_list_word);
            tv_list_translate = itemView.findViewById(R.id.tv_lis_translate);


        }
    }
}
