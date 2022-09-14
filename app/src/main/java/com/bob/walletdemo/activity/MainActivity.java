package com.bob.walletdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bob.walletdemo.App;
import com.bob.walletdemo.R;
import com.bob.walletdemo.activity.base.BaseActivity;
import com.bob.walletdemo.util.ETHWalletUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
//import org.hyperledger.besu.ethereum.vm.OperationTracer;
import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
//import org.web3j.evm.Configuration;
//import org.web3j.evm.ConsoleDebugTracer;
//import org.web3j.evm.EmbeddedWeb3jService;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

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

        TextView balanceTv = findViewById(R.id.tv_balance);
        findViewById(R.id.btn_get_balance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = connectWallet();
//                BigInteger balance = getAccountBalance(address);
//                balanceTv.setText((CharSequence) balance);
            }
        });

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

    Web3j web3j;

    public String connectWallet() {
        web3j = Web3j.build(new HttpService("http://192.168.132.67:8545"));

        Web3ClientVersion web3ClientVersion = null;
        try {
            web3ClientVersion = web3j.web3ClientVersion().sendAsync().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        if (web3ClientVersion != null) {
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            Log.d(TAG, "connectWallet: ====clientVersion:" + clientVersion);
        }


        Bip39Wallet wallet;
        String address = "";
        try {
            String mnemonic = "wood table canoe submit fold page dress auto tell biology appear recipe";
            wallet = WalletUtils.generateBip39WalletFromMnemonic(ETHWalletUtil.PASSWORD, mnemonic, ETHWalletUtil.PATH);

            address = ETHWalletUtil.getAddressFromWalletFileName(wallet.getFilename());
        } catch (CipherException | IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "connectNode: ====address:" + address);
        return address;
    }

    //获得某个账户余额，大整数类型
    public BigInteger getAccountBalance(String contractAddress) {
        EthGetBalance result = new EthGetBalance();
        try {
            web3j.ethGetBalance(contractAddress,
                            DefaultBlockParameter.valueOf("latest"))
                    .sendAsync()
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return null;
        return result.getBalance();  //报错：org.web3j.exceptions.MessageDecodingException: Value must be in format 0x[1-9]+[0-9]* or 0x0
    }

}