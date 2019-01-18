package testcases;

import java.util.Collections;
import java.util.concurrent.*;
import macmoney.Peer;
import macmoney.Settings;
import macmoney.Wallet;


public class TestCase7 {

	private static int incrementI = 0;
	

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// Access to alter settings.
		Settings.confirmations = 6;
		Settings.numberOfPeers = 5;
		Settings.numberOfBlocksToMake = 10;
		
		
		Wallet lender = new Wallet();
		Wallet userA = new Wallet();
		Wallet userB = new Wallet();

		/*
		 * Once a set confirmations (number of blocks in the chain after the transaction
		 * block is reached update wallets balances and increment the block
		 */

		Runnable updateAndPrint = new Runnable() {
			public void run() {

				

				int i = incrementI;

				Collections.sort(Peer.peerList, Collections.reverseOrder()); // Sort list of peers by longest blockchain.

				System.out.println("Confirmations reached, finalizing & processing transaction");
				System.out.println("Lender Balance: "+ lender.updateBalance(Peer.peerList.get(0).bc.blockchain.get(i)));
				System.out.println("userA Balance: "+ userA.updateBalance(Peer.peerList.get(0).bc.blockchain.get(i)));
				System.out.println("userB Balance: "+ userB.updateBalance(Peer.peerList.get(0).bc.blockchain.get(i)));
				System.out.println(Peer.peerList.get(0).getName() + " " + Peer.peerList.get(0).bc.blockchain.size());
				System.out.println(Peer.peerList.get(1).getName() + " " + Peer.peerList.get(1).bc.blockchain.size());
				incrementI++;
				System.out.println();
			}
		};

		CyclicBarrier updateWallets = new CyclicBarrier(5, updateAndPrint); // create the CyclicBarrier method
																			// for updating wallet balances

		Peer peera = new Peer(updateWallets); // create peers
		Peer peerb = new Peer(updateWallets);
		Peer peerc = new Peer(updateWallets);
		Peer peerd = new Peer(updateWallets);
		Peer peere = new Peer(updateWallets);
		

		lender.setBalance(200); // Set lender balance to 200 $MAC

		peera.addTransaction(lender.sendFunds(userA.getPublicKey(), 200)); // Add transaction to each peers unconfirmed transactions list
		peerb.addTransaction(lender.sendFunds(userA.getPublicKey(), 200));
		peerc.addTransaction(lender.sendFunds(userA.getPublicKey(), 200));
		peerd.addTransaction(lender.sendFunds(userA.getPublicKey(), 200));
		peere.addTransaction(lender.sendFunds(userA.getPublicKey(), 200));
		
		
		peera.addTransaction(userA.sendFunds(userB.getPublicKey(), 100));
		peerb.addTransaction(userA.sendFunds(userB.getPublicKey(), 100));
		peerc.addTransaction(userA.sendFunds(userB.getPublicKey(), 100));
		peerd.addTransaction(userA.sendFunds(userB.getPublicKey(), 100));
		peere.addTransaction(userA.sendFunds(userB.getPublicKey(), 100));
		
		
		
		peera.start();
		peerb.start();
		peerc.start();
		peerd.start();
		peere.start();

	}

}
