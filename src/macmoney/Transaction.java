package macmoney;

import java.security.*;
import java.util.Base64;

/**
 * 
 * transaction.java 
 * Purpose: Holds the sender, recipient & amount sent for a
 * given transaction.
 * 
 * @author William Robbie McMillan - 2035341
 * @version 1.0
 */

public class Transaction {

	private PublicKey sender; // senders address.
	private PublicKey recipient; // recipient address.
	private double amount; // amount to be sent.
	private String transactionID;
	public byte[] digitalSignature; // This is to prevent anybody else from spending funds in our wallet
	
	/**
	 * @param sender
	 * @param recipient
	 * @param amount
	 */
	public Transaction(PublicKey sender, PublicKey recipient, double amount) {
		this.sender = sender;
		this.recipient = recipient;
		this.amount = amount;
	}

	/**
	 * @return sender
	 */
	public PublicKey getSender() {
		return sender;
	}

	/**
	 * @param sender
	 */
	public void setSender(PublicKey sender) {
		this.sender = sender;
	}

	/**
	 * @return recipient
	 */
	public PublicKey getRecipient() {
		return recipient;
	}

	/**
	 * @param recipient
	 */
	public void setRecipient(PublicKey recipient) {
		this.recipient = recipient;
	}

	/**
	 * @return amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * 
	 * Generate a digital signature for a given transaction using the senders private key.
	 * 
	 * Source: https://www.pixelstech.net/article/1448118341-Signature-sign-verification-demo-in-Java
	 * 
	 * @param privateKey
	 * @return digitalSignature
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 */
	public byte[] generateSignature(PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		
		SecureRandom secureRandom = new SecureRandom();
		Signature signature = Signature.getInstance("SHA256WithRSA");
		signature.initSign(privateKey, secureRandom);
		String stuff = sender.toString() + recipient.toString() + Double.toString(amount);
		byte[] data = stuff.getBytes();
		signature.update(data);
		digitalSignature = signature.sign();
		return digitalSignature;
	}
	
	/**
	 * 
	 * Verify that the transaction is legitimate using the senders public key.
	 * 
	 * Source: https://www.pixelstech.net/article/1448118341-Signature-sign-verification-demo-in-Java
	 * 
	 * @param publicKey
	 * @return boolean
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 */
	public boolean verifySignature(PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		
		Signature signature = Signature.getInstance("SHA256WithRSA");
		signature.initVerify(publicKey);
		String stuff = sender.toString() + recipient.toString() + Double.toString(amount);
		byte[] data = stuff.getBytes();
		signature.update(data);
		boolean verified = signature.verify(digitalSignature);
		return verified;
	}

	/**
	 * @param publicKey
	 * @return Base64 string representation of PublicKey
	 */
	public String PublicKeyAsString(PublicKey publicKey) {

		byte[] encoded = Base64.getEncoder().encode(publicKey.getEncoded());

		return new String(encoded);

	}

	/**
	 * 
	 * Generate transctionID through concatenation of transaction data and hashing
	 * via SHA256 utility.
	 * 
	 * @return transactionID
	 * @throws NoSuchAlgorithmException
	 */
	public String transactionID() throws NoSuchAlgorithmException {
		
		transactionID = StringUtility.applySHA256(sender.toString() + recipient + Double.toString(amount));
		return transactionID;

	}
}