
package com.example.stridestore_admin;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars());

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );

        // Connect buttons
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnShow = findViewById(R.id.btnShow);

        // Open Add Shoe Fragment
        btnAdd.setOnClickListener(v ->
                showFragment(new AddShoeFragment()));

        // Open Show Shoes Fragment
        btnShow.setOnClickListener(v ->
                showFragment(new ShowShoesFragment()));

        // Show Admin Home Screen on first launch
        if (savedInstanceState == null) {
            showFragment(new AdminFragment());
        }
    }

    // Replace current Fragment
    public void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
