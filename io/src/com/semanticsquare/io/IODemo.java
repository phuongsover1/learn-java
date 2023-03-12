package com.semanticsquare.io;

import java.io.*;
import java.util.Scanner;

class IODemo {
	static String inFileStr = "walden.png";
	static String outFileStr = "walden-out.png";

	// By convention, static nested classes should be placed before static methods
	public static class SerializableDemo implements Serializable {
		private static final long serialVersionUID = 8882416210786165012L;
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		private transient int id = 4;

		public int getId() {
			return id;
		}

		private String gender;

	}

	static void fileCopyNoBuffer() {
		System.out.println("\nInside fileCopyNoBuffer ...");

		long startTime, elapsedTime;

		File fileIn = new File(inFileStr);
		System.out.println("File size is " + fileIn.length() + " bytes");

		try (FileInputStream in = new FileInputStream(inFileStr);
				FileOutputStream out = new FileOutputStream(outFileStr)) {
			startTime = System.nanoTime();
			int byteRead;

			while ((byteRead = in.read()) != -1) {
				out.write(byteRead);
			}
			elapsedTime = System.nanoTime() - startTime;
			System.out.println("Elapsed Time is " + (elapsedTime / 1000000.0) + " msec");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void fileCopyWithBufferAndArray() {
		System.out.println("\nInside fileCopyWithBufferAndArray ...");

		long startTime, elapsedTime;

		File fileIn = new File(inFileStr);
		System.out.println("File size is " + fileIn.length() + " bytes");

		startTime = System.nanoTime();
		try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(inFileStr));
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outFileStr))) {

			byte[] byteBuf = new byte[8000];
			int numBytesRead;
			while ((numBytesRead = in.read(byteBuf)) != -1) {
				out.write(byteBuf, 0, numBytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		elapsedTime = System.nanoTime() - startTime;
		System.out.println("Elapsed Time is " + (elapsedTime / 1000000.0) + " msec");
	}

	private static void readFromStandardInput() throws UnsupportedEncodingException, IOException {
		System.out.println("\nInside readFromStandardInput ...");
		String data;

		/*
		 * System.out.println("Enter \"start\" to continue (Using BufferedReader): ");
		 * try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new
		 * FileOutputStream("test"))); BufferedReader in = new BufferedReader(new
		 * InputStreamReader(System.in, "UTF-8"))) {
		 * while ((data = in.readLine()) != null && !data.equals("start")) {
		 * System.out.println("\nDid not enter \"start\". Try again.: ");
		 * if (data != null)
		 * writer.append(data);
		 * }
		 * }
		 * System.out.println("Current");
		 */
		System.out.println("\nEnter \"start\" to continue (Using java.util.Scanner): ");
		try (Scanner sc = new Scanner(System.in); Scanner sc1 = new Scanner("Hello, How are you ?")) {
			while (!(data = sc.nextLine()).equals("start")) {
				System.out.println("Did not enter \"start\". Try again:  ");
			}
			System.out.println("Now enter the start code:");
			int code = sc.nextInt();
			System.out.println("You entered code: " + code);

			while (sc1.hasNext())
				System.out.println(sc1.next());
		}
		System.out.println("Correct");

	}

	private static void fileMethodDemo() {
		System.out.println("\nInside fileMethodsDemo ...");

		File f = new File("/media/phuong/DATA/Spring_ly_thuyet/io/src/../file.txt");

		System.out.println("getAbsolutePath(): " + f.getAbsolutePath());
		try {
			System.out.println("getCanonicalPath()" + f.getCanonicalPath());
			System.out.println("createNewFile(): " + f.createNewFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("separator: " + File.separator);
		System.out.println("sepatatorChar: " + File.separatorChar);
		System.out.println("getParent(): " + f.getParent());
		System.out.println("lastModified(): " + f.lastModified());
		System.out.println("exists(): " + f.exists());
		System.out.println("isFile(): " + f.isFile());
		System.out.println("isDirectory(): " + f.isDirectory());
		System.out.println("length(): " + f.length());

		System.out.println("My working or user directory: " + System.getProperty("user.dir"));
		System.out.println("new File(\"testdir\").mkdir(): " + new File("testdir").mkdir());
		System.out.println("new File(\"testdir/test\").mkdir(): " + new File("testdir/test").mkdirs());
		System.out.println("new File(\"testdir/test\").delete(): " + new File("testdir/test").delete());
		System.out.println("new File(\"testdir/test1/test2\").mkdir(): " + new File("testdir/test1/test2").mkdir());
		System.out.println("new File(\"testdir/test1/test2\").mkdirs(): " + new File("testdir/test1/test2").mkdirs());

		File f2 = new File("temp2.txt");
		try {

			System.out.println("f2.createNewFile(): " + f2.createNewFile());
			System.out.println("f2.renameTo(...): " + f2.renameTo(new File("test333.txt")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void dirFilter(boolean applyFilter) {
		System.out.println("\nInside dirFilter ...");

		File path = new File(".");
		String[] list;

		if (applyFilter)
			list = path.list(new DirFilter());
		else
			list = path.list();

		for (String dirItem : list) {
			System.out.println(dirItem);
		}
	}

	public void doSerialization() {
		System.out.println("\nInside doSerialization ...");

		SerializableDemo serializableDemo = new SerializableDemo();
		serializableDemo.setName("Java");
		System.out.println("name (before serialization): " + serializableDemo.getName());
		System.out.println("id (before serialization): " + serializableDemo.getId());

		try (ObjectOutputStream out = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream(new File("../serial.ser"))))) {
			out.writeObject(serializableDemo);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void doDeserialization() {
		System.out.println("\nInside doDeserialization ...");

		try (ObjectInputStream in = new ObjectInputStream(
				new BufferedInputStream(
						new FileInputStream(new File("../serial.ser"))))) {

			SerializableDemo serializableDemo = (SerializableDemo) in.readObject();
			System.out.println("name (after serialization): " + serializableDemo.getName());
			System.out.println("id (after serialization): " + serializableDemo.getId());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void encodingSync() {
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(new File("encode.txt")), "UTF-16"))) {
			System.out.println(br.readLine());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String... args) {
		// fileCopyNoBuffer();
		// fileCopyWithBufferAndArray();
		/*
		 * try {
		 * readFromStandardInput();
		 * } catch (IOException e) {
		 * e.printStackTrace();
		 * }
		 */
		// fileMethodDemo();
		// dirFilter(true);
		//

		// new IODemo().doSerialization();
		// new IODemo().doDeserialization();
		encodingSync();
	}
}

class DirFilter implements FilenameFilter {

	@Override
	public boolean accept(File dir, String name) {
		return name.endsWith(".png") || name.endsWith(".PNG");
	}

}
