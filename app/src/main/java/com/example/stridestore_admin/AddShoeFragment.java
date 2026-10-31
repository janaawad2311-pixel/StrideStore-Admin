
package com.example.stridestore_admin;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

public class AddShoeFragment extends Fragment {

    private EditText etId, etName, etBrand;
    private EditText etSize, etPrice, etQuantity;
    private ImageView ivLogo;
    private Button btnAdd;

    private Uri selectedImageUri;
    private FirebaseServices fbs;

    // Opens the gallery
    private final ActivityResultLauncher<String> pickImage =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri != null) {
                            selectedImageUri = uri;
                            ivLogo.setImageURI(uri);
                        }
                    });

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_add_shoe,
                container,
                false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        fbs = FirebaseServices.getInstance();

        etId = view.findViewById(R.id.etId);
        etName = view.findViewById(R.id.etName);
        etBrand = view.findViewById(R.id.etBrand);
        etSize = view.findViewById(R.id.etSize);
        etPrice = view.findViewById(R.id.etPrice);
        etQuantity = view.findViewById(R.id.etQuantity);

        ivLogo = view.findViewById(R.id.ivLogo);
        btnAdd = view.findViewById(R.id.btnAdd);

        ivLogo.setOnClickListener(v ->
                pickImage.launch("image/*"));

        btnAdd.setOnClickListener(v -> addShoe());
    }

    private void addShoe() {

        String id = etId.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String brand = etBrand.getText().toString().trim();
        String size = etSize.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String quantity = etQuantity.getText().toString().trim();

        // Check the form before we talk to Firebase
        if (id.isEmpty() || name.isEmpty() || brand.isEmpty()
                || size.isEmpty() || price.isEmpty()
                || quantity.isEmpty()) {

            Toast.makeText(getContext(),
                    "Please fill in all shoe details",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageUri == null) {
            Toast.makeText(getContext(),
                    "Please pick a shoe image",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        btnAdd.setEnabled(false); // stop double clicks while we upload

        // A new, empty document gives us a unique id for the shoe
        DocumentReference doc = fbs.getDb()
                .collection("shoes").document();

        StorageReference shoeRef = fbs.getStorage().getReference()
                .child("shoes/" + doc.getId() + ".jpg");

        // Step 1: upload the shoe image to Storage
        shoeRef.putFile(selectedImageUri)
                .addOnSuccessListener(snapshot -> {

                    // Step 2: ask Storage for the image's download URL
                    shoeRef.getDownloadUrl()
                            .addOnSuccessListener(uri ->
                                    saveShoe(doc, id, name, brand,
                                            size, price, quantity, uri.toString()))
                            .addOnFailureListener(this::showError);
                })
                .addOnFailureListener(this::showError);
    }


    // Step 3: create the shoe object and save it in Firestore
    private void saveShoe(DocumentReference doc, String id,
                          String name, String brand,
                          String size, String price,
                          String quantity, String imageUrl) {

        StrideStore shoe = new StrideStore(
                id, name, brand,
                Integer.parseInt(size),
                Double.parseDouble(price),
                Integer.parseInt(quantity)
        );

        shoe.setImageUrl(imageUrl);

        doc.set(shoe)
                .addOnSuccessListener(unused -> {
                    if (!isAdded()) return;

                    Toast.makeText(getContext(), "Shoe added!",
                            Toast.LENGTH_SHORT).show();

                    // Step 4: go to the Show screen
                    MainActivity activity = (MainActivity) requireActivity();
                    activity.showFragment(new ShowShoesFragment());
                })
                .addOnFailureListener(this::showError);
    }

    private void showError(Exception e) {
        if (!isAdded()) return;

        btnAdd.setEnabled(true);

        Toast.makeText(getContext(),
                "Failed: " + e.getMessage(),
                Toast.LENGTH_LONG).show();
    }

}
