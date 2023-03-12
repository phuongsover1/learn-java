package com.semacticsquare.exceptions;

import java.io.*;

public class TryWithResourcesDemo {
    static String inFileStr = "walden.png";
    static String outFileStr = "walden-out.png";

    static void fileCopyWithArm() throws IOException {
        System.out.println("\nInside fileCopyWithArm ...");

        Test test = null;
        Test2 test2= null;
        try {
            test = new Test();
            test2 = new Test2();

            throw new IOException("Important exception");
        } finally {
            if (test != null) {
                test.close();
            }
            if (test2 != null) {
                try {

                test.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
/*
        try(BufferedInputStream in = new BufferedInputStream(new FileInputStream(inFileStr));
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outFileStr))) {
            byte[] byteBuf = new byte[4000];
            int numBytesRead;
            while((numBytesRead = in.read(byteBuf)) != -1) {
                out.write(byteBuf, 0, numBytesRead);
            }
            throw new IOException("Important exception");
        }
*/
    }

    public static void main(String... args) {
        try {
            fileCopyWithArm();
        } catch (IOException e) {
            e.printStackTrace();

//            Throwable[] throwables = e.getSuppressed();
//            System.out.println(throwables[0]);
//            System.out.println(throwables[1]);
        }

    }

}
class Test implements AutoCloseable {
    @Override
    public void close() throws IOException {
        throw new IOException("Trivial exception");
    }
}

class Test2 implements AutoCloseable {

    @Override
    public void close() throws IOException {
        throw new IOException("Trivial exception2");
    }
}
