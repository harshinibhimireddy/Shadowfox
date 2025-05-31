package com.example.authapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AuthActivity extends AppCompatActivity {

    private EditText emailInput, usernameInput, passwordInput, confirmPasswordInput;
    private Button actionButton, toggleButton;
    private TextView errorText, toggleText;
    private boolean isSignUpMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // Initialize UI components
        emailInput = findViewById(R.id.emailInput);
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        actionButton = findViewById(R.id.actionButton);
        toggleButton = findViewById(R.id.toggleButton);
        errorText = findViewById(R.id.errorText);
        toggleText = findViewById(R.id.toggleText);

        updateUIForMode();

        toggleButton.setOnClickListener(v -> {
            isSignUpMode = !isSignUpMode;
            updateUIForMode();
        });

        actionButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();

            SharedPreferences prefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            // Validation
            if (email.isEmpty() || password.isEmpty() || (isSignUpMode && username.isEmpty())) {
                errorText.setText(getString(R.string.error_fill_all_fields));
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                errorText.setText(getString(R.string.error_invalid_email));
                return;
            }

            if (password.length() < 6) {
                errorText.setText(getString(R.string.error_pass_must_be_6));
                return;
            }

            if (isSignUpMode) {
                if (!password.equals(confirmPassword)) {
                    errorText.setText(getString(R.string.error_password_mismatch));
                    return;
                }

                // Store credentials
                editor.putString("email", email);
                editor.putString("username", username);
                editor.putString("password", password);
                editor.apply();

                Toast.makeText(this, "Signed up successfully!", Toast.LENGTH_SHORT).show();
            } else {
                // Retrieve credentials
                String savedEmail = prefs.getString("email", null);
                String savedPassword = prefs.getString("password", null);
                String savedUsername = prefs.getString("username", null);

                if (savedEmail == null || savedPassword == null) {
                    errorText.setText(getString(R.string.no_acc_found));
                    return;
                }

                if (!email.equals(savedEmail) || !password.equals(savedPassword)) {
                    errorText.setText(getString(R.string.invalid_email_pass));
                    return;
                }

                Toast.makeText(this, "Logged in successfully!", Toast.LENGTH_SHORT).show();
                username = savedUsername;
            }

            errorText.setText("");
            // Redirect to WelcomeActivity
            Intent intent = new Intent(AuthActivity.this, WelcomeActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
            finish();
        });
    }

    private void updateUIForMode() {
        if (isSignUpMode) {
            actionButton.setText(getString(R.string.signup_button));
            toggleButton.setText(getString(R.string.switch_to_login));
            toggleText.setText(getString(R.string.toggle_to_login));
            confirmPasswordInput.setVisibility(View.VISIBLE);
            usernameInput.setVisibility(View.VISIBLE);
        } else {
            actionButton.setText(getString(R.string.login_button));
            toggleButton.setText(getString(R.string.switch_to_signup));
            toggleText.setText(getString(R.string.toggle_to_signup));
            confirmPasswordInput.setVisibility(View.GONE);
            usernameInput.setVisibility(View.GONE);
        }
    }
}
