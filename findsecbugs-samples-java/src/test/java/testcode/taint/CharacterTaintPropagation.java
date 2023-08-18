package testcode.taint;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CharacterTaintPropagation {

    private static final String SAFE_PART = "staticSafePart";

    /** This should report PATH_TRAVERSAL_IN. */
    public FileInputStream unsafeFileFromCharConcat(final char unsafePart) throws FileNotFoundException {
        return new FileInputStream("safePart" + unsafePart);
    }

    /** This should report PATH_TRAVERSAL_IN. */
    public FileInputStream unsafeFileFromChar(final char unsafePart) throws FileNotFoundException {
        return new FileInputStream(String.valueOf(unsafePart));
    }

    /** This should report PATH_TRAVERSAL_IN. */
    public FileInputStream unsafeFileFromCharToString(final char unsafePart) throws FileNotFoundException {
        return new FileInputStream(Character.toString(unsafePart));
    }

    /** This should report PATH_TRAVERSAL_IN. */
    public FileInputStream unsafeFileFromCharGetName(final int unsafePart) throws FileNotFoundException {
        return new FileInputStream(Character.getName(unsafePart));
    }

    /** This should report PATH_TRAVERSAL_IN. */
    public FileInputStream unsafeFileFromCharStringBuilder(final char unsafePart) throws FileNotFoundException {
        return new FileInputStream(new StringBuilder("safePart").append(unsafePart).toString());
    }

    /** This should report PATH_TRAVERSAL_IN. */
    public FileInputStream unsafeFileFromCharStringBuffer(final char unsafePart) throws FileNotFoundException {
        return new FileInputStream(new StringBuffer("safePart").append(unsafePart).toString());
    }

    /** This should report PATH_TRAVERSAL_IN. */
    public FileInputStream unsafeFileFromCharacterToString(final Character unsafePart) throws FileNotFoundException {
        return new FileInputStream(unsafePart.toString());
    }

    /** This should report PATH_TRAVERSAL_IN. */
    public FileInputStream unsafeFileFromCharacterConcat(final Character unsafePart) throws FileNotFoundException {
        return new FileInputStream("safePart" + unsafePart);
    }

    /** This should report PATH_TRAVERSAL_IN. */
    public FileInputStream unsafeFileFromString(final String unsafePart) throws FileNotFoundException {
        return new FileInputStream("safePart" + unsafePart);
    }

    public FileInputStream safeFileFromConstant() throws FileNotFoundException {
        return new FileInputStream(SAFE_PART);
    }

    public FileInputStream safeFileFromConstantWithInt(final int safeInt) throws FileNotFoundException {
        return new FileInputStream(new StringBuilder(SAFE_PART).append(safeInt).toString());
    }
}
