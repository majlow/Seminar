package com.example.myapplication.NavigationFragment;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.myapplication.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.TimeUnit;


public class OTPCodeFragment extends Fragment {
    private View mView;
    EditText inputmobile, inputcode1, inputcode2, inputcode3, inputcode4, inputcode5, inputcode6;
    Button buttongetotp,buttonverify, down;
    TextView textResendOTP, longtext, entermobilephone, enterotpcode;
    LinearLayout layout_inputnumber, layout_inputcode, layout_resendOTP;
    ProgressBar progressBar;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    StorageReference ref;
     private String verificationId;
Intent intent = new Intent();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView =  inflater.inflate(R.layout.fragment_otp_code, container, false);
        longtext = mView.findViewById(R.id.longtext);
        entermobilephone = mView.findViewById(R.id.entermobilephone);
        layout_inputnumber = mView.findViewById(R.id.layout_inputnumber);
        inputmobile = mView.findViewById(R.id.inputmobile);
        buttongetotp = mView.findViewById(R.id.buttongetotp);
        enterotpcode = mView.findViewById(R.id.enterotpcode);
        layout_inputcode = mView.findViewById(R.id.layout_inputcode);
        layout_resendOTP = mView.findViewById(R.id.layout_resendOTP);
        textResendOTP = mView.findViewById(R.id.textResendOTP);
        buttonverify = mView.findViewById(R.id.buttonverify);
        inputcode1 = mView.findViewById(R.id.inputcode1);
        inputcode2 = mView.findViewById(R.id.inputcode2);
        inputcode3 = mView.findViewById(R.id.inputcode3);
        inputcode4 = mView.findViewById(R.id.inputcode4);
        inputcode5 = mView.findViewById(R.id.inputcode5);
        inputcode6 = mView.findViewById(R.id.inputcode6);
        progressBar = mView.findViewById(R.id.progressBar);
        down=mView.findViewById(R.id.down);
        verificationId = requireActivity().getIntent().getStringExtra("verificationId");

        down.setOnClickListener(v -> download());
        buttongetotp.setOnClickListener(v -> {
            if (inputmobile.getText().toString().trim().isEmpty()) {
                Toast.makeText(requireActivity().getApplication(), "Enter Mobile", Toast.LENGTH_SHORT).show();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            buttongetotp.setVisibility(View.INVISIBLE);
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+84" + inputmobile.getText().toString(), 60, TimeUnit.SECONDS,requireActivity(),new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        }
                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            progressBar.setVisibility(View.GONE);
                            buttongetotp.setVisibility(View.VISIBLE);
                            Toast.makeText(requireActivity().getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onCodeSent(@NonNull String VerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            verificationId = VerificationId;
                            intent.putExtra("mobile", Boolean.parseBoolean(inputmobile.getText().toString()));
                            intent.putExtra("verification", verificationId);
                            Toast.makeText(requireActivity().getApplication(),"OTP Code Send",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            longtext.setVisibility(View.GONE);
                            entermobilephone.setVisibility(View.GONE);
                            layout_inputnumber.setVisibility(View.GONE);
                            buttongetotp.setVisibility(View.GONE);
                            enterotpcode.setVisibility(View.VISIBLE);
                            layout_inputcode.setVisibility(View.VISIBLE);
                            layout_resendOTP.setVisibility(View.VISIBLE);
                            buttonverify.setVisibility(View.VISIBLE);
                        }

                            }
                        );
        });
        setupOTPInput();
        buttonverify.setOnClickListener(v -> {
            if(inputcode1.getText().toString().trim().isEmpty()
                    || inputcode2.getText().toString().trim().isEmpty()
                    || inputcode3.getText().toString().trim().isEmpty()
                    || inputcode4.getText().toString().trim().isEmpty()
                    || inputcode5.getText().toString().trim().isEmpty()
                    || inputcode6.getText().toString().trim().isEmpty()){
                Toast.makeText(requireActivity().getApplication(), "Please Enter valid code", Toast.LENGTH_SHORT).show();
                return ;
            }
            String code =
                    inputcode1.getText().toString() +
                            inputcode2.getText().toString()+
                            inputcode3.getText().toString()+
                            inputcode4.getText().toString()+
                            inputcode5.getText().toString()+
                            inputcode6.getText().toString();
            if(verificationId !=null){
                progressBar.setVisibility(View.VISIBLE);
                buttonverify.setVisibility(View.INVISIBLE);
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                        verificationId, code);
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                        .addOnCompleteListener(task -> {
                            progressBar.setVisibility(View.GONE);
                            buttonverify.setVisibility(View.VISIBLE);
                            if(task.isSuccessful()){
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                Toast.makeText(requireActivity().getApplication(),"The verification code is valid",Toast.LENGTH_SHORT).show();
                                down.setVisibility(View.VISIBLE);
                            }else {
                                Toast.makeText(requireActivity().getApplication(),"The verification code is invalid",Toast.LENGTH_SHORT).show();
                                down.setVisibility(View.GONE);
                            }
                        });
            }
        });
        textResendOTP.setOnClickListener(v -> PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+84" + inputmobile.getText().toString(), 60, TimeUnit.SECONDS,
                requireActivity(), new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential){
                    }
                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(requireActivity().getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        verificationId = newVerificationId;
                        Toast.makeText(requireActivity().getApplication(),"OTP Code Send",Toast.LENGTH_SHORT).show();
                    }
                }
        ));
        return mView;
    }


    private void download() {
        storageReference= FirebaseStorage.getInstance().getReference();
        ref=storageReference.child("QualityChecked.pdf");
        final MaterialDialog mDialog = new MaterialDialog.Builder(requireContext()).content("Downloading.....")
                .progress(true, 0)
                .build();
        mDialog.show();
        ref.getDownloadUrl().addOnSuccessListener(uri -> {
            String url=uri.toString();
            downloadFile(requireContext(),"QualityChecked",".pdf", (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()),url);
                Toast.makeText(requireActivity().getApplication(),"Download file Successfully",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            longtext.setVisibility(View.VISIBLE);
            entermobilephone.setVisibility(View.VISIBLE);
            inputmobile.getText().clear();
            layout_inputnumber.setVisibility(View.VISIBLE);
            buttongetotp.setVisibility(View.VISIBLE);
            enterotpcode.setVisibility(View.GONE);
            layout_inputcode.setVisibility(View.GONE);
            layout_resendOTP.setVisibility(View.GONE);
            buttonverify.setVisibility(View.GONE);
            down.setVisibility(View.GONE);

            mDialog.dismiss();

        }).addOnFailureListener(e -> {
            Toast.makeText(requireActivity().getApplication(),"Download Failed",Toast.LENGTH_SHORT).show();
            mDialog.dismiss();


        });

    }

    public void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {


        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        downloadmanager.enqueue(request);
    }

    private void setupOTPInput(){
        inputcode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    inputcode2.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        inputcode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    inputcode3.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        inputcode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    inputcode4.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        inputcode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    inputcode5.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        inputcode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    inputcode6.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}