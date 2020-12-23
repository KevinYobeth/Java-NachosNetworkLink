package nachos.proj1;

import java.util.Vector;

import nachos.threads.KThread;
import nachos.threads.ThreadQueue;

public class Queue extends ThreadQueue {
	Vector<KThread> listQueue = new Vector<>();

	public Queue() {
	}

	@Override
	public void waitForAccess(KThread thread) {
		listQueue.add(thread);
	}

	@Override
	public KThread nextThread() {
		if (listQueue.isEmpty()) {
			return null;
		}
		return listQueue.remove(0);
//		return listQueue.remove(listQueue.size() - 1);
	}

	@Override
	public void acquire(KThread thread) {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

}
