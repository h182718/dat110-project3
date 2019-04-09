package no.hvl.dat110.node.client.test;

/**
 * exercise/demo purpose in dat110
 * @author tdoy
 *
 */

import java.math.BigInteger;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.List;

import no.hvl.dat110.file.FileManager;
import no.hvl.dat110.node.Message;
import no.hvl.dat110.rpc.StaticTracker;
import no.hvl.dat110.rpc.interfaces.ChordNodeInterface;
import no.hvl.dat110.util.Hash;
import no.hvl.dat110.util.Util;

public class NodeClientWriter extends Thread {

	private boolean succeed = false;
	private String content;
	private String filename;
	
	public NodeClientWriter(String content, String filename) {
		this.content = content;
		this.filename = filename;
	}
	
	public void run() {
		sendRequest();
	}
	
	private void sendRequest() {

		Registry registry = Util.tryIPs();
		// use the hash to retrieve the ChordNodeInterface remote object from the registry
		String haship = Hash.hashOf(Util.activeIP).toString();

		try {
			ChordNodeInterface entryNode = (ChordNodeInterface) registry.lookup(haship);
			FileManager fm = new FileManager(entryNode, StaticTracker.N);
			this.succeed = fm.requestWriteToFileFromAnyActiveNode(filename, content);

		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}

	}
	
	public boolean isSucceed() {
		return succeed;
	}

	public void setSucceed(boolean succeed) {
		this.succeed = succeed;
	}

}
