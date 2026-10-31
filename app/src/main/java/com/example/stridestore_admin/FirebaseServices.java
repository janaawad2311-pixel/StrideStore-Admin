package com.example.stridestore_admin;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;


public class FirebaseServices {

    private static FirebaseServices instance;

    private final FirebaseAuth auth;
    private final FirebaseFirestore db;
    private final FirebaseStorage storage;

    // Private Constructor
    private FirebaseServices() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }


    public static FirebaseServices getInstance() {
        if (instance == null) {
            instance = new FirebaseServices();
        }
        return instance;
    }



    public FirebaseAuth getAuth() {
        return auth;
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    public FirebaseStorage getStorage() {
        return storage;
    }


}
