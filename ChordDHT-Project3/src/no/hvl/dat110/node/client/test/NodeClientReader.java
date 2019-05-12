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

        String activenodes = StaticTracker.ACTIVENODES[0];
        // connect to an active chord node - can use the process defined in StaticTracker
        Registry reg = Util.locateRegistry(activenodes);
        BigInteger bi = Hash.hashOf(activenodes);

        try {
            ChordNodeInterface cni = (ChordNodeInterface) reg.lookup(bi.toString());
            FileManager fm = new FileManager(cni, StaticTracker.N);
            succeed = fm.requestToReadFileFromAnyActiveNode(filename);

        } catch(RemoteException e) {
            e.printStackTrace();
        } catch(NotBoundException e) {
            e.printStackTrace();
        }

    }

    public boolean isSucceed() {
        return succeed;
    }

}
