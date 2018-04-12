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
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
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
public final class SmartReward extends Contract {
    private static final String BINARY = "6060604052341561000c57fe5b6040516020806108cd83398101604052515b60008054600160a060020a03191633600160a060020a0316908117825581526001602052604090208190555b505b6108728061005b6000396000f300606060405236156100805763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166334c564cb81146100965780633d54ed2d146100fb578063441fcf24146101295780634b8fdc69146101965780638cb768ed146101ab5780638da5cb5b14610210578063cff9ed2d1461023c575b341561008857fe5b6100945b60006000fd5b565b005b341561009e57fe5b604080516020600460443581810135601f8101849004840285018401909552848452610094948235600160a060020a031694602480359560649492939190920191819084018382808284375094965061025e95505050505050565b005b341561010357fe5b610117600160a060020a03600435166103ed565b60408051918252519081900360200190f35b341561013157fe5b604080516020600460643581810135601f8101849004840285018401909552848452610094948235600160a060020a0390811695602480359092169560443595946084949293019190819084018382808284375094965061040c95505050505050565b005b341561019e57fe5b61009460043561059f565b005b34156101b357fe5b604080516020600460443581810135601f8101849004840285018401909552848452610094948235600160a060020a031694602480359560649492939190920191819084018382808284375094965061069e95505050505050565b005b341561021857fe5b6102206107fa565b60408051600160a060020a039092168252519081900360200190f35b341561024457fe5b610117610809565b60408051918252519081900360200190f35b60005433600160a060020a0390811691161461027a5760006000fd5b600160a060020a033316600090815260016020526040902054829010156102a15760006000fd5b600160a060020a03831660009081526001602052604090205482810110156102c95760006000fd5b600160a060020a03338181166000818152600160209081526040808320805489900390559488168083529185902080548801905584519283528281019190915292810185905260a0606082018181526014918301919091527f476f742072657761726420706f696e742873292e00000000000000000000000060c083015260e060808301818152865191840191909152855160008051602061082783398151915295899489948994919390926101008501919086019080838382156103a9575b8051825260208311156103a957601f199092019160209182019101610389565b505050905090810190601f1680156103d55780820380516001836020036101000a031916815260200191505b50965050505050505060405180910390a15b5b505050565b600160a060020a0381166000908152600160205260409020545b919050565b60005433600160a060020a039081169116146104285760006000fd5b600160a060020a0384166000908152600160205260409020548290101561044f5760006000fd5b600160a060020a03831660009081526001602052604090205482810110156104775760006000fd5b600160a060020a038085166000818152600160209081526040808320805488900390559387168083529184902080548701905583519283528281019190915291810184905260a0606082018181526015918301919091527f53656e742072657761726420706f696e742873292e000000000000000000000060c083015260e060808301818152855191840191909152845160008051602061082783398151915294899489948994899492939192909161010085019190860190808383821561055a575b80518252602083111561055a57601f19909201916020918201910161053a565b505050905090810190601f1680156105865780820380516001836020036101000a031916815260200191505b50965050505050505060405180910390a15b5b50505050565b6000805433600160a060020a039081169116146105bc5760006000fd5b600160a060020a03331660009081526001602052604090205482810110156105e45760006000fd5b600160a060020a03338116600081815260016020908152604080832087905580519384529385169083015281830185905260a0606083018190526023908301527f536574206e6577206176616c696c61626c652072657761726420706f696e742860c08301527f73292e000000000000000000000000000000000000000000000000000000000060e0830152610100608083018190528201529051600080516020610827833981519152918190036101200190a15b5b5050565b6000805433600160a060020a039081169116146106bb5760006000fd5b600160a060020a038416600090815260016020526040902054839010156106e25760006000fd5b600160a060020a0380851660008181526001602090815260409182902080548890039055815192835292841682840152810185905260a0606082018181526016918301919091527f5370656e742072657761726420706f696e742873292e0000000000000000000060c083015260e060808301818152865191840191909152855160008051602061082783398151915294899487948a948a9492939192909161010085019190860190808383821561055a575b80518252602083111561055a57601f19909201916020918201910161053a565b505050905090810190601f1680156105865780820380516001836020036101000a031916815260200191505b50965050505050505060405180910390a15b5b50505050565b600054600160a060020a031681565b600160a060020a0333166000908152600160205260409020545b9056002eb9fd6adabf11fa42d72a5d5c0f7e37bd14fa2bf9fd409ed2320aced2776834a165627a7a72305820eccf46df101974ab4933875473b1c375471c85a9656278b091d3da90d7e8f7230029";

    private SmartReward(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private SmartReward(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<BroadcastRewardEventResponse> getBroadcastRewardEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("BroadcastReward", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<BroadcastRewardEventResponse> responses = new ArrayList<BroadcastRewardEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            BroadcastRewardEventResponse typedResponse = new BroadcastRewardEventResponse();
            typedResponse.sender = eventValues.getNonIndexedValues().get(0).toString();
            typedResponse.receiver = eventValues.getNonIndexedValues().get(1).toString();
            typedResponse.value = eventValues.getNonIndexedValues().get(2).getValue().toString();
            typedResponse.event = eventValues.getNonIndexedValues().get(3).getValue().toString();
            typedResponse.reason = eventValues.getNonIndexedValues().get(4).getValue().toString();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<BroadcastRewardEventResponse> broadcastRewardEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("BroadcastReward", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
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
                typedResponse.event = eventValues.getNonIndexedValues().get(3).getValue().toString();
                typedResponse.reason = eventValues.getNonIndexedValues().get(4).getValue().toString();
                return typedResponse;
            }
        });
    }

    public Future<TransactionReceipt> giveReward(Address to, Uint256 value, Utf8String rewardReasonMsg) {
        Function function = new Function("giveReward", Arrays.<Type>asList(to, value, rewardReasonMsg), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<Uint256> showMyReward(Address address) {
        Function function = new Function("showMyReward", 
                Arrays.<Type>asList(address), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> sendReward(Address sender, Address receiver, Uint256 value, Utf8String senderMsg) {
        Function function = new Function("sendReward", Arrays.<Type>asList(sender, receiver, value, senderMsg), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<TransactionReceipt> setAvailRewardPoints(Uint256 value) {
        Function function = new Function("setAvailRewardPoints", Arrays.<Type>asList(value), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<TransactionReceipt> spendReward(Address spender, Uint256 value, Utf8String spentOnMsg) {
        Function function = new Function("spendReward", Arrays.<Type>asList(spender, value, spentOnMsg), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<Address> owner() {
        Function function = new Function("owner", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<Uint256> showAvailReward() {
        Function function = new Function("showAvailReward", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public static Future<SmartReward> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue, Uint256 initValue) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(initValue));
        return deployAsync(SmartReward.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor, initialWeiValue);
    }

    public static Future<SmartReward> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue, Uint256 initValue) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(initValue));
        return deployAsync(SmartReward.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, initialWeiValue);
    }

    public static SmartReward load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SmartReward(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static SmartReward load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SmartReward(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class BroadcastRewardEventResponse {
        public String sender;

        public String receiver;

        public String value;

        public String event;

        public String reason;
    }
}
