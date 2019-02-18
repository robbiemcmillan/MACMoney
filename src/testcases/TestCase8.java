package testcases;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Collections;
import java.util.concurrent.CyclicBarrier;

import macmoney.Block;
import macmoney.Peer;
import macmoney.Transaction;
import macmoney.Wallet;

public class TestCase8 {
	
	

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeyException, SignatureException {
		
		Wallet lender = new Wallet();
		Wallet userA = new Wallet();
		Wallet dummy = new Wallet();
		
		
		
		Runnable updateAndPrint = new Runnable() {
			public void run() {

				

				

				Collections.sort(Peer.peerList, Collections.reverseOrder()); // Sort list of peers by longest blockchain.

				
				System.out.println(Peer.peerList.get(0).getName() + " " + Peer.peerList.get(0).bc.blockchain.size());
				System.out.println(Peer.peerList.get(1).getName() + " " + Peer.peerList.get(1).bc.blockchain.size());
				System.out.println();
			}
		};

		CyclicBarrier updateWallets = new CyclicBarrier(2, updateAndPrint); // create the CyclicBarrier method
																			// for updating wallet balances
		

		Peer peera = new Peer(updateWallets);
		Peer peerb = new Peer(updateWallets);
		
		
		lender.setBalance(200);
		
		
		peera.addTransaction(lender.sendFunds(userA.getPublicKey(), 200));
		peerb.addTransaction(lender.sendFunds(userA.getPublicKey(), 200));
		
		
		
		//add extra block to test peer sorting by blockchain
		peerb.bc.blockchain.add(new Block("0000", new Transaction(dummy.getPublicKey(),dummy.getPublicKey(),0))); 
		
		peera.start();
		peerb.start();
		
		

	}

}
