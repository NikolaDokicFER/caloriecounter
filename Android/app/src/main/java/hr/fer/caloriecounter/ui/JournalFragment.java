package hr.fer.caloriecounter.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import hr.fer.caloriecounter.R;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JournalFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_journal, container, false);
    }
}
