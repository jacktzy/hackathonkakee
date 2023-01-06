package com.yydds.hackathonkakee.participant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Participant;

public class HOFPersonAdapter extends FirestoreRecyclerAdapter<Participant, HOFPersonAdapter.HOFPersonViewHolder> {
    Context context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public HOFPersonAdapter(@NonNull FirestoreRecyclerOptions<Participant> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull HOFPersonViewHolder holder, int position, @NonNull Participant participant) {
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);

        if (!participant.getProfilePicUrl().isEmpty()) Picasso.get().load(participant.getProfilePicUrl()).into(holder.profilePicIV);
        holder.rankingTV.setText("#" + (position + 1));
        holder.nameTV.setText(participant.getName());
        holder.pointTV.setText(Integer.toString(participant.getPoints()));
        System.out.println(participant.getPoints());

        holder.itemView.startAnimation(animation);
    }

    @NonNull
    @Override
    public HOFPersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_design_hall_of_fame_person_item, parent, false);
        return new HOFPersonViewHolder(view);
    }

    class HOFPersonViewHolder extends RecyclerView.ViewHolder {
        TextView rankingTV, nameTV, pointTV;
        ImageView profilePicIV;

        public HOFPersonViewHolder(@NonNull View itemView) {
            super(itemView);
            rankingTV = itemView.findViewById(R.id.rankingTV);
            nameTV = itemView.findViewById(R.id.nameTV);
            pointTV = itemView.findViewById(R.id.pointTV);
            profilePicIV = itemView.findViewById(R.id.profilePicIV);
        }
    }
}
