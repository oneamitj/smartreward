package com.rewardapp.smartreward;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.rewardapp.smartreward.SmartReward.BroadcastRewardEventResponse;
import org.apache.commons.codec.binary.StringUtils;
import org.web3j.utils.Numeric;

public class App {
	static final String OWNER = "0xCBaEbf0FDb942eeB9B1F3B897c392de1405764d5";
	static final String USR1 = "0x5a705c6335d250Ed389A90B7446aeAceDEe96f69";
	static final String USR2 = "0xCCD21FAA9d6321F3F66e29D9FED62430A85f11DB";

	public static void main(String[] args) {
		System.out.println("\nProgram Start\n");

		MultiRewardWrapper smartReward = new MultiRewardWrapper();

		Scanner scanner = new Scanner(System.in);
		String user;
		String to;
		int value;
		String reason;
		String transactionHash = null;


//		System.out.println(StringUtils.getBytesUsAscii("nrs").length);
//		System.out.println(Numeric.toBytesPadded(Numeric.toBigInt("nrs".getBytes()), 32));
//		System.out.println(reward);
//		System.out.println(smartReward.hexToASCII(reward).equals("nrs"));
//		System.out.println(smartReward.setAvailRewardPoints(reward,11111111));
		System.out.println(smartReward.showAvailReward("nrs"));
//		System.out.println(smartReward.showRewardTypes("0xCBaEbf0FDb942eeB9B1F3B897c392de1405764d5"));
		System.out.println("\nProgram Sop\n");

	}

	public static void mainSub(String[] args) throws Exception {
		System.out.println("\nProgram Init\n");

		RewardWrapper smartReward = new RewardWrapper();

		Scanner scanner = new Scanner(System.in);
		String user;
		String to;
		int value;
		String reason;
		String transactionHash = null;

		while (true) {
			System.out.println(
					"1. Give Task\n2. Check My Reward Points\n3. Send Reward\n4. Spent Reward\n5. Check Availabe Reward\n6. Set Availabe Reward\n7. Check Transaction Details\n8. Show All Transaction\n0. Exit\nChoose Option (0-8): ");
			int choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 0:
				System.exit(0);
			case 1: // Give Task
				System.out.println("Enter user hash: ");
				user = scanner.next();

				if (!smartReward.checkAccount(user)) {
					System.out.println("Invalid User");
					System.exit(0);
				}

				System.out.println("Enter Reward Value: ");
				value = scanner.nextInt();
				scanner.nextLine();

				System.out.println("Reason for Reward: ");
				reason = scanner.nextLine();
				transactionHash = someWork(smartReward, user, value, reason);
				break;
			case 2: // Check Reward Point
				System.out.println("Enter user hash: ");
				user = scanner.next();

				if (!smartReward.checkAccount(user)) {
					System.out.println("Invalid User");
					System.exit(0);
				}
				System.out.println(smartReward.showMyReward(user));
				break;
			case 3: // Send Reward
				System.out.println("Enter user hash: ");
				user = scanner.next();

				System.out.println("Enter receiver hash: ");
				to = scanner.next();

				if (!smartReward.checkAccount(user)) {
					System.out.println("Invalid User");
					System.exit(0);
				}

				if (!smartReward.checkAccount(to)) {
					System.out.println("Invalid Receiver");
					System.exit(0);
				}

				System.out.println("Enter Send Value: ");
				value = scanner.nextInt();
				scanner.nextLine();

				System.out.println("Message: ");
				reason = scanner.nextLine();
				System.out.println(smartReward.sendReward(user, to, value, reason));
				break;
			case 4: // Spent Reward
				System.out.println("Enter user hash: ");
				user = scanner.next();

				if (!smartReward.checkAccount(user)) {
					System.out.println("Invalid User");
					System.exit(0);
				}

				System.out.println("Enter Reward Value: ");
				value = scanner.nextInt();
				scanner.nextLine();

				System.out.println("Spent on: ");
				reason = scanner.nextLine();

				System.out.println(smartReward.spendReward(user, value, reason));
				break;
			case 5: // Check Availabe Reward
				System.out.println(smartReward.showAvailReward());
				break;
			case 6: // Set Availabe Reward
				System.out.println("Enter Reward Value: ");
				value = scanner.nextInt();
				scanner.nextLine();

				System.out.println(smartReward.setAvailRewardPoints(value));
				break;
			case 7: // Check Transaction Details
				System.out.println("Enter transaction hash: ");
				transactionHash = scanner.next();

				if (transactionHash != null) {
					
					List<BroadcastRewardEventResponse> events = smartReward.getBroadcastRewardEvent(transactionHash);
					for (int i = 0; i < events.size(); i++) {
						System.out.println("Sender: " + events.get(i).sender);
						System.out.println("Receiver: " + events.get(i).receiver);
						System.out.println("Value: " + events.get(i).value);
						System.out.println("Event: " + events.get(i).event);
						System.out.println("Reason: " + events.get(i).reason);
					}
				}
				break;
			case 8:
				List<BroadcastRewardEventResponse> events = smartReward.getAllBroadcastRewardEvent();
				for (int i = 0; i < events.size(); i++) {
					System.out.println("Sender: " + events.get(i).sender);
					System.out.println("Receiver: " + events.get(i).receiver);
					System.out.println("Value: " + events.get(i).value);
					System.out.println("Event: " + events.get(i).event);
					System.out.println("Reason: " + events.get(i).reason);
					System.out.println("________");
				}
				break;
			default:
				System.out.println("\nInvalid Choice\n");
				break;
			}

			System.out.println("\nContinue? (y,n): ");
			String runAgain = scanner.next();

			if (!runAgain.trim().toLowerCase().equals("y")) {
				break;
			}
		}

		System.out.println("\nProgram End\n");
	}

	private static String someWork(RewardWrapper smartReward, String user, int value, String reason) {
		String transactionHash = null;
		Random rand = new Random();
		int num1 = rand.nextInt(10);
		int num2 = rand.nextInt(10);

		Scanner scanner = new Scanner(System.in);
		System.out.format("Please Complete this task for reward.\n%d + %d = ", num1, num2);
		int sum = scanner.nextInt();
		scanner.nextLine();

		if (sum == (num1 + num2)) {
			transactionHash = smartReward.giveReward(user, value, reason);
			System.out.println("Well Done");
		} else {
			System.out.println("Invalid Work");
		}

		return transactionHash;
	}
}
