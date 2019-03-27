package android.os;

import java.io.File;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

public class ParcelFileDescriptor implements Parcelable, Closeable {
    public static class AutoCloseInputStream
            extends FileInputStream
    {
        public AutoCloseInputStream(ParcelFileDescriptor pfd)
        {
            super(pfd.getFileDescriptor());
            throw new RuntimeException("Stub!");
        }

        public void close()
                throws IOException
        {
            throw new RuntimeException("Stub!");
        }

        public int read()
                throws IOException
        {
            throw new RuntimeException("Stub!");
        }

        public int read(byte[] b)
                throws IOException
        {
            throw new RuntimeException("Stub!");
        }

        public int read(byte[] b, int off, int len)
                throws IOException
        {
            throw new RuntimeException("Stub!");
        }
    }

    public static class AutoCloseOutputStream
            extends FileOutputStream
    {
        public AutoCloseOutputStream(ParcelFileDescriptor pfd)
        {
            super(pfd.getFileDescriptor());
            throw new RuntimeException("Stub!");
        }

        public void close()
                throws IOException
        {
            throw new RuntimeException("Stub!");
        }
    }

    public static abstract interface OnCloseListener
    {
        public abstract void onClose(IOException paramIOException);
    }

    public static class FileDescriptorDetachedException
            extends IOException
    {
        public FileDescriptorDetachedException()
        {
            throw new RuntimeException("Stub!");
        }
    }

    public ParcelFileDescriptor(ParcelFileDescriptor wrapped)
    {
        throw new RuntimeException("Stub!");
    }

    public static ParcelFileDescriptor open(File file, int mode)
            throws FileNotFoundException
    {
        throw new RuntimeException("Stub!");
    }

    public static ParcelFileDescriptor open(File file, int mode, Handler handler, OnCloseListener listener)
            throws IOException
    {
        throw new RuntimeException("Stub!");
    }

    public static ParcelFileDescriptor dup(FileDescriptor orig)
            throws IOException
    {
        throw new RuntimeException("Stub!");
    }

    public ParcelFileDescriptor dup()
            throws IOException
    {
        throw new RuntimeException("Stub!");
    }

    public static ParcelFileDescriptor fromFd(int fd)
            throws IOException
    {
        throw new RuntimeException("Stub!");
    }

    public static ParcelFileDescriptor adoptFd(int fd)
    {
        throw new RuntimeException("Stub!");
    }

    public static ParcelFileDescriptor fromSocket(Socket socket)
    {
        throw new RuntimeException("Stub!");
    }

    public static ParcelFileDescriptor fromDatagramSocket(DatagramSocket datagramSocket)
    {
        throw new RuntimeException("Stub!");
    }

    public static ParcelFileDescriptor[] createPipe()
            throws IOException
    {
        throw new RuntimeException("Stub!");
    }

    public static ParcelFileDescriptor[] createReliablePipe()
            throws IOException
    {
        throw new RuntimeException("Stub!");
    }

    public static ParcelFileDescriptor[] createSocketPair()
            throws IOException
    {
        throw new RuntimeException("Stub!");
    }

    public static ParcelFileDescriptor[] createReliableSocketPair()
            throws IOException
    {
        throw new RuntimeException("Stub!");
    }

    public static int parseMode(String mode)
    {
        throw new RuntimeException("Stub!");
    }

    public FileDescriptor getFileDescriptor()
    {
        throw new RuntimeException("Stub!");
    }

    public long getStatSize()
    {
        throw new RuntimeException("Stub!");
    }

    public int getFd()
    {
        throw new RuntimeException("Stub!");
    }

    public int detachFd()
    {
        throw new RuntimeException("Stub!");
    }

    public void close()
            throws IOException
    {
        throw new RuntimeException("Stub!");
    }

    public void closeWithError(String msg)
            throws IOException
    {
        throw new RuntimeException("Stub!");
    }

    public boolean canDetectErrors()
    {
        throw new RuntimeException("Stub!");
    }

    public void checkError()
            throws IOException
    {
        throw new RuntimeException("Stub!");
    }

    public String toString()
    {
        throw new RuntimeException("Stub!");
    }

    protected void finalize()
            throws Throwable
    {
        throw new RuntimeException("Stub!");
    }

    public int describeContents()
    {
        throw new RuntimeException("Stub!");
    }

    public void writeToParcel(Parcel out, int flags)
    {
        throw new RuntimeException("Stub!");
    }

    public static final Parcelable.Creator<ParcelFileDescriptor> CREATOR = null;
    public static final int MODE_APPEND = 33554432;
    public static final int MODE_CREATE = 134217728;
    public static final int MODE_READ_ONLY = 268435456;
    public static final int MODE_READ_WRITE = 805306368;
    public static final int MODE_TRUNCATE = 67108864;
    @Deprecated
    public static final int MODE_WORLD_READABLE = 1;
    @Deprecated
    public static final int MODE_WORLD_WRITEABLE = 2;
    public static final int MODE_WRITE_ONLY = 536870912;
}
