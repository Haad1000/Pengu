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
import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReportAdptr extends RecyclerView.Adapter<ReportAdptr.viewholder> {
    private A_ViewReportsActivity aViewReportsActivity;
    private List<JSONObject> ongoingReportList;
    public ReportAdptr(A_ViewReportsActivity aViewReportsActivity, List<JSONObject> ongoingReportList) {
        this.aViewReportsActivity = aViewReportsActivity;
        this.ongoingReportList = ongoingReportList;
    }

    @NonNull
    @Override
    public ReportAdptr.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(aViewReportsActivity).inflate(R.layout.user_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdptr.viewholder holder, int position) {

        try {
            JSONObject ongoingreport = ongoingReportList.get(position);
            String title = ongoingreport.getString("title");
            int reportid = ongoingreport.getInt("id");

            holder.reportTitle.setText(title);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(aViewReportsActivity, A_ViewUserReportActivity.class);
                    intent.putExtra("reportid", reportid);
                    aViewReportsActivity.startActivity(intent);
                }
            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return ongoingReportList.size();
    }
    public class viewholder extends RecyclerView.ViewHolder {

        TextView reportTitle;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            reportTitle = itemView.findViewById(R.id.userName);

        }
    }
}
