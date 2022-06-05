package hr.fer.caloriecounter.ui;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

import hr.fer.caloriecounter.NetworkClient;
import hr.fer.caloriecounter.PathUtil;
import hr.fer.caloriecounter.R;
import hr.fer.caloriecounter.api.ImageApi;
import hr.fer.caloriecounter.api.ProgressApi;
import hr.fer.caloriecounter.model.Image;
import hr.fer.caloriecounter.model.Progress;
import hr.fer.caloriecounter.model.UserDetail;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProgressActivity extends AppCompatActivity implements DatePickerFragment.IDateSetListener {
    private UserDetail user;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private ImageButton addButton;
    private Dialog dialog;
    private File imageFile;
    private Progress progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        user = (UserDetail) getIntent().getSerializableExtra("user");

        getSupportActionBar().hide();

        initUI();
        initListeners();
        getProgressRetrofit();
    }

    private void initUI() {
        addButton = findViewById(R.id.progress_add_button);

        recyclerView = findViewById(R.id.progress_recycler_view);
        recyclerAdapter = new RecyclerAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
    }

    private void initListeners() {
        addButton.setOnClickListener(view -> {
            addProgressDialog();
        });
    }

    private void addProgressDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.progress_add_dialog);

        EditText weightText = dialog.findViewById(R.id.add_progress_weight);
        Button dateButton = dialog.findViewById(R.id.add_progress_date);
        dateButton.setText(LocalDate.now().toString());
        ImageButton photoButton = dialog.findViewById(R.id.add_progress_photo);
        Button addButton = dialog.findViewById(R.id.add_progress_add_btn);

        DatePickerFragment newFragemnt = new DatePickerFragment();
        ((DatePickerFragment) newFragemnt).setIDateSetListener(this);

        dateButton.setOnClickListener(view -> {
            newFragemnt.show(getSupportFragmentManager(), "datePicker");
        });

        photoButton.setOnClickListener(view -> {
            photoPicker.launch("image/*");
            photoButton.setBackgroundColor(Color.parseColor("#4fe37a"));
        });

        addButton.setOnClickListener(view -> {
            if (weightText.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter your weight", Toast.LENGTH_SHORT).show();
            } else if (imageFile == null) {
                Toast.makeText(this, "Please upload your photo", Toast.LENGTH_SHORT).show();
            } else {
                progress = new Progress();
                progress.setDate(dateButton.getText().toString());
                progress.setWeight(Float.parseFloat(weightText.getText().toString()));
                progress.setUserId(user.getId());

                photoRetrofit();

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void getProgressRetrofit() {
        Retrofit retrofit = NetworkClient.retrofit();

        ProgressApi progressApi = retrofit.create(ProgressApi.class);
        Call<List<Progress>> call = progressApi.getProgress(user.getId());

        call.enqueue(new Callback<List<Progress>>() {
            @Override
            public void onResponse(Call<List<Progress>> call, Response<List<Progress>> response) {
                if (response.code() == 200) {
                    recyclerAdapter.setProgress(response.body());
                    recyclerView.setAdapter(recyclerAdapter);
                } else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Progress>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void addProgressRetrofit() {
        Retrofit retrofit = NetworkClient.retrofit();

        ProgressApi progressApi = retrofit.create(ProgressApi.class);
        Call<Progress> call = progressApi.saveProgress(progress);

        call.enqueue(new Callback<Progress>() {
            @Override
            public void onResponse(Call<Progress> call, Response<Progress> response) {
                if (response.code() == 200) {
                    getProgressRetrofit();
                    Toast.makeText(ProgressActivity.this, "Progress entered", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<Progress> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void photoRetrofit() {
        Retrofit retrofit = NetworkClient.retrofit();

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part parts = MultipartBody.Part.createFormData("image", imageFile.getName(), requestBody);

        ImageApi imageApi = retrofit.create(ImageApi.class);
        Call<Image> call = imageApi.saveImage(parts);

        call.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(Call<Image> call, Response<Image> response) {
                if (response.code() == 200) {
                    progress.setImage(response.body());
                    addProgressRetrofit();
                } else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<Image> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    @Override
    public void processDate(int year, int month, int day) {
        LocalDate selectedDate = LocalDate.of(year, month + 1, day);
        Button dateButton = dialog.findViewById(R.id.add_progress_date);
        dateButton.setText(selectedDate.toString());
    }

    ActivityResultLauncher<String> photoPicker = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                try {
                    String path = PathUtil.getPath(this, uri);
                    imageFile = new File(path);
                    Toast.makeText(ProgressActivity.this, "Photo selected", Toast.LENGTH_SHORT).show();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            });
}
