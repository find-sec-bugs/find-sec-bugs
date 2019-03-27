package android.os;

public final class CancellationSignal
{
    public CancellationSignal()
    {
        throw new RuntimeException("Stub!");
    }

    public boolean isCanceled()
    {
        throw new RuntimeException("Stub!");
    }

    public void throwIfCanceled()
    {
        throw new RuntimeException("Stub!");
    }

    public void cancel()
    {
        throw new RuntimeException("Stub!");
    }

    public void setOnCancelListener(OnCancelListener listener)
    {
        throw new RuntimeException("Stub!");
    }

    public static abstract interface OnCancelListener
    {
        public abstract void onCancel();
    }
}
