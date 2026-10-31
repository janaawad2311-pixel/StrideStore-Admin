
package com.example.stridestore_admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class ShowShoesFragment extends Fragment
        implements ShoeAdapter.OnShoeActionListener {

    private FirebaseServices fbs;
    private ArrayList<StrideStore> shoes = new ArrayList<>();
    private ShoeAdapter adapter;
    private TextView tvEmpty;


    // Creates and returns the layout for the Show Shoes screen.
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_show_shoes,
                container,
                false);
    }


    // Connects the XML views to Java, sets up the RecyclerView,
    // and loads the shoes from Firebase.
    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        fbs = FirebaseServices.getInstance();

        RecyclerView recyclerView =
                view.findViewById(R.id.recyclerView);

        tvEmpty = view.findViewById(R.id.tvEmpty);

        adapter = new ShoeAdapter(shoes, this);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext()));

        recyclerView.setAdapter(adapter);

        loadShoes();
    }


    // Reads all shoes from Firebase Firestore and displays
    // them in the RecyclerView.
    private void loadShoes() {

        fbs.getDb().collection("shoes").get()
                .addOnSuccessListener(result -> {

                    if (!isAdded()) return;

                    shoes.clear();

                    for (DocumentSnapshot document :
                            result.getDocuments()) {

                        StrideStore item =
                                document.toObject(StrideStore.class);

                        if (item != null) {

                            // Gets the document ID from Firebase.
                            item.setId(document.getId());

                            shoes.add(item);
                        }
                    }

                    // Refreshes the RecyclerView after loading data.
                    adapter.notifyDataSetChanged();

                    // Shows a message if the shoes list is empty.
                    tvEmpty.setVisibility(
                            shoes.isEmpty() ?
                                    View.VISIBLE : View.GONE);
                })
                .addOnFailureListener(e ->
                        showMessage("Could not load: " +
                                e.getMessage()));
    }


    // Opens the Edit Shoe dialog when the user clicks Update.
    @Override
    public void onUpdateClick(StrideStore shoes) {

        EditShoeDialog dialog =
                EditShoeDialog.newInstance(shoes);

        dialog.setOnSavedListener(this::loadShoes);

        dialog.show(
                getChildFragmentManager(),
                "edit_shoe");
    }


    // Shows a confirmation dialog when the user clicks Delete.
    @Override
    public void onDeleteClick(StrideStore shoes) {

        new AlertDialog.Builder(requireContext())
                .setTitle("Delete shoe")
                .setMessage("Are you sure you want to delete "
                        + shoes.getName() + "?")
                .setPositiveButton("Yes",
                        (dialog, which) -> deleteShoe(shoes))
                .setNegativeButton("No", null)
                .show();
    }


    // Deletes the selected shoe from Firebase Firestore
    // and refreshes the shoes list.
    private void deleteShoe(StrideStore shoes) {

        fbs.getDb().collection("shoes")
                .document(shoes.getId())
                .delete()
                .addOnSuccessListener(unused -> {
                    showMessage("Shoe deleted");
                    loadShoes();
                })
                .addOnFailureListener(e ->
                        showMessage("Delete failed: " +
                                e.getMessage()));
    }


    // Displays a short Toast message to the user.
    private void showMessage(String message) {

        if (!isAdded()) return;

        Toast.makeText(
                requireContext(),
                message,
                Toast.LENGTH_SHORT).show();
    }
}
