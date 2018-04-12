package com.rewardapp.smartreward;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.ipc.UnixIpcService;
import org.web3j.protocol.parity.Parity;
import org.web3j.protocol.parity.methods.response.NewAccountIdentifier;
import org.web3j.protocol.parity.methods.response.PersonalUnlockAccount;

public class Helper {
	private static final BigInteger GAS_PRICE = BigInteger.valueOf(20_000_000_000L);
	static final BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);

	private static final BigInteger ACCOUNT_UNLOCK_DURATION = BigInteger.valueOf(30);

	private static final int SLEEP_DURATION = 5000;
	private static final int ATTEMPTS = 3;

	Parity parity;
	Web3j web3j;

	public Helper() {
		// HTTP Logging
		// System.setProperty(
		// "org.apache.commons.logging.Log","org.apache.commons.logging.impl.SimpleLog");
		// System.setProperty("org.apache.commons.logging.simplelog.showdatetime",
		// "true");
		// System.setProperty(
		// "org.apache.commons.logging.simplelog.log.org.apache.http.wire",
		// "DEBUG");

		// IPC Logging
		// System.setProperty(
		// "org.apache.commons.logging.simplelog.log.org.web3j.protocol.ipc",
		// "DEBUG");
	}

	public void setUp() {
		 this.parity = Parity.build(new HttpService());
		 this.web3j = Web3j.build(new HttpService());
		// this.parity = Parity.build(new
		// UnixIpcService("/Volumes/HDD/rewardapp/ethereum/testgeth.ipc"));
		// this.web3j = Web3j.build(new
		// UnixIpcService("/Volumes/HDD/rewardapp/ethereum/testgeth.ipc"));
	}

	boolean unlockAccount(String address, String password) {
		PersonalUnlockAccount personalUnlockAccount;
		try {
			personalUnlockAccount = parity.personalUnlockAccount(address, password, ACCOUNT_UNLOCK_DURATION).sendAsync()
					.get();
			return personalUnlockAccount.accountUnlocked();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return false;
		}
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

	String createAccount(String password) {
		NewAccountIdentifier newAccount;
		try {
			newAccount = parity.personalNewAccount(password).sendAsync().get();
			return newAccount.getAccountId();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	TransactionReceipt waitForTransactionReceipt(String transactionHash) {
		if (transactionHash == null) {
			return null;
		}
		Optional<TransactionReceipt> transactionReceiptOptional;
		try {
			transactionReceiptOptional = getTransactionReceipt(transactionHash, SLEEP_DURATION, ATTEMPTS);
			if (!transactionReceiptOptional.isPresent()) {
				System.out.println("Transaction receipt not generated after " + ATTEMPTS + " attempts");
				return null;
			}
			return transactionReceiptOptional.get();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private Optional<TransactionReceipt> getTransactionReceipt(String transactionHash, int sleepDuration,
			int attempts) {

		Optional<TransactionReceipt> receiptOptional;
		try {
			receiptOptional = sendTransactionReceiptRequest(transactionHash);
			for (int i = 0; i < attempts; i++) {
				if (!receiptOptional.isPresent()) {
					Thread.sleep(sleepDuration);
					receiptOptional = sendTransactionReceiptRequest(transactionHash);
				} else {
					break;
				}
			}
			return receiptOptional;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	private Optional<TransactionReceipt> sendTransactionReceiptRequest(String transactionHash) {
		EthGetTransactionReceipt transactionReceipt;
		try {
			transactionReceipt = parity.ethGetTransactionReceipt(transactionHash).sendAsync().get();
			return transactionReceipt.getTransactionReceipt();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	BigInteger getNonce(String callerAddress) throws Exception {
		try {
			EthGetTransactionCount ethGetTransactionCount = parity
					.ethGetTransactionCount(callerAddress, DefaultBlockParameterName.LATEST).sendAsync().get();

			return ethGetTransactionCount.getTransactionCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String callConstantFunction(Function function, String contractAddress, String callerAddress) {

		String encodedFunction = FunctionEncoder.encode(function);
		try {
			org.web3j.protocol.core.methods.response.EthCall response = web3j
					.ethCall(Transaction.createEthCallTransaction(callerAddress, contractAddress, encodedFunction),
							DefaultBlockParameterName.LATEST)
					.sendAsync().get();

			return FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters()).get(0).getValue()
					.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String callTransactionFunction(Function function, String contractAddress, String callerAddress) {

		String encodedFunction = FunctionEncoder.encode(function);

		try {
			Transaction transaction = Transaction.createFunctionCallTransaction(callerAddress, getNonce(callerAddress),
					GAS_PRICE, GAS_LIMIT, contractAddress, encodedFunction);

			org.web3j.protocol.core.methods.response.EthSendTransaction transactionResponse = parity
					.ethSendTransaction(transaction).sendAsync().get();
			
			return transactionResponse.getTransactionHash();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String callEtherSend(Function function, String contractAddress, String callerAddress, BigInteger value) {

		String encodedFunction = FunctionEncoder.encode(function);

		try {
			Transaction transaction = Transaction.createFunctionCallTransaction(callerAddress, getNonce(callerAddress),
					GAS_PRICE, GAS_LIMIT, contractAddress, value, encodedFunction);

			org.web3j.protocol.core.methods.response.EthSendTransaction transactionResponse = parity
					.ethSendTransaction(transaction).sendAsync().get();

			return transactionResponse.getTransactionHash();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected List<EthLog.LogResult> createFilterForEvent(String encodedEventSignature, String contractAddress) {
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

}