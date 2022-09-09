package com.bob.walletdemo.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bob.walletdemo.R;
import com.bob.walletdemo.activity.base.BaseActivity;
import com.bob.walletdemo.util.ETHWalletUtil;
import com.bob.walletdemo.util.Util;

import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;

public class ImportWalletActivity extends BaseActivity {

    EditText editText;
    TextView mnemonicAddressTv;
    TextView privateKeyAddressTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_wallet);

        editText = findViewById(R.id.et);
        mnemonicAddressTv = findViewById(R.id.tv_address);

        Button mnemonicImportBtn = findViewById(R.id.btn_import_mnemonic);
        mnemonicImportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mnemonic = editText.getText().toString();
                importWalletByMnemonic(mnemonic);
            }
        });

        Button privateKeyImportBtn = findViewById(R.id.btn_import_private_key);
        privateKeyImportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String privateKey = editText.getText().toString();
                importWalletByPrivateKey(privateKey);
            }
        });

        Button keystoreImportBtn = findViewById(R.id.btn_import_keystore);
        keystoreImportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filePath = ETHWalletUtil.PATH + File.separator + "UTC--2022-09-09T10-45-12.970000000Z--1b52a7ab3a440ce04cab85a858f878b9b902a1a0.json";
                importWalletByKeyStore(filePath);
            }
        });
    }

    private void importWalletByKeyStore(String filePath) {
//        Credentials credentials = null;
//         WalletFile walletFile = new Gson().fromJson(keystore, WalletFile.class);
//        credentials = Credentials.create(Wallet.decrypt(ETHWalletUtil.PASSWORD, walletFile));
//
//        if (credentials != null) {
//            generateWallet(generateNewWalletName(), pwd, credentials.getEcKeyPair());
//        }
    }

    //助记词导入钱包
    private void importWalletByMnemonic(String mnemonic) {
        Bip39Wallet wallet;
        try {
            wallet = WalletUtils.generateBip39WalletFromMnemonic(ETHWalletUtil.PASSWORD, mnemonic, ETHWalletUtil.PATH);

            String address = ETHWalletUtil.getAddressFromWalletFileName(wallet.getFilename());
            Toast.makeText(ImportWalletActivity.this, "导入成功", Toast.LENGTH_LONG).show();
            mnemonicAddressTv.setText(address);
        } catch (CipherException | IOException e) {
            e.printStackTrace();
        }
    }

    //私钥导入钱包
    private void importWalletByPrivateKey(String privateKey) {
        String hexPrivateKey = Util.bigInteger2Hex(privateKey);
        ECKeyPair ecKeyPair = ECKeyPair.create(Numeric.toBigInt(hexPrivateKey));

        WalletFile wallet;
        try {
            wallet = Wallet.createStandard(ETHWalletUtil.PASSWORD, ecKeyPair);

            Toast.makeText(ImportWalletActivity.this, "导入成功", Toast.LENGTH_LONG).show();
            privateKeyAddressTv.setText(wallet.getAddress());
        } catch (CipherException e) {
            e.printStackTrace();
        }
    }

}