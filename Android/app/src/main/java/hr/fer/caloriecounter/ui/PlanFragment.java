package hr.fer.caloriecounter.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import hr.fer.caloriecounter.NetworkClient;
import hr.fer.caloriecounter.R;
import hr.fer.caloriecounter.api.FoodApi;
import hr.fer.caloriecounter.model.Food;
import hr.fer.caloriecounter.model.UserDetail;
import hr.fer.caloriecounter.sqlite.CalorieCounterFoodDbHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PlanFragment extends Fragment {
    private View view;
    private Bundle bundle;
    private UserDetail user;
    private RecyclerView recyclerView;
    private RecyclerAdapterPlan recyclerAdapterPlan;
    private CalorieCounterFoodDbHelper dbHelper;
    private ArrayList<String> allFoodList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_plan, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bundle = getArguments();
        user = (UserDetail) bundle.getSerializable("user");

        dbHelper = new CalorieCounterFoodDbHelper(getContext());
        allFoodList = new ArrayList<>();
        Cursor cursor = dbHelper.fetchEntries();

        while (cursor.moveToNext()) {
            allFoodList.add(cursor.getString(1) + "♥︎");
        }
        cursor.close();

        initUI();
        foodRetrofit();
    }

    private void initUI(){
        recyclerView = view.findViewById(R.id.plan_recycler_view);

        recyclerAdapterPlan = new RecyclerAdapterPlan(view.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(recyclerAdapterPlan);
    }

    private void foodRetrofit(){
        Retrofit retrofit = NetworkClient.retrofit();

        FoodApi foodApi = retrofit.create(FoodApi.class);
        Call<List<Food>> call = foodApi.getAllFood();

        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                if (response.code() == 200) {
                    ArrayList<String> helpList = new ArrayList<>(allFoodList);
                    for (int i = 0; i < helpList.size(); i++)
                        helpList.set(i, helpList.get(i).substring(0, helpList.get(i).length() - 2));
                    allFoodList.addAll(response.body().stream().map(Food::getName).collect(Collectors.toList()));
                    allFoodList.removeAll(helpList);

                    recyclerAdapterPlan.setFood(allFoodList);
                    recyclerView.setAdapter(recyclerAdapterPlan);
                } else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}
