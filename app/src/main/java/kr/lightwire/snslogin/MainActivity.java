package kr.lightwire.snslogin;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getHashKey();

        CallbackManager callbackManager = CallbackManager.Factory.create();


        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // App code
                    Toast.makeText(getBaseContext(), getString(R.string.msg_facebook_login), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel() {
                    // App code
                }

                @Override
                public void onError(@NonNull FacebookException exception) {
                    // App code
                }
        });


        LoginButton facebookLogin = findViewById(R.id.btnLoginFacebook);
        facebookLogin.setPermissions(Collections.singletonList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        facebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(@NonNull FacebookException exception) {
                // App code
            }
        });
    }

    private void getHashKey()
    {
        PackageInfo packageInfo = null;
        try
        {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNING_CERTIFICATES);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : Objects.requireNonNull(packageInfo).signingInfo.getApkContentsSigners())
        {
            try
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
            catch (NoSuchAlgorithmException e)
            {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }
}