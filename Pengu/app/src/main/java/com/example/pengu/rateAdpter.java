//package com.example.pengu;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import org.json.JSONObject;
//
//import java.util.List;
//import android.content.Intent;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.w3c.dom.Text;
//
//import java.util.List;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
//public class rateAdpter extends RecyclerView.Adapter<RateAdptr.viewholder> {
//    private S_ViewMyRatingsActivity sViewMyRatingsActivity;
//    private List<JSONObject> ratingView;
//    public rateAdpter(S_ViewMyRatingsActivity sViewMyRatingsActivity, List<JSONObject> ratingView) {
//        this.sViewMyRatingsActivity = sViewMyRatingsActivity;
//        this.ratingView = ratingView;
//    }
//
//    @NonNull
//    @Override
//    public RateAdptr.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(sViewMyRatingsActivity).inflate(R.layout.user_item,parent,false);
//        return viewholder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RateAdptr.viewholder holder, int position) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return ratingView.size();
//    }
//
//    public class viewholder extends RecyclerView.ViewHolder {
//
//        TextView reportTitle;
//        public viewholder(@NonNull View itemView) {
//            super(itemView);
//            reportTitle = itemView.findViewById(R.id.userName);
//
//        }
//    }
//}
