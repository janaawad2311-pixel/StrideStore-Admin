
package com.example.stridestore_admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class AdminFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        TextView textView = new TextView(requireContext());
        textView.setText("Welcome to StrideStore Admin");
        textView.setTextSize(24);

        return textView;
    }
}
