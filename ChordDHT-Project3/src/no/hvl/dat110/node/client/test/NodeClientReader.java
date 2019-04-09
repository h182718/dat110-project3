package no.hvl.dat110.node.client.test;

/**
 * exercise/demo purpose in dat110
 *
 * @author tdoy
 */

import no.hvl.dat110.file.FileManager;
import no.hvl.dat110.rpc.StaticTracker;
import no.hvl.dat110.rpc.interfaces.ChordNodeInterface;
import no.hvl.dat110.util.Hash;
import no.hvl.dat110.util.Util;

import java.math.BigInteger;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class NodeClientReader extends Thread {

    private boolean succeed = false;

    private String filename;

    public NodeClientReader(String filename) {
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
            this.succeed = fm.requestToReadFileFromAnyActiveNode(filename);

        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }

    }

    public boolean isSucceed() {
        return succeed;
    }

}
