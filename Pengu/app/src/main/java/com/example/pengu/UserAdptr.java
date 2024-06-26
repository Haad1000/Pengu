package com.example.pengu;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdptr extends RecyclerView.Adapter<UserAdptr.viewholder> {
    ViewTeachersActivity viewTeachersActivity;
    List<JSONObject> teacherList;
    public UserAdptr(ViewTeachersActivity viewTeachersActivity, List<JSONObject> teacherList) {
        this.viewTeachersActivity = viewTeachersActivity;
        this.teacherList = teacherList;
    }

    @NonNull
    @Override
    public UserAdptr.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(viewTeachersActivity).inflate(R.layout.user_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdptr.viewholder holder, int position) {

        try {
            JSONObject user = teacherList.get(position);
            String name = user.getString("name");
            int userid = user.getInt("id");

            // Bind data to views
            holder.userName.setText(name);

            // Set other data if needed

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(viewTeachersActivity, reportTeacherActivity.class);
                    intent.putExtra("userid", userid);
                    viewTeachersActivity.startActivity(intent);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return teacherList.size() ;
    }

    public class viewholder extends RecyclerView.ViewHolder {
        CircleImageView userProfile;
        TextView userName;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            userProfile = itemView.findViewById(R.id.userProfile);
            userName = itemView.findViewById(R.id.userName);
        }
    }
}
