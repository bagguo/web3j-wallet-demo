package com.bob.walletdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bob.walletdemo.App;
import com.bob.walletdemo.R;
import com.bob.walletdemo.activity.base.BaseActivity;
import com.bob.walletdemo.util.ETHWalletUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.ObjectMapperFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    Bip39Wallet mWallet = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        App.setupBouncyCastle();

        findViewById(R.id.btn_create_bip39_wallet).setOnClickListener(view ->
                generateBip39Wallets()
        );

        TextView mnemonicTv = findViewById(R.id.tv_show_mnemonic);
        TextView privateKeyTv = findViewById(R.id.tv_show_private_key);

        findViewById(R.id.btn_get_mnemonic).setOnClickListener(view ->
                mnemonicTv.setText(ETHWalletUtil.getMnemonic(mWallet))
        );

        findViewById(R.id.btn_get_private_key).setOnClickListener(view ->
                privateKeyTv.setText(ETHWalletUtil.getPrivateKey(mWallet))
        );

        findViewById(R.id.btn_import_wallet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ImportWalletActivity.class);
                startActivity(intent);
            }
        });
    }

    private static ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();


    public void generateBip39Wallets() {

        try {
            mWallet = WalletUtils.generateBip39Wallet(ETHWalletUtil.PASSWORD, ETHWalletUtil.PATH);
        } catch (CipherException | IOException e) {
            e.printStackTrace();
        }
        if (mWallet == null) return;

        String filename = mWallet.getFilename();
        File file = new File(ETHWalletUtil.PATH + File.separator + filename);

        WalletFile walletFile = null;
        try {
            walletFile = objectMapper.readValue(file, WalletFile.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "generateBip39Wallets: ====address:" + walletFile.getAddress()
                + " getMnemonic:" + mWallet.getMnemonic()
                + " wallet:" + mWallet.toString()
                + " walletFile:" + walletFile.toString());
    }
}