package macmoney;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.BrokenBarrierException;


/**
 * 
 * miner.java 
 * Purpose: Proof-of-Work method which performs a 
 * mathematical puzzle. This puzzle produces a block hash with a predetermined
 * set of leading zeros using a nonce value that is incremented on every
 * iteration until a hash with the leading zeroes is found..
 * 
 * @author William Robbie McMillan - 2035341
 * @version 1.0
 */
public class Miner {

	
	// Create files for writing up to 5 peers blockchains to text files.
	File peeraledger = new File("PeerA.txt");
	File peerbledger = new File("PeerB.txt");
	File peercledger = new File("PeerC.txt");
	File peerdledger = new File("PeerD.txt");
	File peereledger = new File("PeerE.txt");

	
	// Create Strings for appending the block contents to.
	String ledgerA = "";
	String ledgerB = "";
	String ledgerC = "";
	String ledgerD = "";
	String ledgerE = "";

	int j = 0;
	

	public Miner() throws NoSuchAlgorithmException, IOException {

	}

	/**
	 * 
	 * Based on the block & blockchain parameters supplied, the mineBlock method
	 * carries out the calculation, incrementing the nonce until the leading zero hash
	 * is found.
	 * 
	 * @param block
	 * @param blockchain
	 * @throws NoSuchAlgorithmException
	 * @throws BrokenBarrierException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public synchronized void mineBlock(Block block, Blockchain blockchain)
			throws NoSuchAlgorithmException, InterruptedException, IOException {

		final int difficulty = Settings.miningDifficulty; // Amount of zeroes needed set here, the more zeroes, the
														  // longer it will
														  // take to find the leading zero hash.
		String target = "";

		String zero = "0";

		
		// Create file writers.
		FileWriter peeraledgerFW = new FileWriter(peeraledger);
		FileWriter peerbledgerFW = new FileWriter(peerbledger);
		FileWriter peercledgerFW = new FileWriter(peercledger);
		FileWriter peerdledgerFW = new FileWriter(peerdledger);
		FileWriter peereledgerFW = new FileWriter(peereledger);

		for (int i = 0; i < difficulty; i++) { // Amount of zeroes created here.
			target = target + zero;

		}

		/*
		 * While the beginning of the calculated hash does not equal the difficulty
		 * chosen, increment the nonce and set the blocks hash. Once the leading zero
		 * hash is found print out the hash and comapare.
		 * 
		 * NOTE: When implementing the finding consensus amongst nodes section look to
		 * compare all miners hashes before adding block.
		 */

		if (Peer.currentThread().getName().equals("Thread-0")) {
			System.out.println("Miners competing for the block");
		}

		while (!block.getHash().substring(0, difficulty).equals(target)) {
			block.nonce++;
			block.setHash(block.calculateHash());
		}

		Peer.peerList.add((Peer) Peer.currentThread());// Location in list shows the winning miner peer for that given block.
													 
		
		
		Thread.sleep(Settings.sleepPeers);
		
		String winningMinersHash = Peer.peerList.get(j).blockToBeMined.getHash();// Get the winning miners hash for comparing.


		

		
		/*
		 * If current peer is the winning miner, add the block to their blockchain &
		 * write to text file. Update balance of winning miner/peer.
		 * If not the winning miner, hash the blocks contents to verify the hash is legitimate,
		 * then add block and write to file.
		 * If comparison returns false, the peer discards the block & moves on to mine the next block
		 */
		
		if (Peer.currentThread().getName().equals(Peer.peerList.get(j).getName())) {
			
			System.out.println("Winning Peer: " + Peer.currentThread().getName());
			System.out.println("Winning Hash Found! : " + winningMinersHash);
			System.out.println("Rewarding winning miner with 30 $MAC");
			Peer.peerList.get(j).peerwallet.recieveMinerReward();
			System.out.println(Peer.currentThread().getName() + " Balance: " + Peer.peerList.get(j).peerwallet.getBalance());
			System.out.println("Winning miner adding block to their chain");
			blockchain.addBlock(Peer.peerList.get(j).blockToBeMined);
			
			System.out.println("Verifying block on the network"+"\n");

			if (Peer.currentThread().getName().equals("Thread-0")) {
				ledgerA = ledgerA + blockchain.lastBlock().blockContents() + "\n";

			}

			else if (Peer.currentThread().getName().equals("Thread-1")) {

				ledgerB = ledgerB + blockchain.lastBlock().blockContents() + "\n";

			}

			else if (Peer.currentThread().getName().equals("Thread-2")) {

				ledgerC = ledgerC + blockchain.lastBlock().blockContents() + "\n";

			} else if (Peer.currentThread().getName().equals("Thread-3")) {

				ledgerD = ledgerD + blockchain.lastBlock().blockContents() + "\n";

			} else {

				ledgerE = ledgerE + blockchain.lastBlock().blockContents() + "\n";

			}
			
			Thread.sleep(Settings.sleepPeers);

		}
		
		

		

		else {
			
			
			
			String verifyMinersHash = StringUtility.applySHA256(Peer.peerList.get(j).blockToBeMined.getPreviousHash()
					+ Long.toString(Peer.peerList.get(j).blockToBeMined.getTimeStamp())
					+ Peer.peerList.get(j).blockToBeMined.getTransaction() + Peer.peerList.get(j).blockToBeMined.nonce);
			

			if (winningMinersHash.equals(verifyMinersHash)) {

				System.out.println("Current Peer: "+Peer.currentThread().getName()+"\n"+ "Found Hash: "+ verifyMinersHash+"\n"+"Winning block hash verified, adding to Blockchain & writing to the ledger!" +

						"\nMoving on to the next Block!" + "\n");

				blockchain.addBlock(Peer.peerList.get(j).blockToBeMined);

				
				if (Peer.currentThread().getName().equals("Thread-0")) {
					ledgerA = ledgerA + blockchain.lastBlock().blockContents() + "\n";

				}

				else if (Peer.currentThread().getName().equals("Thread-1")) {

					ledgerB = ledgerB + blockchain.lastBlock().blockContents() + "\n";

				}

				else if (Peer.currentThread().getName().equals("Thread-2")) {

					ledgerC = ledgerC + blockchain.lastBlock().blockContents() + "\n";

				} else if (Peer.currentThread().getName().equals("Thread-3")) {

					ledgerD = ledgerD + blockchain.lastBlock().blockContents() + "\n";

				} else {

					ledgerE = ledgerE + blockchain.lastBlock().blockContents() + "\n";

				}
				Thread.sleep(Settings.sleepPeers);

			} else {

				System.out.println(
						"Verification failed, " + Peer.currentThread().getName() + " discarding block!" + "\n");
				
				Thread.sleep(Settings.sleepPeers);

			}
			}

		

		
		// Write blockchain contents to string.
		peeraledgerFW.write(ledgerA);
		peerbledgerFW.write(ledgerB);
		peercledgerFW.write(ledgerC);
		peerdledgerFW.write(ledgerD);
		peereledgerFW.write(ledgerE);

		j = j + Settings.numberOfPeers; // increment peer list based on numbers of peers active

		// close fileWriter.
		peeraledgerFW.close();
		peerbledgerFW.close();
		peercledgerFW.close();
		peerdledgerFW.close();
		peereledgerFW.close();

	}

}
