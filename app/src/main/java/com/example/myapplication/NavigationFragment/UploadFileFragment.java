package com.example.myapplication.NavigationFragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.myapplication.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class UploadFileFragment extends Fragment {
    EditText editText;
    private View mView;
    Button btn_select,btn_upload;

    StorageReference storageReference;
    DatabaseReference databaseReference;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

         mView = inflater.inflate(R.layout.fragment_upload_file, container, false);
        editText=mView.findViewById(R.id.editText);
        btn_select = mView.findViewById(R.id.btn_select);
        btn_upload = mView.findViewById(R.id.btn_upload);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        btn_select.setOnClickListener(v -> selectPDF());
//        btn_upload.setOnClickListener(v -> uploadPDFFileFirebase());
        // Inflate the layout for this fragment
        return mView;
    }
    private void selectPDF() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
//        onDataResult(intent);
        startActivityForResult(Intent.createChooser(intent,"PDF FILE SELECTED"),12);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 12 && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            Toast.makeText(requireActivity().getApplication(), "PDF FILE SELECTED", Toast.LENGTH_SHORT).show();
            editText.getText().clear();
            editText.setText(data.getDataString()
                    .substring(data.getDataString().lastIndexOf("/")+1));
            btn_upload.setOnClickListener(v -> uploadPDFFileFirebase(data.getData()));
        }
    }
    private void uploadPDFFileFirebase(Uri data) {

        final MaterialDialog mDialog = new MaterialDialog.Builder(requireContext()).content("Uploading.....")
                .progress(true, 0)
                .build();
        mDialog.show();
        StorageReference  reference = storageReference.child( System.nanoTime()+".pdf");
        reference.putFile(data).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isComplete());
            Uri uri = uriTask.getResult();
            putPDF putPDF = new putPDF(editText.getText().toString(),uri.toString());
            databaseReference.child(Objects.requireNonNull(databaseReference.push().getKey())).setValue(putPDF);
            Toast.makeText(requireActivity().getApplication(), "File Uploaded to Storage", Toast.LENGTH_SHORT).show();
            editText.getText().clear();
            mDialog.dismiss();
        }).addOnProgressListener(snapshot -> {
//                double progress = (100.0*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
//            Log.i("Listener", "Progress: " + progress);
            mDialog.setContent("File Uploading...");

        });
    }}

