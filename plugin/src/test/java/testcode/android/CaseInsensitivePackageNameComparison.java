package testcode.android;
import android.app.Activity;
public class CaseInsensitivePackageNameComparison extends Activity{

	public boolean isTrusted(String paramString) {
 		if (this.getApplicationContext().getPackageName().equalsIgnoreCase(paramString)) {
 		return true;
	}
    }

}

