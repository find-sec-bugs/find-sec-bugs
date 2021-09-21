package testcode;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class PossibleUntrustedObjectInjection {
    public RandomAccessFile badOpenFile(final File f) {
        askUserPermission(f.getPath());

        return (RandomAccessFile)AccessController.doPrivileged(new PrivilegedAction <Object>() {
            public Object run() {
                try (RandomAccessFile rf = new RandomAccessFile(f, f.getPath())) {
                    return rf;
                } catch (IOException e) {
                    return null;
                }
            }
        });
    }

    public RandomAccessFile goodOpenFile(final File f) {
        final java.io.File copy = new java.io.File(f.getPath());
        askUserPermission(copy.getPath());

        return (RandomAccessFile)AccessController.doPrivileged(new PrivilegedAction <Object>() {
            public Object run() {
                try (RandomAccessFile rf = new RandomAccessFile(copy, copy.getPath())) {
                    return rf;
                } catch (IOException e) {
                    return null;
                }
            }
        });
    }

    public RandomAccessFile badOpenFile2() {
        final File f = getSuspiciousFile();
        askUserPermission(f.getPath());

        return (RandomAccessFile)AccessController.doPrivileged(new PrivilegedAction <Object>() {
            public Object run() {
                try (RandomAccessFile rf = new RandomAccessFile(f, f.getPath())) {
                    return rf;
                } catch (IOException e) {
                    return null;
                }
            }
        });
    }

    public RandomAccessFile goodOpenFile2() {
        final File f = getSuspiciousFile();

        final File copy = new File(f.getPath());
        askUserPermission(copy.getPath());

        return (RandomAccessFile)AccessController.doPrivileged(new PrivilegedAction <Object>() {
            public Object run() {
                try (RandomAccessFile rf = new RandomAccessFile(copy, copy.getPath())) {
                    return rf;
                } catch (IOException e) {
                    return null;
                }
            }
        });
    }

    private File getSuspiciousFile() {
        return new File("/etc/passwd") {
            private int count;
            public String getPath() {
                return (++count == 1) ? "/tmp/foo" : "/etc/passwd";
            }
        };
    }

    private void askUserPermission(String path) throws SecurityException {
        // Asking user permissions
    }
}
