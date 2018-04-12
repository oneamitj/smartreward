package com.rewardapp.smartreward;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * Auto generated code.<br>
 * <strong>Do not modify!</strong><br>
 * Please use {@link org.web3j.codegen.SolidityFunctionWrapperGenerator} to update.
 *
 * <p>Generated with web3j version 2.2.1.
 */
public final class SmartMultiReward extends Contract {
    private static final String BINARY = "6060604052341561000c57fe5b604051604080610c828339810160405280516020909101515b60008054600160a060020a03191633600160a060020a0316908117825580825260016020818152604080852086865282528085208790559284526002905291208054909181016100758382610090565b916000526020600020900160005b50829055505b50506100db565b8154818355818115116100b4576000838152602090206100b49181019083016100ba565b5b505050565b6100d891905b808211156100d457600081556001016100c0565b5090565b90565b610b98806100ea6000396000f3006060604052361561008b5763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416633d3442d081146100a15780636d0346751461011857806372588b201461017f5780638cb1f5af146101b05780638da5cb5b146101c8578063cb3fa928146101f4578063cea3bb0614610261578063f8f4b9d5146102c8575b341561009357fe5b61009f5b60006000fd5b565b005b34156100a957fe5b6100bd600160a060020a03600435166102ed565b6040805160208082528351818301528351919283929083019185810191028083838215610105575b80518252602083111561010557601f1990920191602091820191016100e5565b5050509050019250505060405180910390f35b341561012057fe5b604080516020600460643581810135601f810184900484028501840190955284845261009f948235600160a060020a0316946024803595604435959460849492019190819084018382808284375094965061036195505050505050565b005b341561018757fe5b61019e600160a060020a03600435166024356104d6565b60408051918252519081900360200190f35b34156101b857fe5b61009f600435602435610501565b005b34156101d057fe5b6101d8610657565b60408051600160a060020a039092168252519081900360200190f35b34156101fc57fe5b604080516020601f60843560048181013592830184900484028501840190955281845261009f94600160a060020a0381358116956024803590921695604435956064359560a4940191819084018382808284375094965061066695505050505050565b005b341561026957fe5b604080516020600460643581810135601f810184900484028501840190955284845261009f948235600160a060020a0316946024803595604435959460849492019190819084018382808284375094965061082595505050505050565b005b34156102d057fe5b61019e600435610ac5565b60408051918252519081900360200190f35b6102f5610aef565b600160a060020a0382166000908152600260209081526040918290208054835181840281018401909452808452909183018282801561035457602002820191906000526020600020905b8154815260019091019060200180831161033f575b505050505090505b919050565b6000805433600160a060020a0390811691161461037e5760006000fd5b600160a060020a0385166000908152600160209081526040808320868452909152902054849010156103b05760006000fd5b600160a060020a0380861660008181526001602090815260408083208884528252918290208054899003905581519283529284168284015281018690526060810185905260c0608082018181526016918301919091527f5370656e742072657761726420706f696e742873292e0000000000000000000060e083015261010060a083018181528651918401919091528551600080516020610b4d833981519152948a9487948b948b948b9461012085019190860190808383821561048f575b80518252602083111561048f57601f19909201916020918201910161046f565b505050905090810190601f1680156104bb5780820380516001836020036101000a031916815260200191505b5097505050505050505060405180910390a15b5b5050505050565b600160a060020a03821660009081526001602090815260408083208484529091529020545b92915050565b6000805433600160a060020a0390811691161461051e5760006000fd5b33600160a060020a0316600090815260016020908152604080832085845290915290205483810110156105515760006000fd5b600160a060020a03331660008181526001602081815260408084208785528252808420889055938352600290529190208054909181016105918382610b01565b916000526020600020900160005b508390555060408051600160a060020a033381168252831660208201528082018590526060810184905260c0608082018190526023908201527f536574206e6577206176616c696c61626c652072657761726420706f696e742860e08201527f73292e000000000000000000000000000000000000000000000000000000000061010082015261012060a082018190526000908201529051600080516020610b4d833981519152918190036101400190a15b5b505050565b600054600160a060020a031681565b60005433600160a060020a039081169116146106825760006000fd5b600160a060020a0385166000908152600160209081526040808320858452909152902054839010156106b45760006000fd5b600160a060020a038416600090815260016020908152604080832085845290915290205483810110156106e75760006000fd5b600160a060020a038086166000818152600160208181526040808420888552825280842080548a900390559489168084529181528483208784528152918490208054880190558351928352828201529181018590526060810184905260c0608082018181526015918301919091527f53656e742072657761726420706f696e742873292e000000000000000000000060e083015261010060a083018181528551918401919091528451600080516020610b4d833981519152948a948a948a948a948a9461012085019190860190808383821561048f575b80518252602083111561048f57601f19909201916020918201910161046f565b505050905090810190601f1680156104bb5780820380516001836020036101000a031916815260200191505b5097505050505050505060405180910390a15b5b5050505050565b60008054819033600160a060020a039081169116146108445760006000fd5b600160a060020a0333166000908152600160209081526040808320878452909152812054909250859010156108795760006000fd5b600160a060020a038616600090815260016020908152604080832087845290915290205485810110156108ac5760006000fd5b50600160a060020a033381166000908152600160208181526040808420888552825280842080548a90039055938916835290815282822086835290529081208054860190555b600160a060020a03861660009081526002602052604090205481101561095f57600160a060020a038616600090815260026020526040902080548290811061093657fe5b906000526020600020900160005b5054841415610956576001915061095f565b5b6001016108f2565b8115156109a257600160a060020a038616600090815260026020526040902080546001810161098e8382610b01565b916000526020600020900160005b50859055505b600080516020610b4d83398151915233878787876040518086600160a060020a0316600160a060020a0316815260200185600160a060020a0316600160a060020a0316815260200184815260200183600019166000191681526020018060200180602001838103835260148152602001807f476f742072657761726420706f696e742873292e000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360008314610a7d575b805182526020831115610a7d57601f199092019160209182019101610a5d565b505050905090810190601f168015610aa95780820380516001836020036101000a031916815260200191505b5097505050505050505060405180910390a15b5b505050505050565b600160a060020a03331660009081526001602090815260408083208484529091529020545b919050565b60408051602081019091526000815290565b81548183558181151161065157600083815260209020610651918101908301610b2b565b5b505050565b610b4991905b80821115610b455760008155600101610b31565b5090565b905600657a08aafed0d3c7f3e970495fba3ac1822ca659095104ee0346a36f566ea3b4a165627a7a72305820f1c38723d7c1ed0086e503281e69030f3e74aed64a59fa1c0b6ee6d484f3ed5d0029";

    private SmartMultiReward(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private SmartMultiReward(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<BroadcastRewardEventResponse> getBroadcastRewardEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("BroadcastReward", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<BroadcastRewardEventResponse> responses = new ArrayList<BroadcastRewardEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            BroadcastRewardEventResponse typedResponse = new BroadcastRewardEventResponse();
            typedResponse.sender = eventValues.getNonIndexedValues().get(0).toString();
            typedResponse.receiver = eventValues.getNonIndexedValues().get(1).toString();
            typedResponse.value = eventValues.getNonIndexedValues().get(2).getValue().toString();
            typedResponse.rewardType = eventValues.getNonIndexedValues().get(3).getValue().toString();
            typedResponse.rewardEvent = eventValues.getNonIndexedValues().get(4).getValue().toString();
            typedResponse.reason = eventValues.getNonIndexedValues().get(5).getValue().toString();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<BroadcastRewardEventResponse> broadcastRewardEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("BroadcastReward", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, BroadcastRewardEventResponse>() {
            @Override
            public BroadcastRewardEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                BroadcastRewardEventResponse typedResponse = new BroadcastRewardEventResponse();
                typedResponse.sender = eventValues.getNonIndexedValues().get(0).toString();
                typedResponse.receiver = eventValues.getNonIndexedValues().get(1).toString();
                typedResponse.value = eventValues.getNonIndexedValues().get(2).getValue().toString();
                typedResponse.rewardType = eventValues.getNonIndexedValues().get(3).getValue().toString();
                typedResponse.rewardEvent = eventValues.getNonIndexedValues().get(4).getValue().toString();
                typedResponse.reason = eventValues.getNonIndexedValues().get(5).getValue().toString();
                return typedResponse;
            }
        });
    }

    public Future<DynamicArray<Bytes32>> showRewardTypes(Address user) {
        Function function = new Function("showRewardTypes", 
                Arrays.<Type>asList(user), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Bytes32>>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> spendReward(Address spender, Uint256 value, Bytes32 rewardType, Utf8String spentOnMsg) {
        Function function = new Function("spendReward", Arrays.<Type>asList(spender, value, rewardType, spentOnMsg), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<Uint256> showMyReward(Address user, Bytes32 rewardType) {
        Function function = new Function("showMyReward", 
                Arrays.<Type>asList(user, rewardType), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> setAvailRewardPoints(Uint256 value, Bytes32 rewardType) {
        Function function = new Function("setAvailRewardPoints", Arrays.<Type>asList(value, rewardType), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<Address> owner() {
        Function function = new Function("owner", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> sendReward(Address sender, Address receiver, Uint256 value, Bytes32 rewardType, Utf8String senderMsg) {
        Function function = new Function("sendReward", Arrays.<Type>asList(sender, receiver, value, rewardType, senderMsg), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<TransactionReceipt> giveReward(Address to, Uint256 value, Bytes32 rewardType, Utf8String rewardReasonMsg) {
        Function function = new Function("giveReward", Arrays.<Type>asList(to, value, rewardType, rewardReasonMsg), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<Uint256> showAvailReward(Bytes32 rewardType) {
        Function function = new Function("showAvailReward", 
                Arrays.<Type>asList(rewardType), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public static Future<SmartMultiReward> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue, Uint256 initValue, Bytes32 rewardType) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(initValue, rewardType));
        return deployAsync(SmartMultiReward.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor, initialWeiValue);
    }

    public static Future<SmartMultiReward> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue, Uint256 initValue, Bytes32 rewardType) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(initValue, rewardType));
        return deployAsync(SmartMultiReward.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, initialWeiValue);
    }

    public static SmartMultiReward load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SmartMultiReward(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static SmartMultiReward load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SmartMultiReward(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class BroadcastRewardEventResponse {
        public String sender;

        public String receiver;

        public String value;

        public String rewardType;

        public String rewardEvent;

        public String reason;
    }
}
