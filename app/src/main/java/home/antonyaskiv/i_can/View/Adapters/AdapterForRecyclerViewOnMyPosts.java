package home.antonyaskiv.i_can.View.Adapters;

import android.support.v4.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
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
import home.antonyaskiv.i_can.View.Dialogs.EditDialog;
import home.antonyaskiv.i_can.View.MainActivity;
import home.antonyaskiv.i_can.View.MapFragment;
import home.antonyaskiv.i_can.View.MyPostsFragment;

/**
 * Created by AntonYaskiv on 14.01.2018.
 */

public class AdapterForRecyclerViewOnMyPosts extends RecyclerView.Adapter {

    private List<Messages> listOfMessege;
    private Context context;
    private MyPostsFragment myPostsFragment;

    public AdapterForRecyclerViewOnMyPosts(List<Messages> listOfMessege, Context context,MyPostsFragment myPostsFragment) {
        this.listOfMessege = listOfMessege;
        this.context = context;
        this.myPostsFragment=myPostsFragment;
    }
    public static class CardViewHolderMyPost extends RecyclerView.ViewHolder
    {
        ImageView edit;
        TextView title;
        TextView desc;
        ImageView delete;
        public CardViewHolderMyPost(View itemView) {
            super(itemView);
            this.edit=itemView.findViewById(R.id.edit_my_post);
            this.title=itemView.findViewById(R.id.title_my_post);
            this.desc=itemView.findViewById(R.id.descriprion_my_post);
            this.delete=itemView.findViewById(R.id.delete_from_db_my_post);

        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_element_my_post, parent, false);
        return new CardViewHolderMyPost(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Messages message=listOfMessege.get(position);
        ((CardViewHolderMyPost) holder).title.setText(message.getM_Title());
        ((CardViewHolderMyPost) holder).desc.setText(message.getM_Text());
        ((CardViewHolderMyPost) holder).edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              edit(message);
                 }
        });
        ((CardViewHolderMyPost) holder).delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(message);
            }
        });
    }

    private void delete(Messages message) {
        myPostsFragment.delete(message);
    }

    private void edit(Messages message) {
        DialogFragment dialog = new EditDialog();
        Bundle args = new Bundle();
        args.putParcelable("messege",message);
        dialog.setArguments(args);
        android.support.v4.app.FragmentManager quote_fm = ((MainActivity)context).getSupportFragmentManager();;

        dialog.show(quote_fm, "EDIT");

    }

    @Override
    public int getItemCount() {
        return listOfMessege.size();
    }
}

