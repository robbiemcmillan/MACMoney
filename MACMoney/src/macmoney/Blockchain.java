package macmoney;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Blockchain.java 
 * Purpose: Constructs the Blockchain, providing methods to validate its integrity.
 * 
 * @author William Robbie McMillan - 2035341
 * @version 1.0
 */

public class Blockchain {

	public List<Block> blockchain;

	public Blockchain() throws NoSuchAlgorithmException {

		blockchain = new ArrayList<>();

	}

	/**
	 * If block is not null add to the blockchain.
	 * 
	 * @param b
	 * @return b
	 * @throws NoSuchAlgorithmException
	 */
	public  Block addBlock(Block b) throws NoSuchAlgorithmException {

		if (b != null) {
			blockchain.add(b);
		}
		return b;

	}

	/**
	 * Returns the last block in the blockchain.
	 * 
	 * @return last block
	 */
	public Block lastBlock() {
		return blockchain.get(blockchain.size() - 1);
	}

	/**Return false if the current blocks hash does not equal the current block hash calculation.
	 * Return false if the previous blocks hash does not equal the current blocks previous hash.
	 * @return boolean
	 */
	public boolean verifyChain() {

		Block currentBlock;
		Block previousBlock;

		for (int i = 1; i < blockchain.size(); i++) {

			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i - 1);

			try {
				if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
					System.out.print("Current Hashes not equal");

					return false;

				}

				if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
					System.out.println("Previous Hashes not equal");

					return false;
				}
			} catch (NoSuchAlgorithmException e) {
				
				e.printStackTrace();
			}

		}
		System.out.println("Blockchain Verified!");
		return true;

	}

	

}
