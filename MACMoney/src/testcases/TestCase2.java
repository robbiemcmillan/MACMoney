package testcases;

import java.util.concurrent.CyclicBarrier;

import macmoney.Peer;
import macmoney.Settings;
import macmoney.Transaction;
import macmoney.Wallet;

public class TestCase2 {

	private static int incrementI = 0;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		
		// Access to alter settings.
		Settings.confirmations = 1;
		Settings.numberOfPeers = 2;
		Settings.numberOfBlocksToMake = 1;
		
		Wallet lender = new Wallet();
		Wallet userA = new Wallet();
		Wallet wrongKeyWallet = new Wallet();

		
		
		/*Once a set confirmations (number of blocks in the chain after the transaction block is reached
		 *update wallets balances and increment the block
		 */
		
		
		Runnable updateAndPrint = new Runnable() {
			public void run() {

				int i = incrementI;

				System.out.println(userA.getBalance());
				System.out.println("Confirmations reached, finalizing & processing transaction");
				System.out.println("Lender Balance: "
						+ lender.updateBalance(Peer.peerList.get(Peer.peerList.size() - 1).bc.blockchain.get(i)));
				System.out.println("userA Balance: "
						+ userA.updateBalance(Peer.peerList.get(Peer.peerList.size() - 1).bc.blockchain.get(i)));
				incrementI++;
				System.out.println();
			}
		};

		CyclicBarrier updateWallets = new CyclicBarrier(2, updateAndPrint); // create the CyclicBarrier method 
																			// for updating wallet balances

		Peer peera = new Peer(updateWallets); //create peers
		Peer peerb = new Peer(updateWallets);

		lender.setBalance(200); //Set lender balance to 200 $MAC 		
		Transaction transaction = new Transaction(lender.getPublicKey(), userA.getPublicKey(), 200); //Create transaction sending 200 $MAC from the lender to userA
		transaction.generateSignature(wrongKeyWallet.getPrivateKey());//Genearate transaction signature to be verified by the peers

		peera.addTransaction(transaction); //Add transaction to each peers unconfirmed transactions list
		peerb.addTransaction(transaction);

		peera.start();
		peerb.start();

	}

}

