package com.example.pengu;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ReportAdpterAnother extends RecyclerView.Adapter<ReportAdpterAnother.viewholder> {
    private A_ViewReportsActivity aViewReportsActivity;
    private List<JSONObject> doneReportList;
    public ReportAdpterAnother(A_ViewReportsActivity aViewReportsActivity, List<JSONObject> doneReportList) {
        this.aViewReportsActivity = aViewReportsActivity;
        this.doneReportList = doneReportList;
    }

    @NonNull
    @Override
    public ReportAdpterAnother.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(aViewReportsActivity).inflate(R.layout.user_item, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdpterAnother.viewholder holder, int position) {

        try {
            JSONObject ongoingreport = doneReportList.get(position);
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
        return doneReportList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView reportTitle;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            reportTitle = itemView.findViewById(R.id.userName);
        }
    }
}
