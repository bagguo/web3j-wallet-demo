package com.bob.walletdemo.util;

import static org.web3j.crypto.Hash.sha256;

import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.protocol.ObjectMapperFactory;

import java.io.File;

public class ETHWalletUtil {
    private static final String TAG = ETHWalletUtil.class.getSimpleName();

    public static File PATH = new File(Environment.getExternalStorageDirectory().getPath());
    public static final String PASSWORD = "111111";

    private static ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

    public static String getMnemonic(Bip39Wallet wallet) {
        if (wallet == null) return "";
        String mnemonic = wallet.getMnemonic();
        Log.d(TAG, "getMnemonic: ====" +mnemonic);
        return mnemonic;
    }

    public static String getPrivateKey(Bip39Wallet wallet) {
        if (wallet == null) return "";

        byte[] seed = MnemonicUtils.generateSeed(wallet.getMnemonic(), ETHWalletUtil.PASSWORD);
        Credentials credentials = Credentials.create(ECKeyPair.create(sha256(seed)));
        String privateKey = credentials.getEcKeyPair().getPrivateKey().toString();
        String publicKey = credentials.getEcKeyPair().getPublicKey().toString();
        Log.d(TAG, "getPrivateKey: ====:privateKey:" + privateKey + " publicKey:" + publicKey);
        return privateKey;
    }

    @NonNull
    public static String getAddressFromWalletFileName(String filename) {
        if (filename == null) return "";

        int beginIndex = filename.lastIndexOf("--") + 2;
        int endIndex = filename.lastIndexOf(".");
        return filename.substring(beginIndex, endIndex);
    }
}
