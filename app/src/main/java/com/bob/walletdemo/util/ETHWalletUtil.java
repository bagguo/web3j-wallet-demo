package com.bob.walletdemo.util;

import static org.web3j.crypto.Hash.sha256;

import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;


import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class ETHWalletUtil {
    private static final String TAG = ETHWalletUtil.class.getSimpleName();

    public static File PATH = new File(Environment.getExternalStorageDirectory().getPath());
    public static final String PASSWORD = "111111";


    public static String getMnemonic(Bip39Wallet wallet) {
        if (wallet == null) return "";
        String mnemonic = wallet.getMnemonic();
        Log.d(TAG, "getMnemonic: ====" + mnemonic);
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

    /**
     * json导入钱包
     */
    public static String loadJsonCredentials(String filePath) {
        Credentials credentials = null;
        try {
            String json = FileUtil.readJsonFile(filePath);
            credentials = WalletUtils.loadJsonCredentials(PASSWORD, json);
        } catch (IOException | CipherException e) {
            e.printStackTrace();
        }

        ECKeyPair ecKeyPair = null;
        if (credentials != null) {
            ecKeyPair = credentials.getEcKeyPair();
        }

        BigInteger privateKey = null;
        BigInteger publicKey = null;
        if (ecKeyPair != null) {
            privateKey = ecKeyPair.getPrivateKey();
            publicKey = ecKeyPair.getPublicKey();

        }

        assert credentials != null;

        String address = credentials.getAddress();
        Log.d(TAG, "importWalletByKeyStore: ====address:" + address
                + " privateKey:" + privateKey
                + " publicKey:" + publicKey);
        return address;
    }

    @NonNull
    public static String getAddressFromWalletFileName(String filename) {
        if (filename == null) return "";

        int beginIndex = filename.lastIndexOf("--") + 2;
        int endIndex = filename.lastIndexOf(".");
        return filename.substring(beginIndex, endIndex);
    }

    /**
     * 转换成符合 decimal 的数值
     * @param decimal
     * @param str
     * @return
     */
    public static String toDecimal(int decimal,BigInteger integer){
//		String substring = str.substring(str.length() - decimal);
        StringBuffer sbf = new StringBuffer("1");
        for (int i = 0; i < decimal; i++) {
            sbf.append("0");
        }
        String balance = new BigDecimal(integer).divide(new BigDecimal(sbf.toString()), 18, BigDecimal.ROUND_DOWN).toPlainString();
        return balance;
    }

}
