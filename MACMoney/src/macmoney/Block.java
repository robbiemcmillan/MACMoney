package macmoney;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * 
 * Block.java 
 * Purpose: Construct the block, of which all it contents are hashed
 * together via the SHA256 utility class, including the previous blocks hash.
 * 
 * @author William Robbie McMillan - 2035341
 * @version 1.0
 */

public class Block {

	private String hash;
	private String previousHash;
	private Transaction transaction;
	private long timestamp; // as number of milliseconds since 1/1/1970.
	public int nonce;
	private String blockContents; // Concatenated string of block contents.

	/**
	 * @param previousHash - Hash of the previous block in the blockchain
	 * @param transaction - transaction added to the block
	 * @throws NoSuchAlgorithmException
	 */
	public Block(String previousHash, Transaction transaction) throws NoSuchAlgorithmException {

		this.transaction = transaction;
		this.timestamp = new Date().getTime();
		this.previousHash = previousHash;
		this.hash = calculateHash();

	}

	/**
	 * Concatenates & calculates the hash of the given block.
	 * 
	 * @return hash
	 * @throws NoSuchAlgorithmException
	 */
	public synchronized String calculateHash() throws NoSuchAlgorithmException {

		String calculatedhash = StringUtility
				.applySHA256(previousHash + Long.toString(timestamp) + transaction + Integer.toString(nonce));

		return calculatedhash;
	}

	/**
	 * @return hash
	 */
	public synchronized String getHash() {
		return hash;
	}

	/**
	 * @param hash
	 */
	public synchronized void setHash(String hash) {
		this.hash = hash;
	}


	/**
	 * @return timestamp
	 */
	public long getTimeStamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return transaction
	 */
	public Transaction getTransaction() {
		return transaction;
	}

	/**
	 * @param transactionList
	 */
	public void setTransactions(Transaction transaction) {
		this.transaction = transaction;
	}

	/**
	 * @return nonce
	 */
	public int getNonce() {
		return nonce;
	}

	/**
	 * @param nonce
	 */
	public void setNonce(int nonce) {
		this.nonce = nonce;
	}

	/**
	 * @return previousHash
	 */
	public String getPreviousHash() {
		return previousHash;
	}

	/**
	 * @param previousHash
	 */
	public void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}

	/**
	 * Method to write the contents of the block to a string for printing out the Blockchain.
	 * 
	 * @return blockContents
	 * @throws NoSuchAlgorithmException
	 */
	public String blockContents() throws NoSuchAlgorithmException {
		
		String transactionID = "";
		String sender = "";
		String recipient = "";
		double amount = 0;

		if (transaction != null) { // if statement to deal with null values in transactions

			transactionID = transaction.transactionID();
			sender = transaction.PublicKeyAsString(transaction.getSender());
			recipient = transaction.PublicKeyAsString(transaction.getRecipient());
			amount = transaction.getAmount();

		}

		else {
			transactionID = "N/A";
			sender = "N/A";
			recipient = "N/A";
			amount = 0;
		}

		// format the contents for printing to the ledger. 
		
		blockContents = String.format(
				"Block Hash: %s\nPrevious Block Hash: %s\nNonce: %d\nTime Stamp: %d\nTransaction ID: %s\n"
						+ "Sender Address: %s\nReceiving Address: %s\nAmount: %f\n",
				hash, previousHash, nonce, timestamp, transactionID, sender, recipient, amount);

		return blockContents;

	}

}
