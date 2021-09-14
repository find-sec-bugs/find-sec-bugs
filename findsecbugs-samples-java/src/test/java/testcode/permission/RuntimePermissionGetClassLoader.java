package testcode.permission;

import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.SecureClassLoader;

public class RuntimePermissionGetClassLoader extends SecureClassLoader {
    @Override
    protected PermissionCollection getPermissions(CodeSource cs) {
        PermissionCollection pc = super.getPermissions(cs);
        pc.add(new RuntimePermission("getClassLoader"));
        // Other permissions
        return pc;
    }
}
