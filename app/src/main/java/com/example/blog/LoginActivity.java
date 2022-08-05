package com.example.blog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.blog.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private static final int RC_SIGN_IN = 308;
    private FirebaseAuth auth;
    private GoogleSignInClient googleClient;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //forcing light theme

        auth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1000949087073-ust5b15vcs61jamb22qh945ivedibm9t.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleClient = GoogleSignIn.getClient(this, gso);

        //SignIn Button Click
        binding.googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = googleClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account.getIdToken());


            } catch (ApiException e) {
                Toast.makeText(LoginActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // signIN Success
                            firebaseUser = auth.getCurrentUser();

                            //Saving the user Details inside SharedPreference
                            SharedPreferences userPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
                            SharedPreferences.Editor editor = userPreferences.edit();
                            editor.putString("USER_ID", firebaseUser.getUid());
                            editor.putString("USER_NAME", firebaseUser.getDisplayName());
                            editor.putString("USER_EMAIL", firebaseUser.getEmail());
                            editor.putString("USER_PHONE", firebaseUser.getPhoneNumber());
                            editor.putString("USER_PROFILE", firebaseUser.getPhotoUrl().toString());
                            Toast.makeText(LoginActivity.this, "Welcome Reader!", Toast.LENGTH_SHORT).show();
                            //Activity Change
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            //signIN Failed
                            Toast.makeText(LoginActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}