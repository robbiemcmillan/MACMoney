package macmoney;

import java.security.*;

/**
 * Wallet.java
 * Purpose: Create the users wallet which generates public and private keys.
 * Includes method to update users and miners wallet balances.
 * 
 * 
 * @author William Robbie McMillan - 2035341
 *
 */
public class Wallet {

	public PublicKey publicKey;
	public PrivateKey privateKey;
	public double balance;

	public Wallet() throws NoSuchAlgorithmException {

		keyPairGenerator();
		balance = 0;

	}

	/**
	 * Generate the RSA public and private keys for creating, signing and verifying transactions.
	 * 
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	public void keyPairGenerator() throws NoSuchAlgorithmException {

		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");

		kpg.initialize(512);

		KeyPair kp = kpg.genKeyPair();

		privateKey = kp.getPrivate();
		publicKey = kp.getPublic();

	}

	/**
	 * @param amount
	 */
	public void setBalance(double amount) {

		this.balance = amount;

	}

	/**
	 * @return publicKey
	 */
	public PublicKey getPublicKey() {
		return publicKey;
	}

	/**
	 * @return privateKey
	 */
	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	/**
	 * @return balance
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * Special method for updating winning miner with 30 $MAC
	 */
	public void recieveMinerReward() {

		double reward = 30;

		balance += reward;

	}

	/**
	 * Update balance of associated wallets based on the block parameter.
	 * 
	 * @param block
	 * @return balance
	 */
	public double updateBalance(Block block) {

		if (block.getTransaction().getSender() == publicKey) {

			balance = balance - block.getTransaction().getAmount();
		}

		else if (block.getTransaction().getRecipient() == publicKey) {

			balance = balance + block.getTransaction().getAmount();
		}

		return balance;
	}

	/**
	 * 
	 * Create and sign new transaction based on the sending wallets public and private keys.
	 * 
	 * 
	 * @param recipient
	 * @param amount
	 * @return newTransaction
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws SignatureException
	 */
	public Transaction sendFunds(PublicKey recipient, double amount)
			throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {

		Transaction newTransaction = new Transaction(publicKey, recipient, amount);
		newTransaction.generateSignature(privateKey);

		return newTransaction;

	}

}
