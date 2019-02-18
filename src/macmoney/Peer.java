package macmoney;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;


/**
 * 
 * Peer.java 
 * Purpose: Main functionality is carried out here.
 * Each Peer creates their own Blockchain and calls the miner to mine blocks.
 * 
 * Run Method based on Hunaid Husain's Java-Bitcoin-Minning program.
 * Source: https://github.com/hunaidee007/Java-Bitcoin-Minning
 * 
 * 
 * @author William Robbie McMillan - 2035341
 * @version 1.0
 */

public class Peer extends Thread implements Comparable<Peer>  {

	
	public Block blockToBeMined; // current block

	List<Transaction> unconfirmedTransactions = new ArrayList<Transaction>();

	List<Transaction> rejectedTransactions = new ArrayList<Transaction>();

	public static List<Peer> peerList = Collections.synchronizedList(new ArrayList<Peer>()); // Stores the order in which Peers win blocks
	
	public Wallet peerwallet = new Wallet(); // wallet object for crediting the winning miner for each block.

	public Blockchain bc;

	public Miner miner;

	public Wallet dummyWallet = new Wallet();

	CyclicBarrier updateWallets = null;

	Transaction nullTransaction;

	/**
	 * 
	 * @param  updateWallets
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public Peer(CyclicBarrier updateWallets) throws NoSuchAlgorithmException, IOException {

		bc = new Blockchain();
		miner = new Miner();
		peerwallet.setBalance(0);
		this.updateWallets = updateWallets;

	}

	/**
	 * Method to add transactions to the unconfirmedTransactions arraylist created
	 * in the main method.
	 * 
	 * @param transaction
	 */
	public synchronized void addTransaction(Transaction transaction) {

		unconfirmedTransactions.add(transaction);

	}

	/**
	 * 
	 * Takes input of an transaction from the unconfirmedTransactions list. The
	 * method then carries out a series of checks before adding each transaction to
	 * the next available block. Null transactions are added to ensure the
	 * Blockchain can generate enough confirmations for the transaction to be accepted.
	 * 
	 * @param transaction - transaction that is being checked
	 * @return true or false
	 * @throws NoSuchAlgorithmException
	 */
	
	public boolean checkPreviousTransactions(Transaction transaction) throws NoSuchAlgorithmException {

		double minusAmount = 0;
		double plusAmount = 0;

		for (int i = bc.blockchain.size() - 1; i >= 0; i--) { // loop to iterate backwards through the Blockchain as it
															  // as it is created.

			if (transaction == null) { // if no transaction data is available, add anyway
				return true;
			}

			if (bc.blockchain.get(i).getTransaction().transactionID() == transaction.transactionID()) {
				return false; // To prevent double spending, check the transactions ID against previous blocks
							  // transaction. If any are equal add a null transaction.
			}

			if (bc.blockchain.get(i).getTransaction() == null) {
				return true;
			}

			/*
			 * If the sender address occupies the sender address for any other transactions
			 * in the Blockchain, update the minusAmount variable with the amount sent. If
			 * the sender is the recipient in a previous transaction, update plusAmount.
			 */

			if (bc.blockchain.get(i).getTransaction().getSender() == transaction.getSender()) {

				minusAmount = minusAmount + bc.blockchain.get(i).getTransaction().getAmount();
			}

			else if (bc.blockchain.get(i).getTransaction().getRecipient() == transaction.getSender()) {

				plusAmount = plusAmount + bc.blockchain.get(i).getTransaction().getAmount();
			}

		}

		/*
		 * Subtract plusAmount from minusAmount, if the value is greater than or equal
		 * to the amount held in the current transactions, the transaction can be added
		 * to the block.
		 */

		if ((plusAmount - minusAmount) >= transaction.getAmount()) {

			// System.out.println("Enough Funds Available");
			return true;
		}

		else {
			// System.out.println("Insufficient Funds");
			return false;
		}

	}

	/**
	 * @return peerList
	 */
	public static List<Peer> getPeerList() {
		return peerList;
	}

	/*
	 * Method where each peer creates their own Blockchain, and carries out the
	 * checks before adding transactions to the blocks. 
	 * The miner is then called to compete with the other peers for who's block is added to the chain.
	 * Once a certain amount of blocks have been added, the associated wallets will be updated.
	 */
	public void run() {

		int i = 0;

		if (Peer.currentThread().getName().equals("Thread-0")) {
			System.out.println(
					"Starting up the $MAC Money Network" + "\n" + "Populating unconfirmed transactions list" + "\n");
		}

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while (i <= Settings.numberOfBlocksToMake) {
			//
			if (unconfirmedTransactions.size() != bc.blockchain.size()) { // Add null transactions to ensure the blockchain and unconfirmed transaction lists are equal.

				nullTransaction = new Transaction(dummyWallet.getPublicKey(), dummyWallet.getPublicKey(), 0); // Generate dummy transaction.
				try {
					nullTransaction.generateSignature(dummyWallet.getPrivateKey());
				} catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				unconfirmedTransactions.add(nullTransaction);

			}

			try {
				if (unconfirmedTransactions.get(i)
						.verifySignature(unconfirmedTransactions.get(i).getSender()) == true) {

					if (Peer.currentThread().getName().equals("Thread-0")) {
						System.out.println("Transaction signature verified, adding to the block!");
					}

				}

				else {

					if (Peer.currentThread().getName().equals("Thread-0")) {
						System.out.println("Transaction verification failed, nulling transaction!");
					}

					rejectedTransactions.add(unconfirmedTransactions.get(i));
					unconfirmedTransactions.set(i, nullTransaction);
					Thread.sleep(Settings.sleepPeers);

				}
			} catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException | InterruptedException e1) {
				
				e1.printStackTrace();
			}

			try {

				// If Blockchain does not exist, add genesis block & transaction.

				if (bc.blockchain.size() == 0) {

					blockToBeMined = new Block("0000", unconfirmedTransactions.get(i));
				}

				else {

					if (checkPreviousTransactions(unconfirmedTransactions.get(i)) == true) { // Perform checks
																								

						blockToBeMined = new Block(bc.blockchain.get(bc.blockchain.size() - 1).getHash(),
								unconfirmedTransactions.get(i)); // If true, add transaction
					} else {

						if (Peer.currentThread().getName().equals("Thread-0")) {

							System.out.println("Not enough coins to complete the transaction, nulling transaction!");

						}

						blockToBeMined = new Block(bc.blockchain.get(bc.blockchain.size() - 1).getHash(),
								nullTransaction);
						// else, add an null transaction to keep the Blockchain expanding.
					}
				}

				miner.mineBlock(blockToBeMined, bc);

				if (i >= Settings.confirmations) {// Amount of blocks that need added after the addition of a
												  // transactions  before the transactions can be confirmed.
												  
					this.updateWallets.await(); // Alert the CyclicBarrier in the main method, once a predetermined amount of peers reach this point.
				}
			}

			catch (Exception e) {
				
				e.printStackTrace();

			}

			i++;

		}

	}



	//Sort the peers in the peerlist based on size of blockchain.
	  
	@Override
	public int compareTo(Peer otherPeer)  {
		if (this.bc.blockchain.size() == otherPeer.bc.blockchain.size()) {

			return 0;
		} else if (this.bc.blockchain.size() > otherPeer.bc.blockchain.size()) {
			return 1;
		} else {
			return -1;
		}
	}
}
