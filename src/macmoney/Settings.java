package macmoney;

/**Settings.java
 * Purpose: Allow easier access to altering certain variables within MACMoney.
 * Default values that can be changed in the test case file or within this file.
 * 
 * @author William Robbie McMillan
 * @version 1.0
 */
public class Settings {
	
	public static int confirmations = 0; // amount of blocks needed after a transaction block before it can be processed.
	public static int numberOfPeers = 2; // how many Peers that are active.
	public static int miningDifficulty = 3; // POW, how many zeroes needed.
	public static int numberOfBlocksToMake = 0; // number of blocks to be made by the Peer class + 1.
	public static int sleepPeers = 1000; // How long to sleep the peers for.


}



