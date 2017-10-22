package com.krishnchinya.personalhealthmonitoringsystem.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.krishnchinya.personalhealthmonitoringsystem.R;
import com.krishnchinya.personalhealthmonitoringsystem.other.GlobalVars;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * Created by KrishnChinya on 2/11/17.
 */

public class Login_Activity extends Activity {
    private TextInputLayout usernameInputLayout, passwordInputLayout;
    private Button loginButton, newUser, forgotPass;
    private EditText usernameEditText, passwordEditText;
    myTextWatcher watcher1, watcher2;
    GlobalVars globalVars;


    //FingerPrint Scanner
    KeyStore keyStore;
    // Variable used for storing the key in the Android Keystore container
    private static final String KEY_NAME = "androidHive";
    private Cipher cipher;
    private TextView errorText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //getActionBar().hide();
        final AlertDialog.Builder builder = new AlertDialog.Builder(Login_Activity.this);

        usernameInputLayout = (TextInputLayout) findViewById(R.id.input_username);
        passwordInputLayout = (TextInputLayout) findViewById(R.id.input_password);

        usernameEditText = (EditText) findViewById(R.id.userName);
        passwordEditText = (EditText) findViewById(R.id.password);

        loginButton = (Button) findViewById(R.id.login);
        newUser = (Button) findViewById(R.id.NewUser);
        forgotPass = (Button) findViewById(R.id.forgotPass);

        watcher1 = new myTextWatcher(usernameEditText,usernameInputLayout, Login_Activity.this);
        watcher2 = new myTextWatcher(passwordEditText,passwordInputLayout,Login_Activity.this);

        usernameEditText.addTextChangedListener(watcher1);
        passwordEditText.addTextChangedListener(watcher2);

        errorText = (TextView) findViewById(R.id.errorText);

        //function to check fingersensor present or not
        fingerPrint(errorText);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!watcher1.validateUserName()) {
                    return;
                }
                if (!watcher2.validatePassword()) {
                    return;
                }

                DB_Setter_Getter dbSetterGetter = new DB_Setter_Getter(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
                DB_Handler db_handler = new DB_Handler(Login_Activity.this);
                String[] details = db_handler.getcredentials(dbSetterGetter);

                if(details[0].equals(dbSetterGetter.getMailID()) && details[1].equals(dbSetterGetter.getPassword()))
                {
                    //set the global variables for further use.
                    globalVars = (GlobalVars) getApplicationContext();
                    globalVars.setUsername(details[2].toString());
                    globalVars.setMailid(details[0].toString());

                    Intent intent = new Intent(Login_Activity.this, MainMenu.class);
                    //startActivityForResult(intent,1);
                    startActivity(intent);
                    finish();
                }else
                {
                    builder.setTitle("");
                    builder.setMessage("Wrong Username/Password");
                    builder.show();
                }

            }
        });

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Activity.this ,Registration.class);
                startActivityForResult(intent,1);
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Activity.this, ForgotPassword.class);
                startActivityForResult(intent,1);
            }
        });


    }

     void fingerPrint(TextView errorText) {
         // Initializing both Android Keyguard Manager and Fingerprint Manager
         KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
         FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

         // Check whether the device has a Fingerprint sensor.
         if (!fingerprintManager.isHardwareDetected()) {
             /**
              * An error message will be displayed if the device does not contain the fingerprint hardware.
              * However if you plan to implement a default authentication method,
              * you can redirect the user to a default authentication activity from here.
              * Example:
              * Intent intent = new Intent(this, DefaultAuthenticationActivity.class);
              * startActivity(intent);
              */
             errorText.setText("Your Device does not have a Fingerprint Sensor");
         } else {
             // Checks whether fingerprint permission is set on manifest
             if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                 errorText.setText("Fingerprint authentication permission not enabled");
             } else {
                 // Check whether at least one fingerprint is registered
                 if (!fingerprintManager.hasEnrolledFingerprints()) {
                     errorText.setText("Register at least one fingerprint in Settings");
                 } else {
                     // Checks whether lock screen security is enabled or not
                     if (!keyguardManager.isKeyguardSecure()) {
                         errorText.setText("Lock screen security not enabled in Settings");
                     } else {
                         generateKey();


                         if (cipherInit()) {
                             FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                             FingerprintHandler helper = new FingerprintHandler(this);
                             helper.startAuth(fingerprintManager, cryptoObject);
                         }
                     }
                 }
             }
         }
     }


    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }


        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }


        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }


        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

}
