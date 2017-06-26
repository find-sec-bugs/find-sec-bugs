package testcode.sqli.android;

public class AndroidContentProviderUsage {

    public void detect(LocalProvider localProvider, String input) {
        localProvider.query(null, null, input, null, null);
        localProvider.query(null, null, input, null, null,null);
        localProvider.delete(null, input, null);
        localProvider.update(null, null, input, null);
    }
}
