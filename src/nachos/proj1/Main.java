package nachos.proj1;

import java.util.Vector;

import nachos.machine.Machine;
import nachos.machine.MalformedPacketException;
import nachos.machine.NetworkLink;
import nachos.machine.Packet;
import nachos.threads.KThread;
import nachos.threads.Semaphore;

public class Main {

	Semaphore sem = new Semaphore(0);
	NetworkLink nl = Machine.networkLink();
	MyConsole cons = new MyConsole();
	Vector<MyFile> fileList = new Vector<>();
	
	
	public Main() {

		init();

		int sel, size, dest;
		String name, type;

		do {
			mainMenu();
			sel = cons.readInt();

			if (sel == 1) {
				do {
					cons.write("Input file name: ");
					name = cons.read();
				} while (name.startsWith(".") || name.endsWith(".") || !name.contains("."));
				
				do {
					cons.write("Input size: ");
					size = cons.readInt();
				} while (size < 1 || size > 500);
				
				do {
					cons.write("Input file type: ");
					type = cons.read();
				} while (type.equals("Word Document") || type.equals("Excel Worksheet") || type.equals("Unspecified File"));
				
				cons.write("Send To Computer: ");
				dest = cons.readInt();
				String str = name + "-" + size + "-" + type;
				
				byte[] contents = str.getBytes();
				
				try {
					Packet pkt = new Packet(dest, nl.getLinkAddress(), contents);
					nl.send(pkt);
					sem.P();
					cons.writeLn("File Sent!");
				} catch (MalformedPacketException e) {
					e.printStackTrace();
				}
				
			} else if (sel == 2) {
				if (fileList.isEmpty()) {
					cons.writeLn("No file received!");
				} else {
					cons.writeLn("Name           |Size      |Type");
					for (MyFile myFile : fileList) {
						new KThread(myFile).fork();
					}
				}
			}
		} while (sel != 3);

		cons.writeLn("Ticks of time: " + Machine.timer().getTime());
	}

	private void mainMenu() {
		cons.writeLn("Computer Number: " + nl.getLinkAddress());
		cons.writeLn();
		cons.writeLn("File Manager");
		cons.writeLn("1. Send File");
		cons.writeLn("2. View Existing File(s)");
		cons.writeLn("3. Exit");
		cons.write("Choose: ");
	}

	private void init() {
		Runnable sendInterruptHandler = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				sem.V();
			}
		};

		Runnable receiveInterruptHandler = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Packet pkt = nl.receive();
				String str = new String(pkt.contents);
				String[] temp = str.split("-");
				fileList.add(new MyFile(temp[0], temp[2], Integer.parseInt(temp[1])));
				sem.V();
			}
		};

		nl.setInterruptHandlers(receiveInterruptHandler, sendInterruptHandler);
	}

}
