package com.rewardapp.smartreward;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.EthLog.LogObject;
import org.web3j.protocol.ipc.UnixIpcService;
import org.web3j.protocol.parity.Parity;
import org.web3j.tx.ChainId;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;

import com.rewardapp.smartreward.SmartReward.BroadcastRewardEventResponse;

public class RewardWrapper {
    private static final String CONTRACT_ADDRESS = "0x99e917a0339f8ee204375E4489088B62f8b9F579";
    private static final String IPC_PATH = "/home/amitj/rewardapp/ethereum/testgeth.ipc";
    private static final String OWNER_KEY = "/home/amitj/rewardapp/ethereum/testchain/keystore/UTC--2017-07-17T08-30-19.235609423Z--cbaebf0fdb942eeb9b1f3b897c392de1405764d5"; // Full Path to owner key file
    private static final String OWNER_PASS = "1"; // Password for OWNER_KEY
    
    private static final BigInteger GAS_PRICE = BigInteger.valueOf(20_000_000_000L);
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);
    
    private Web3j web3j;
    private Parity parity;
    private Credentials ownerCredentials;
    private SmartReward contract;

    public RewardWrapper() {
        try {
            ownerCredentials = WalletUtils.loadCredentials(OWNER_PASS, OWNER_KEY);
        } catch (IOException | CipherException e) {
            e.printStackTrace();
        }
        web3j = Web3j.build(new UnixIpcService(IPC_PATH));
        parity = Parity.build(new UnixIpcService(IPC_PATH));
        contract = SmartReward.load(CONTRACT_ADDRESS, web3j, ownerCredentials, GAS_PRICE, GAS_LIMIT);
    }

    public String giveReward(String userAddress, int rewardValue, String msg) {
        try {
            Address to = new Address(userAddress);
            Uint256 value = new Uint256(BigInteger.valueOf(rewardValue));
            Utf8String rewardReasonMsg = new Utf8String(msg);
            TransactionReceipt transactionReceipt = contract.giveReward(to, value, rewardReasonMsg).get();
            return transactionReceipt.getTransactionHash();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;

    }

    public String showMyReward(String userAddress) {
        Address address = new Address(userAddress);
        try {
            Uint256 rew = contract.showMyReward(address).get();
            return rew.getValue().toString();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String sendReward(String from, String to, int rewardValue, String msg) {
        Address sender = new Address(from);
        Address receiver = new Address(to);
        Uint256 value = new Uint256(BigInteger.valueOf(rewardValue));
        Utf8String senderMsg = new Utf8String(msg);
        try {
            TransactionReceipt transactionReceipt = contract.sendReward(sender, receiver, value, senderMsg).get();
            return transactionReceipt.getTransactionHash();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;    
    }
    
    public List<String> listAccounts() {
        try {
            return parity.personalListAccounts().sendAsync().get().getAccountIds();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public boolean checkAccount(String address) {
        List<String> accountList = listAccounts();
        return accountList.contains(address.toLowerCase());
    }

    public String spendReward(String userAddress, int rewardValue, String msg) {
        Address address = new Address(userAddress);
        Uint256 value = new Uint256(BigInteger.valueOf(rewardValue));
        Utf8String spentOnMsg = new Utf8String(msg);
        TransactionReceipt transactionReceipt;
        try {
            transactionReceipt = contract.spendReward(address, value, spentOnMsg).get();
            return transactionReceipt.getTransactionHash();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;

    }

    public String showAvailReward() {
        try {
            Uint256 reward = contract.showAvailReward().get();
            return reward.getValue().toString();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String setAvailRewardPoints(int rewardValue) {
        Uint256 value = new Uint256(BigInteger.valueOf(rewardValue));
        try {
            TransactionReceipt transactionReceipt = contract.setAvailRewardPoints(value).get();
            return transactionReceipt.getTransactionHash();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<BroadcastRewardEventResponse> getAllBroadcastRewardEvent() {
        Event event = new Event("BroadcastReward", Collections.emptyList(), Arrays.asList(new TypeReference<Address>() {
        }, new TypeReference<Address>() {
        }, new TypeReference<Uint256>() {
        }, new TypeReference<Utf8String>() {
        }, new TypeReference<Utf8String>() {
        }));

        String encodedEventSignature = EventEncoder.encode(event);
        List<EthLog.LogResult> filterLogs = createFilterForEvent(encodedEventSignature, CONTRACT_ADDRESS);
        List<BroadcastRewardEventResponse> returnArray = new ArrayList<>();

        for (int i = 0; i < filterLogs.size(); i++) {
            BroadcastRewardEventResponse broadcastReward = new BroadcastRewardEventResponse();
            LogObject log = (LogObject) filterLogs.get(i);
            List<Type> nonIndexedValues = FunctionReturnDecoder.decode(log.getData(), event.getNonIndexedParameters());
            broadcastReward.sender = nonIndexedValues.get(0).toString();
            broadcastReward.receiver = nonIndexedValues.get(1).toString();
            broadcastReward.value = nonIndexedValues.get(2).getValue().toString();
            broadcastReward.event = nonIndexedValues.get(3).getValue().toString();
            broadcastReward.reason = nonIndexedValues.get(4).getValue().toString();
            returnArray.add(broadcastReward);
        }
        return returnArray;
    }
    
    private List<EthLog.LogResult> createFilterForEvent(String encodedEventSignature, String contractAddress) {
        EthFilter ethFilter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST,
                contractAddress);

        ethFilter.addSingleTopic(encodedEventSignature);

        try {
            EthLog ethLog = parity.ethGetLogs(ethFilter).send();
            return ethLog.getLogs();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
    
    public List<BroadcastRewardEventResponse> getBroadcastRewardEvent(String transactionHash) {
        EthGetTransactionReceipt transactionReceipt;
        try {
            transactionReceipt = parity.ethGetTransactionReceipt(transactionHash).sendAsync().get();
            return contract.getBroadcastRewardEvents(transactionReceipt.getTransactionReceipt().get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}