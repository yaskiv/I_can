package home.antonyaskiv.i_can.View.Adapters;

import android.app.Fragment;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import home.antonyaskiv.i_can.Model.Messages;
import home.antonyaskiv.i_can.R;
import home.antonyaskiv.i_can.View.MapFragment;

/**
 * Created by AntonYaskiv on 13.01.2018.
 */

public class AdpterForRecyclerViewOnMapFragment extends RecyclerView.Adapter {

    private List<Messages> listOfMessege;
    private Context context;
    private MapFragment mapFragment;

    public AdpterForRecyclerViewOnMapFragment(List<Messages> listOfMessege, Context context,MapFragment mapFragment) {
        this.listOfMessege = listOfMessege;
        this.context = context;
        this.mapFragment=mapFragment;
    }
    public static class CardViewHolder extends RecyclerView.ViewHolder
    {
        ImageView logo;
        TextView title;
        TextView desc;
        ImageView navigateTo;
        public CardViewHolder(View itemView) {
            super(itemView);
            this.logo=itemView.findViewById(R.id.logo);
            this.title=itemView.findViewById(R.id.title);
            this.desc=itemView.findViewById(R.id.descriprion);
            this.navigateTo=itemView.findViewById(R.id.navigate_to_message);

        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_element, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Messages message=listOfMessege.get(position);
        ((CardViewHolder) holder).title.setText(message.getM_Title());
        ((CardViewHolder) holder).desc.setText(message.getM_Text());
        ((CardViewHolder) holder).navigateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapFragment.Navigate(message.getM_Owner().getLocation().getCoordinates(),message.getM_Owner().getP_Level());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfMessege.size();
    }
}
