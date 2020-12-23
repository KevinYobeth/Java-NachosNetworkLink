//package nachos.proj1;
//
//import nachos.machine.SerialConsole;
//import nachos.threads.Semaphore;
//
//public class MyConsole {
//	SerialConsole sercon;
//	Semaphore semaphore = new Semaphore(0);;
//	char temp;
//
//	public MyConsole(SerialConsole sercon) {
//		this.sercon = sercon;
//
//		Runnable recv = new Runnable() {
//			public void run() {
//				temp = (char) sercon.readByte();
//				semaphore.V();
//			}
//		};
//
//		Runnable send = new Runnable() {
//
//			public void run() {
//				semaphore.V();
//			}
//		};
//
//		this.sercon.setInterruptHandlers(recv, send);
//	}
//
//	public void write(String str) {
//		for (int i = 0; i < str.length(); i++) {
//			this.sercon.writeByte(str.charAt(i));
//			semaphore.P();
//		}
//	}
//
//	public void writeLn(String str) {
//		write(str + '\n');
//	}
//	
//	public void writeLn() {
//		write("\n");
//	}
//
//
//	public String read() {
//		String result = "";
//		do {
//			semaphore.P();
//			if (temp != '\n') {
//				result += temp;
//			}
//		} while (temp != '\n');
//		return result;
//	}
//
//	public int readInt() {
//		int result = 0;
//		try {
//			result = Integer.parseInt(read());
//		} catch (Exception e) {
//			// TODO: handle exception
//			result = 0;
//		}
//		return result;
//	}
//}

package nachos.proj1;

import nachos.machine.Machine;
import nachos.machine.SerialConsole;
import nachos.threads.Semaphore;

public class MyConsole {

	Semaphore sem = new Semaphore(0);
	SerialConsole sc = Machine.console();
	char temp;

	public MyConsole() {
		Runnable receiveInterruptHandler = new Runnable() {
			@Override
			public void run() {
				temp = (char) sc.readByte();
				sem.V();
			}
		};

		Runnable sendInterruptHandler = new Runnable() {
			@Override
			public void run() {
				sem.V();
			}
		};

		sc.setInterruptHandlers(receiveInterruptHandler, sendInterruptHandler);
	}

	public String read() {
		String str = "";
		
		do {
			sem.P();
			if (temp != '\n')
			{
				str += temp;
			}
		} while (temp != '\n');

		return str;
	}

	public int readInt() {
		int num = 0;

		try {
			num = Integer.parseInt(read());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return num;
	}

	public void write(String output) {
		for (int i = 0; i < output.length(); i++) {
			sc.writeByte(output.charAt(i));
			sem.P();
		}

	}

	public void writeLn(String output) {
		write(output + "\n");
	}

	public void writeLn() {
		write("\n");
	}

}
