package android.os;

//import android.util.Size;
//import android.util.SizeF;
//import android.util.SparseArray;
//import android.util.SparseBooleanArray;
import java.io.FileDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Parcel
{
    Parcel()
    {
        throw new RuntimeException("Stub!");
    }

    public static Parcel obtain()
    {
        throw new RuntimeException("Stub!");
    }

    public final void recycle()
    {
        throw new RuntimeException("Stub!");
    }

    public final int dataSize()
    {
        throw new RuntimeException("Stub!");
    }

    public final int dataAvail()
    {
        throw new RuntimeException("Stub!");
    }

    public final int dataPosition()
    {
        throw new RuntimeException("Stub!");
    }

    public final int dataCapacity()
    {
        throw new RuntimeException("Stub!");
    }

    public final void setDataSize(int size)
    {
        throw new RuntimeException("Stub!");
    }

    public final void setDataPosition(int pos)
    {
        throw new RuntimeException("Stub!");
    }

    public final void setDataCapacity(int size)
    {
        throw new RuntimeException("Stub!");
    }

    public final byte[] marshall()
    {
        throw new RuntimeException("Stub!");
    }

    public final void unmarshall(byte[] data, int offset, int length)
    {
        throw new RuntimeException("Stub!");
    }

    public final void appendFrom(Parcel parcel, int offset, int length)
    {
        throw new RuntimeException("Stub!");
    }

    public final boolean hasFileDescriptors()
    {
        throw new RuntimeException("Stub!");
    }

    public final void writeInterfaceToken(String interfaceName)
    {
        throw new RuntimeException("Stub!");
    }

    public final void enforceInterface(String interfaceName)
    {
        throw new RuntimeException("Stub!");
    }

    public final void writeByteArray(byte[] b)
    {
        throw new RuntimeException("Stub!");
    }

    public final void writeByteArray(byte[] b, int offset, int len)
    {
        throw new RuntimeException("Stub!");
    }

    public final void writeInt(int val)
    {
        throw new RuntimeException("Stub!");
    }

    public final void writeLong(long val)
    {
        throw new RuntimeException("Stub!");
    }

    public final void writeFloat(float val)
    {
        throw new RuntimeException("Stub!");
    }

    public final void writeDouble(double val)
    {
        throw new RuntimeException("Stub!");
    }

    public final void writeString(String val)
    {
        throw new RuntimeException("Stub!");
    }

//    public final void writeStrongBinder(IBinder val)
//    {
//        throw new RuntimeException("Stub!");
//    }
//
//    public final void writeStrongInterface(IInterface val)
//    {
//        throw new RuntimeException("Stub!");
//    }

    public final void writeFileDescriptor(FileDescriptor val)
    {
        throw new RuntimeException("Stub!");
    }

    public final void writeByte(byte val)
    {
        throw new RuntimeException("Stub!");
    }

    public final void writeMap(Map val)
    {
        throw new RuntimeException("Stub!");
    }

    public final void writeBundle(Bundle val)
    {
        throw new RuntimeException("Stub!");
    }

//    public final void writePersistableBundle(PersistableBundle val)
//    {
//        throw new RuntimeException("Stub!");
//    }

//    public final void writeSize(Size val)
//    {
//        throw new RuntimeException("Stub!");
//    }
//
//    public final void writeSizeF(SizeF val)
//    {
//        throw new RuntimeException("Stub!");
//    }

    public final void writeList(List val)
    {
        throw new RuntimeException("Stub!");
    }

    public final void writeArray(Object[] val)
    {
        throw new RuntimeException("Stub!");
    }

//    public final void writeSparseArray(SparseArray<Object> val)
//    {
//        throw new RuntimeException("Stub!");
//    }
//
//    public final void writeSparseBooleanArray(SparseBooleanArray val)
//    {
//        throw new RuntimeException("Stub!");
//    }

    public final void writeBooleanArray(boolean[] val)
    {
        throw new RuntimeException("Stub!");
    }

    public final boolean[] createBooleanArray()
    {
        throw new RuntimeException("Stub!");
    }

    public final void readBooleanArray(boolean[] val)
    {
        throw new RuntimeException("Stub!");
    }

    public final void writeCharArray(char[] val)
    {
        throw new RuntimeException("Stub!");
    }

    public final char[] createCharArray()
    {
        throw new RuntimeException("Stub!");
    }

    public final void readCharArray(char[] val)
    {
        throw new RuntimeException("Stub!");
    }

    public final void writeIntArray(int[] val)
    {
        throw new RuntimeException("Stub!");
    }

    public final int[] createIntArray()
    {
        throw new RuntimeException("Stub!");
    }

    public final void readIntArray(int[] val)
    {
        throw new RuntimeException("Stub!");
    }

    public final void writeLongArray(long[] val)
    {
        throw new RuntimeException("Stub!");
    }

    public final long[] createLongArray()
    {
        throw new RuntimeException("Stub!");
    }

    public final void readLongArray(long[] val)
    {
        throw new RuntimeException("Stub!");
    }

    public final void writeFloatArray(float[] val)
    {
        throw new RuntimeException("Stub!");
    }

    public final float[] createFloatArray()
    {
        throw new RuntimeException("Stub!");
    }

    public final void readFloatArray(float[] val)
    {
        throw new RuntimeException("Stub!");
    }

    public final void writeDoubleArray(double[] val)
    {
        throw new RuntimeException("Stub!");
    }

    public final double[] createDoubleArray()
    {
        throw new RuntimeException("Stub!");
    }

    public final void readDoubleArray(double[] val)
    {
        throw new RuntimeException("Stub!");
    }

    public final void writeStringArray(String[] val)
    {
        throw new RuntimeException("Stub!");
    }

    public final String[] createStringArray()
    {
        throw new RuntimeException("Stub!");
    }

    public final void readStringArray(String[] val)
    {
        throw new RuntimeException("Stub!");
    }

    public final void writeBinderArray(IBinder[] val)
    {
        throw new RuntimeException("Stub!");
    }

    public final IBinder[] createBinderArray()
    {
        throw new RuntimeException("Stub!");
    }

    public final void readBinderArray(IBinder[] val)
    {
        throw new RuntimeException("Stub!");
    }

    public final <T extends Parcelable> void writeTypedList(List<T> val)
    {
        throw new RuntimeException("Stub!");
    }

    public final void writeStringList(List<String> val)
    {
        throw new RuntimeException("Stub!");
    }

    public final void writeBinderList(List<IBinder> val)
    {
        throw new RuntimeException("Stub!");
    }

    public final <T extends Parcelable> void writeTypedArray(T[] val, int parcelableFlags)
    {
        throw new RuntimeException("Stub!");
    }

    public final <T extends Parcelable> void writeTypedObject(T val, int parcelableFlags)
    {
        throw new RuntimeException("Stub!");
    }

    public final void writeValue(Object v)
    {
        throw new RuntimeException("Stub!");
    }

    public final void writeParcelable(Parcelable p, int parcelableFlags)
    {
        throw new RuntimeException("Stub!");
    }

    public final void writeSerializable(Serializable s)
    {
        throw new RuntimeException("Stub!");
    }

    public final void writeException(Exception e)
    {
        throw new RuntimeException("Stub!");
    }

    public final void writeNoException()
    {
        throw new RuntimeException("Stub!");
    }

    public final void readException()
    {
        throw new RuntimeException("Stub!");
    }

    public final void readException(int code, String msg)
    {
        throw new RuntimeException("Stub!");
    }

    public final int readInt()
    {
        throw new RuntimeException("Stub!");
    }

    public final long readLong()
    {
        throw new RuntimeException("Stub!");
    }

    public final float readFloat()
    {
        throw new RuntimeException("Stub!");
    }

    public final double readDouble()
    {
        throw new RuntimeException("Stub!");
    }

    public final String readString()
    {
        throw new RuntimeException("Stub!");
    }

    public final IBinder readStrongBinder()
    {
        throw new RuntimeException("Stub!");
    }

    public final ParcelFileDescriptor readFileDescriptor()
    {
        throw new RuntimeException("Stub!");
    }

    public final byte readByte()
    {
        throw new RuntimeException("Stub!");
    }

    public final void readMap(Map outVal, ClassLoader loader)
    {
        throw new RuntimeException("Stub!");
    }

    public final void readList(List outVal, ClassLoader loader)
    {
        throw new RuntimeException("Stub!");
    }

    public final HashMap readHashMap(ClassLoader loader)
    {
        throw new RuntimeException("Stub!");
    }

    public final Bundle readBundle()
    {
        throw new RuntimeException("Stub!");
    }

    public final Bundle readBundle(ClassLoader loader)
    {
        throw new RuntimeException("Stub!");
    }

//    public final PersistableBundle readPersistableBundle()
//    {
//        throw new RuntimeException("Stub!");
//    }
//
//    public final PersistableBundle readPersistableBundle(ClassLoader loader)
//    {
//        throw new RuntimeException("Stub!");
//    }
//
//    public final Size readSize()
//    {
//        throw new RuntimeException("Stub!");
//    }
//
//    public final SizeF readSizeF()
//    {
//        throw new RuntimeException("Stub!");
//    }

    public final byte[] createByteArray()
    {
        throw new RuntimeException("Stub!");
    }

    public final void readByteArray(byte[] val)
    {
        throw new RuntimeException("Stub!");
    }

    public final ArrayList readArrayList(ClassLoader loader)
    {
        throw new RuntimeException("Stub!");
    }

    public final Object[] readArray(ClassLoader loader)
    {
        throw new RuntimeException("Stub!");
    }

//    public final SparseArray readSparseArray(ClassLoader loader)
//    {
//        throw new RuntimeException("Stub!");
//    }
//
//    public final SparseBooleanArray readSparseBooleanArray()
//    {
//        throw new RuntimeException("Stub!");
//    }

    public final <T> ArrayList<T> createTypedArrayList(Parcelable.Creator<T> c)
    {
        throw new RuntimeException("Stub!");
    }

    public final <T> void readTypedList(List<T> list, Parcelable.Creator<T> c)
    {
        throw new RuntimeException("Stub!");
    }

    public final ArrayList<String> createStringArrayList()
    {
        throw new RuntimeException("Stub!");
    }

    public final ArrayList<IBinder> createBinderArrayList()
    {
        throw new RuntimeException("Stub!");
    }

    public final void readStringList(List<String> list)
    {
        throw new RuntimeException("Stub!");
    }

    public final void readBinderList(List<IBinder> list)
    {
        throw new RuntimeException("Stub!");
    }

    public final <T> T[] createTypedArray(Parcelable.Creator<T> c)
    {
        throw new RuntimeException("Stub!");
    }

    public final <T> void readTypedArray(T[] val, Parcelable.Creator<T> c)
    {
        throw new RuntimeException("Stub!");
    }

    public final <T> T readTypedObject(Parcelable.Creator<T> c)
    {
        throw new RuntimeException("Stub!");
    }

    public final <T extends Parcelable> void writeParcelableArray(T[] value, int parcelableFlags)
    {
        throw new RuntimeException("Stub!");
    }

    public final Object readValue(ClassLoader loader)
    {
        throw new RuntimeException("Stub!");
    }

    public final <T extends Parcelable> T readParcelable(ClassLoader loader)
    {
        throw new RuntimeException("Stub!");
    }

    public final Parcelable[] readParcelableArray(ClassLoader loader)
    {
        throw new RuntimeException("Stub!");
    }

    public final Serializable readSerializable()
    {
        throw new RuntimeException("Stub!");
    }

    protected void finalize()
            throws Throwable
    {
        throw new RuntimeException("Stub!");
    }

    public static final Parcelable.Creator<String> STRING_CREATOR = null;
}
