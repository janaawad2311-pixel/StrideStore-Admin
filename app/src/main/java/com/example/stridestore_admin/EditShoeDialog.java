
package com.example.stridestore_admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EditShoeDialog extends DialogFragment {

    public interface OnSavedListener {
        void onSaved();
    }

    private static final String ARG_SHOES = "shoes";

    private StrideStore shoes;
    private OnSavedListener onSavedListener;

    private EditText etId, etName, etBrand;
    private EditText etSize, etPrice, etQuantity;

    public static EditShoeDialog newInstance(StrideStore shoes) {

        EditShoeDialog dialog = new EditShoeDialog();

        Bundle args = new Bundle();
        args.putSerializable(ARG_SHOES, shoes);

        dialog.setArguments(args);

        return dialog;
    }

    public void setOnSavedListener(OnSavedListener listener) {
        this.onSavedListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.dialog_edit_shoe,
                container,
                false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        shoes = (StrideStore) requireArguments()
                .getSerializable(ARG_SHOES);

        etId = view.findViewById(R.id.etId);
        etName = view.findViewById(R.id.etName);
        etBrand = view.findViewById(R.id.etBrand);
        etSize = view.findViewById(R.id.etSize);
        etPrice = view.findViewById(R.id.etPrice);
        etQuantity = view.findViewById(R.id.etQuantity);

        Button btnSave = view.findViewById(R.id.btnSave);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        // Fill the fields with the current values
        etId.setText(shoes.getId());
        etName.setText(shoes.getName());
        etBrand.setText(shoes.getBrand());
        etSize.setText(String.valueOf(shoes.getSize()));
        etPrice.setText(String.valueOf(shoes.getPrice()));
        etQuantity.setText(String.valueOf(shoes.getQuantity()));

        btnSave.setOnClickListener(v -> saveChanges());
        btnCancel.setOnClickListener(v -> dismiss());
    }

    @Override
    public void onStart() {
        super.onStart();

        // Make the dialog use the full screen width
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void saveChanges() {

        String name = etName.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Name is required");
            return;
        }

        shoes.setName(name);

        shoes.setBrand(
                etBrand.getText().toString().trim());

        shoes.setSize(Integer.parseInt(
                etSize.getText().toString().trim()));

        shoes.setPrice(Double.parseDouble(
                etPrice.getText().toString().trim()));

        shoes.setQuantity(Integer.parseInt(
                etQuantity.getText().toString().trim()));
        shoes.setQuantity(Integer.parseInt(
                etQuantity.getText().toString().trim()));

        // Overwrite the document that has this shoe's id
        FirebaseServices.getInstance().getDb()
                .collection("shoes").document(shoes.getId())
                .set(shoes)
                .addOnSuccessListener(unused -> {
                    if (!isAdded()) return;

                    Toast.makeText(getContext(),
                            "Changes saved",
                            Toast.LENGTH_SHORT).show();

                    if (onSavedListener != null) {
                        onSavedListener.onSaved();
                    }

                    dismiss();
                })
                .addOnFailureListener(e -> {
                    if (!isAdded()) return;

                    Toast.makeText(getContext(),
                            "Save failed: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });


    }

}
