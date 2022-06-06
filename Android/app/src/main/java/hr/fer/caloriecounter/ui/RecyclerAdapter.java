package hr.fer.caloriecounter.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import hr.fer.caloriecounter.NetworkClient;
import hr.fer.caloriecounter.R;
import hr.fer.caloriecounter.model.Progress;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    private Context ctx;
    private List<Progress> progressList;
    private Dialog dialog;

    public RecyclerAdapter(Context ctx) {
        this.ctx = ctx;
    }

    public void setProgress(List<Progress> progressList) {
        this.progressList = progressList;
        Collections.sort(this.progressList);
        Collections.reverse(this.progressList);
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.progress_row, parent, false);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerAdapter.RecyclerViewHolder holder, int position) {
        holder.dateText.setText("Date: " + String.valueOf(progressList.get(position).getDate()));
        holder.weightText.setText("Weight: " + String.valueOf(progressList.get(position).getWeight()) + "kg");

        holder.itemView.setOnClickListener(view -> {
            addPhotoDialog(position);
        });
    }

    @Override
    public int getItemCount() {
        return progressList != null ? progressList.size() : 0;
    }

    private void addPhotoDialog(int position) {
        dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.progress_photo_dialog);

        ImageView imageView = dialog.findViewById(R.id.progress_photo_view);
        Picasso.get().load(NetworkClient.BASE_URL + "api/image/" + progressList.get(position).getImage().getUuid()).into(imageView);

        dialog.show();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView dateText, weightText;

        public RecyclerViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.progress_row_date);
            weightText = itemView.findViewById(R.id.progress_row_weight);
        }
    }
}
