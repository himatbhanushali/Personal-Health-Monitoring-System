package com.krishnchinya.personalhealthmonitoringsystem.activity;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import com.krishnchinya.personalhealthmonitoringsystem.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by KrishnChinya on 2/11/17.
 */

public class Registration extends Activity
        implements EasyPermissions.PermissionCallbacks{

    EditText dob,etFirstName,etLastName,etWeight,etHeight,etPhone,etEmail,etPassword,etRePass;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormat;
    RadioButton btnMale ,btnFemale;
    ProgressDialog mProgress;

    Button signup;
    myTextWatcher watcher1,watcher2,watcher3,watcher4,watcher5,watcher6,watcher7,watcher8,watcher9;
    TextInputLayout input_etFirstName,input_etLastName,input_etWeight,
            input_etHeight,input_etPhone,input_etEmail,input_etpassword,input_etrepass;
    Toast toast;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    Gmail mService = null;
    GoogleAccountCredential mCredential;

    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {GmailScopes.GMAIL_LABELS, GmailScopes.GMAIL_COMPOSE,
            GmailScopes.GMAIL_INSERT, GmailScopes.GMAIL_MODIFY, GmailScopes.GMAIL_READONLY, GmailScopes.MAIL_GOOGLE_COM};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        final DB_Handler db_handler = new DB_Handler(Registration.this);

        dob = (EditText) findViewById(R.id.etdob);
        signup = (Button) findViewById(R.id.btnSignup);
        dob.setInputType(InputType.TYPE_NULL);

        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide the keyboard
                InputMethodManager inputManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(v.getWindowToken(),0);

                Calendar calenderinstance = Calendar.getInstance();
                datePickerDialog = new DatePickerDialog(Registration.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar newdate = Calendar.getInstance();
                        newdate.set(year,month,dayOfMonth);
                        dob.setText(dateFormat.format(newdate.getTime()));

                    }
                },calenderinstance.get(Calendar.YEAR), calenderinstance.get(Calendar.MONTH),calenderinstance.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        etFirstName = (EditText)findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etWeight = (EditText) findViewById(R.id.etWeight);
        etHeight = (EditText) findViewById(R.id.etHeight);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etRePass = (EditText) findViewById(R.id.etRePass);
        btnMale = (RadioButton)findViewById(R.id.btnMale);
        btnFemale = (RadioButton)findViewById(R.id.btnFeale);


        input_etFirstName = (TextInputLayout) findViewById(R.id.input_etFirstName);
        input_etLastName= (TextInputLayout) findViewById(R.id.input_etLastName);
        input_etWeight= (TextInputLayout) findViewById(R.id.input_etWeight);
        input_etHeight= (TextInputLayout) findViewById(R.id.input_etHeight);
        input_etPhone= (TextInputLayout) findViewById(R.id.input_etPhone);
        input_etEmail= (TextInputLayout) findViewById(R.id.input_etEmail);
        input_etpassword= (TextInputLayout) findViewById(R.id.input_etpassword);
        input_etrepass= (TextInputLayout) findViewById(R.id.input_etrepass);

        watcher1 = new myTextWatcher(etFirstName,input_etFirstName,Registration.this);
        watcher2 = new myTextWatcher(etLastName,input_etLastName,Registration.this);
        watcher3 = new myTextWatcher(etEmail,input_etEmail,Registration.this);
        watcher4 = new myTextWatcher(etWeight,input_etWeight,Registration.this);
        watcher5 = new myTextWatcher(etHeight,input_etHeight,Registration.this);
        watcher6 = new myTextWatcher(etPhone,input_etPhone,Registration.this);
        watcher7 = new myTextWatcher(etPassword,input_etpassword,Registration.this);
        watcher8 = new myTextWatcher(etRePass,etPassword,input_etrepass,Registration.this);
       // watcher9 = new myTextWatcher(btnMale,btnFemale,Registration.this);


        etFirstName.addTextChangedListener(watcher1);
        etLastName.addTextChangedListener(watcher2);
        etEmail.addTextChangedListener(watcher3);
        etWeight.addTextChangedListener(watcher4);
        etHeight.addTextChangedListener(watcher5);
        etPhone.addTextChangedListener(watcher6);
        etPassword.addTextChangedListener(watcher7);
        etRePass.addTextChangedListener(watcher8);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!watcher1.validateName()) | (!watcher2.validateName())) {
                    return;
                }
                if (!watcher3.validateUserName()) {
                    return;
                }
                if((!watcher4.validateWight()) | (!watcher5.validateHeight()) | (!watcher6.validatePhone())){
                    return;
                }
                if((!watcher8.validateregrepass()) | (!watcher8.validateregpassword())) {
                    return;
                }
//                if(!watcher9.validateGender())
//                {
//                    return;
//                }


                DB_Setter_Getter dbSetterGetter = new DB_Setter_Getter(etFirstName.getText().toString(),
                        etLastName.getText().toString(),dob.getText().toString(),"Male",etEmail.getText().toString(),
                        etWeight.getText().toString(),etHeight.getText().toString(),etPhone.getText().toString()
                        ,etPassword.getText().toString());

                if(db_handler.checkMail(dbSetterGetter)) {

                    db_handler.addRegistration(dbSetterGetter);
                    db_handler.addLogin(dbSetterGetter);

                    getResultsFromApi();
                    AsyncTask<String, Void, Void> send = new AsyncTask<String, Void, Void>() {


                        @Override
                        protected Void doInBackground(String... params) {
                            try {
                                HttpTransport transport = AndroidHttp.newCompatibleTransport();
                                JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
                                mService = new com.google.api.services.gmail.Gmail.Builder(
                                        transport, jsonFactory, mCredential)
                                        .build();

                                String user = "me";
                                MimeMessage message = createEmail(params[0], "phmsteam4@gmail.com", "New Registered User",
                                        "Hello User, \n \nYour have been registered with Personal " +
                                                "Health Monitoring System" +
                                                "Your UserName is : "+params[0]+
                                                "\nRegards, \nPHMS Team"
                                );
                                Message message1 = sendMessage(mService, user, message);

                                // return null;
                            } catch (Exception ex) {
                                return null;
                            }
                            return null;
                        };

                        @Override
                        protected void onPreExecute() {
                            mProgress.show();
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            mProgress.hide();
                        }

                    };


                    send.execute(etEmail.getText().toString());
                    db_handler.SetNewPassword(dbSetterGetter);

                   finish();


                }else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
                    builder.setTitle("Error");
                    builder.setMessage("Duplicate Mail Id");
                    builder.show();
                }
            }
        });

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling Gmail API ...");

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
    }



    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void getResultsFromApi() {
        if(!isGooglePlayServicesAvailable())
        {
            acquireGooglePlayServices();
        } else if(mCredential.getSelectedAccountName() == null)
        {
            chooseAccount();
        } else if (!isDeviceOnline()) {
            toast.setText("No network connection available.");
            toast.show();
        }
//        } else {
//            new MakeRequestTask(mCredential).execute();
//        }
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     *
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode  code indicating the result of the incoming
     *                    activity result.
     * @param data        Intent (containing result data) returned by incoming
     *                    activity result.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    toast.setText(
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.");
                    toast.show();
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Checks whether the device currently has a network connection.
     *
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     *
     * @return true if Google Play Services is available and up to
     * date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }


    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     *
     * @param connectionStatusCode code describing the presence (or lack of)
     *                             Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                Registration.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * Create a MimeMessage using the parameters provided.
     *
     * @param to email address of the receiver
     * @param from email address of the sender, the mailbox account
     * @param subject subject of the email
     * @param bodyText body text of the email
     * @return the MimeMessage to be used to send email
     * @throws MessagingException
     */
    public MimeMessage createEmail(String to,
                                   String from,
                                   String subject,
                                   String bodyText)
            throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }

    /**
     * Create a message from an email.
     *
     * @param emailContent Email to be set to raw of message
     * @return a message containing a base64url encoded email
     * @throws IOException
     * @throws MessagingException
     */
    public Message createMessageWithEmail(MimeMessage emailContent)
            throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    /**
     * Send an email from the user's mailbox to its recipient.
     *
     * @param service Authorized Gmail API instance.
     * @param userId User's email address. The special value "me"
     * can be used to indicate the authenticated user.
     * @param emailContent Email to be sent.
     * @return The sent message
     * @throws MessagingException
     * @throws IOException
     */
    public Message sendMessage(Gmail service,
                               String userId,
                               MimeMessage emailContent)
            throws MessagingException, IOException {

        Message message = createMessageWithEmail(emailContent);
        try {
            message = service.users().messages().send(userId, message).execute();
            return message;
        }
        catch (UserRecoverableAuthIOException e){
            startActivityForResult(e.getIntent(),REQUEST_AUTHORIZATION);
        }
        return message;

    }
}
