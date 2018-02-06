package penserlabs.womensafety;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SigninActivity extends AppCompatActivity {


    public ProgressDialog mProgressDialog;
    public EditText Email_ET, Password_ET;
    private Button Signin_BT;
    private static final String TAG = "EmailPassword";


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        Email_ET = findViewById(R.id.Signin_Email_ET);
        Password_ET = findViewById(R.id.Signin_Password_ET);
        Signin_BT = findViewById(R.id.Signin_BT);

        mAuth = FirebaseAuth.getInstance();

        Signin_BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(Email_ET.getText().toString(), Password_ET.getText().toString());
            }
        });
    }


    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SigninActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]...
    }
    private boolean validateForm() {
        boolean valid = true;

        String email = Email_ET.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Email_ET.setError("Required.");
            valid = false;
        } else {
            Email_ET.setError(null);
        }

        String password = Password_ET.getText().toString();
        if (TextUtils.isEmpty(password)) {
            Password_ET.setError("Required.");
            valid = false;
        } else {
            Password_ET.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            Intent intent = new Intent(SigninActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Please Login", Toast.LENGTH_SHORT).show();

        }
    }
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    public void Signup_TV(View view) {
        Intent signupintent = new Intent(SigninActivity.this, LoginActivity.class);
        startActivity(signupintent);
        finish();
    }
}
