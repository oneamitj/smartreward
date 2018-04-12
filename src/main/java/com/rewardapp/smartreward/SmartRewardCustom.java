package com.rewardapp.smartreward;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.EthLog.LogObject;

public class SmartRewardCustom extends Helper {
	protected static final String CONTRACT_ADDRESS = "0x99e917a0339f8ee204375E4489088B62f8b9F579";
	protected static final String OWNER = "0xCBaEbf0FDb942eeB9B1F3B897c392de1405764d5";
	protected static final String PASSWORD = "1";

	public SmartRewardCustom() {
		setUp();
		unlockAccount(OWNER, PASSWORD);
	}

	protected String showAvailReward() {
		Function function = new Function("showAvailReward", Arrays.<Type>asList(),
				Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
				}));

		return callConstantFunction(function, CONTRACT_ADDRESS, OWNER);
	}

	protected String showMyReward(String userAddress) {
		Address address = new Address(userAddress);

		Function function = new Function("showMyReward", Arrays.<Type>asList(address),
				Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
				}));

		return callConstantFunction(function, CONTRACT_ADDRESS, OWNER);
	}

	protected String giveReward(String userAddress, int rewardValue, String msg) {
		Address to = new Address(userAddress);
		Uint256 value = new Uint256(BigInteger.valueOf(rewardValue));
		Utf8String rewardReasonMsg = new Utf8String(msg);
		Function function = new Function("giveReward", Arrays.<Type>asList(to, value, rewardReasonMsg),
				Collections.<TypeReference<?>>emptyList());

		return callTransactionFunction(function, CONTRACT_ADDRESS, OWNER);
	}

	protected String spendReward(String userAddress, int rewardValue, String msg) {
		Address spender = new Address(userAddress);
		Uint256 value = new Uint256(BigInteger.valueOf(rewardValue));
		Utf8String spentOnMsg = new Utf8String(msg);
		Function function = new Function("spendReward", Arrays.<Type>asList(spender, value, spentOnMsg),
				Collections.<TypeReference<?>>emptyList());
		return callTransactionFunction(function, CONTRACT_ADDRESS, OWNER);
	}

	protected String sendReward(String from, String to, int rewardValue, String msg) {
		Address sender = new Address(from);
		Address receiver = new Address(to);
		Uint256 value = new Uint256(BigInteger.valueOf(rewardValue));
		Utf8String senderMsg = new Utf8String(msg);
		
		Function function = new Function("sendReward", Arrays.<Type>asList(sender, receiver, value, senderMsg),
				Collections.<TypeReference<?>>emptyList());
		return callTransactionFunction(function, CONTRACT_ADDRESS, OWNER);
	}

	protected String setAvailRewardPoints(int rewardValue) {
		Uint256 value = new Uint256(BigInteger.valueOf(rewardValue));
		Function function = new Function("setAvailRewardPoints", Arrays.<Type>asList(value),
				Collections.<TypeReference<?>>emptyList());
		return callTransactionFunction(function, CONTRACT_ADDRESS, OWNER);
	}

	public List<BroadcastRewardEvent> getAllBroadcastRewardEvent() {
		Event event = new Event("BroadcastReward", Collections.emptyList(), Arrays.asList(new TypeReference<Address>() {
		}, new TypeReference<Address>() {
		}, new TypeReference<Uint256>() {
		}, new TypeReference<Utf8String>() {
		}, new TypeReference<Utf8String>() {
		}));

		String encodedEventSignature = EventEncoder.encode(event);
		List<EthLog.LogResult> filterLogs = createFilterForEvent(encodedEventSignature, CONTRACT_ADDRESS);
		List<BroadcastRewardEvent> returnArray = new ArrayList<>();

		for (int i = 0; i < filterLogs.size(); i++) {
			BroadcastRewardEvent broadcastReward = new BroadcastRewardEvent();
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

	public List<BroadcastRewardEvent> getBroadcastRewardEvent(TransactionReceipt transactionReceipt) {
		Event event = new Event("BroadcastReward", Collections.emptyList(), Arrays.asList(new TypeReference<Address>() {
		}, new TypeReference<Address>() {
		}, new TypeReference<Uint256>() {
		}, new TypeReference<Utf8String>() {
		}, new TypeReference<Utf8String>() {
		}));

		List<Log> logs = transactionReceipt.getLogs();
		List<EventValues> valueList = new ArrayList<>();

		for (Log log : logs) {
			List<String> topics = log.getTopics();
			String encodedEventSignature = EventEncoder.encode(event);
			if (!topics.get(0).equals(encodedEventSignature)) {
				return null;
			}

			List<Type> indexedValues = new ArrayList<>();
			List<Type> nonIndexedValues = FunctionReturnDecoder.decode(log.getData(), event.getNonIndexedParameters());
			if (new EventValues(indexedValues, nonIndexedValues) != null) {
				valueList.add(new EventValues(indexedValues, nonIndexedValues));
			}
		}

		ArrayList<BroadcastRewardEvent> responses = new ArrayList<BroadcastRewardEvent>(valueList.size());
		for (EventValues eventValues : valueList) {
			BroadcastRewardEvent typedResponse = new BroadcastRewardEvent();
			typedResponse.sender = eventValues.getNonIndexedValues().get(0).toString();
			typedResponse.receiver = eventValues.getNonIndexedValues().get(1).toString();
			typedResponse.value = eventValues.getNonIndexedValues().get(2).getValue().toString();
			typedResponse.event = eventValues.getNonIndexedValues().get(3).getValue().toString();
			typedResponse.reason = eventValues.getNonIndexedValues().get(4).getValue().toString();
			responses.add(typedResponse);
		}
		return responses;
	}

	public static class BroadcastRewardEvent {
		public String sender;

		public String receiver;

		public String value;

		public String event;

		public String reason;
	}
}
