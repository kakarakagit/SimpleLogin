package com.kakaraka.simplelogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;

    // Login Layout
    private EditText mEmail, mPassword;
    private AppCompatButton mLogin;
    private TextView mGotoSignup;

    // Signup Layout
    private EditText mUserSignup, mEmailsignup, mPasswordsignup;
    private AppCompatButton mSignup;
    private TextView mgotologin;


    private ViewFlipper viewFlipper;


    ProgressDialog progressDialog;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        viewFlipper = findViewById(R.id.view_flipper);

        // Login Layout
        mEmail = findViewById(R.id.email_login);
        mPassword = findViewById(R.id.password_login);
        mLogin = findViewById(R.id.button_login);
        mGotoSignup = findViewById(R.id.goto_signup);

        // Signup Layout
        mUserSignup = findViewById(R.id.username_signup);
        mEmailsignup = findViewById(R.id.email_signup);
        mPasswordsignup = findViewById(R.id.password_signup);
        mgotologin = findViewById(R.id.goto_login);
        mSignup = findViewById(R.id.button_signup);


        mAuth = FirebaseAuth.getInstance();

        // Login User
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                ProgressDialog();
                if (!email.equals("") && !password.equals(""))
                {
                    SignIn(email,password);

                }else
                    {
                        Toast.makeText(MainActivity.this, "Cannot be empty!!", Toast.LENGTH_SHORT).show();
                    }

            }
        });


        // Sign Up User
        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String usernameSignup = mUserSignup.getText().toString();
                String emailUp = mEmailsignup.getText().toString();
                String passUp = mPasswordsignup.getText().toString();
                ProgressDialog();
                if (!emailUp.equals("") && !passUp.equals(""))
                {
                    SignUp(emailUp, passUp);

                } else
                {
                    Toast.makeText(MainActivity.this, "Cannot be empty!!", Toast.LENGTH_SHORT).show();
                }
            }
        });





        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }

        // ke layout Login
        mgotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLogIn(v);
            }
        });
        // ke layout SignUp
        mGotoSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSignUp(v);
            }
        });

    }
    //
    //
    //===========END OF ONCREATE=============END OF ONCREATE==============END OF ONCREATE===============END OF ONCREATE==========
    //===========END OF ONCREATE=============END OF ONCREATE==============END OF ONCREATE===============END OF ONCREATE==========
    //
    //


    //============== START OF FILTHY CODE ===============//



    public void ProgressDialog()
    {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Wait..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.cancel();
            }
        }, 1500);
    }




    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            //super.onBackPressed();
            //return;
            moveTaskToBack(true);

        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }




    public void gotoLogIn(View v)
    {
        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);
        viewFlipper.showPrevious();

        mgotologin.setEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mgotologin.setEnabled(true);
            }
        },1000);

        mUserSignup.getText().clear();
        mEmailsignup.getText().clear();
        mPasswordsignup.getText().clear();

    }

    public void gotoSignUp (View v)
    {
        viewFlipper.setInAnimation(this, R.anim.slide_in_right);
        viewFlipper.setOutAnimation(this, R.anim.slide_out_left);
        viewFlipper.showNext();

        mGotoSignup.setEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mGotoSignup.setEnabled(true);
            }
        },1000);

        mEmail.getText().clear();
        mPassword.getText().clear();

    }


    private void SignUp (final String emailUp, String passUp)
    {
        mAuth.createUserWithEmailAndPassword(emailUp, passUp)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Toast.makeText(MainActivity.this, "Berhasil Daftar", Toast.LENGTH_SHORT).show();
                            gotoLogIn(viewFlipper);

                            // field email otomatis di isi jika berhasil daftar
                            mEmail.setText(emailUp);

                           } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
    private void SignIn(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            Toast.makeText(MainActivity.this, "LOGIN Berhasil", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }



    private void updateUI(FirebaseUser currentUser) {
    }


}
