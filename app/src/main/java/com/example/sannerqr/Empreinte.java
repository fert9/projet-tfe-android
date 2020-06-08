package com.example.sannerqr;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.Executor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
public class Empreinte extends AppCompatActivity {

        private TextView textView;
        private ImageView imageView;
        private Button button;
        private FingerprintManager fingerprintManager;
        private FingerprintManager.AuthenticationCallback authenticationCallback;
        private Context context;
        private Executor executor;
        private BiometricPrompt biometricPrompt;
        private BiometricPrompt.PromptInfo promptInfo;
        private static final String KEY_NAME = "aKey";

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            textView = findViewById(R.id.textView);
            imageView = findViewById(R.id.imageView);
            button = findViewById(R.id.button_send);

            executor = ContextCompat.getMainExecutor(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                biometricPrompt = new BiometricPrompt(Empreinte.this,
                        executor, new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        Toast.makeText(getApplicationContext(),
                                "Authentication error: " + errString, Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onAuthenticationSucceeded(
                            @NonNull BiometricPrompt.AuthenticationResult result) {
                        // NullPointerException is unhandled; use Objects.requireNonNull().
                        //Log.d("TAG", " Authentification réussie: "+ result.getCryptoObject().getCipher());

                        Boolean verify = true;
                        if (verify){
                            int data = result.hashCode();
                            Log.d("TAG"," Authentication succeeded hashcode : "+ data);
                        }

//                    try {
//                        byte [] encrypted = result.getCryptoObject().getCipher().doFinal("TATA".getBytes(Charset.defaultCharset()));
//
//                        Log.d("MY_APP_TAG", "Encrypted information: " +
//                                Arrays.toString(encrypted));
//                    } catch (BadPaddingException e) {
//                        e.printStackTrace();
//                    } catch (IllegalBlockSizeException e) {
//                        e.printStackTrace();
//                    }
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Toast.makeText(getApplicationContext(), "Authentication failed",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }

            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle(" ")
                    .setSubtitle(" ")
                    .setDescription(" ")
                    .setNegativeButtonText(" ")
//                .setConfirmationRequired(false)
                    .build();

            // Prompt appearsipher.ENCRYPT_MODE, secretKey);
            //            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            //                biometricPrompt.authenticate(promptInfo,
            //                        n when user clicks "Log in".
            // Consider integrating with the keystore to unlock cryptographic operations,
            // if needed by your app.
            Button biometricLoginButton = findViewById(R.id.button_send);
            biometricLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    biometricPrompt.authenticate(promptInfo);
                    Cipher cipher = null;
                    try {
                        cipher = Empreinte.this.getCipher();
                        SecretKey secretKey = Empreinte.this.getSecretKey();
                        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                        biometricPrompt.authenticate(promptInfo,
                                new BiometricPrompt.CryptoObject(cipher));
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    }
                }
            });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                generateSecretKey(new KeyGenParameterSpec.Builder(
                        KEY_NAME,
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .setUserAuthenticationRequired(true)
                        .setInvalidatedByBiometricEnrollment(true)
                        .build());
            }



        }
        @RequiresApi(api = Build.VERSION_CODES.M)
        private void generateSecretKey(KeyGenParameterSpec keyGenParameterSpec) {
            KeyGenerator keyGenerator = null;
            try {
                keyGenerator = KeyGenerator.getInstance(
                        KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    keyGenerator.init(keyGenParameterSpec);
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }
            }
            keyGenerator.generateKey();
        }

        private SecretKey getSecretKey() {
            KeyStore keyStore = null;
            try {
                keyStore = KeyStore.getInstance("AndroidKeyStore");
            } catch (KeyStoreException e) {
                e.printStackTrace();
            }

            // Before the keystore can be accessed, it must be loaded.
            try {
                keyStore.load(null);
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            try {
                return ((SecretKey)keyStore.getKey(KEY_NAME, null));
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnrecoverableKeyException e) {
                e.printStackTrace();
            }
            return null;}

        private Cipher getCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
            return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        }

        private void encryptSecretInformation() {
            // Exceptions are unhandled for getCipher() and getSecretKey().
            Cipher cipher = null;
            try {
                cipher = getCipher();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            SecretKey secretKey = getSecretKey();
            try {
                // NullPointerException is unhandled; use Objects.requireNonNull().
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                try {
                    byte[] encryptedInfo = cipher.doFinal(
                            "TATA".getBytes(Charset.defaultCharset()));
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
            } catch (InvalidKeyException e) {
                Log.e("MY_APP_TAG", "Key is invalid.");
            }
        }


    }
