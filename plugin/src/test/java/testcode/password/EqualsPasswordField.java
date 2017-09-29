package testcode.password;

public abstract class EqualsPasswordField {

    public boolean hardcodedLogin1(String username, String password) {

        if(username.equals("admin")) {
            System.out.println("OK");
        }
        if(username.equals("abc")) {
            System.out.println("OK");
        }

        String test=username; //This code block is only there to make sure the stack is properly tracked..
        String abc=test+"def";
        String test2="abcdef";
        String abc2=test2+"def";

        if(password.equals("@dm1n")) { //!!
            return true;
        }

        return validateDb(username,password);
    }

    public boolean hardcodedLogin2(String username, String password) {

        if("adm1nL3ft".equals(password)) { //!!
            return true;
        }

        return validateDb(username,password);
    }


    public boolean hardcodedLogin3(String username, String p1) {

        String password = p1;
        if("adm1nL3ft!!!!".equals(password)) { //!! (Not supported at the moment)
            return true;
        }

        return validateDb(username,password);
    }

    public boolean safeLogin1(String username, String password) {

        if(password.equals("")) {
            return true;
        }

        return validateDb(username,password);
    }

    public boolean safeLogin2(String username, String password) {

        if("".equals(password)) {
            return false;
        }

        return validateDb(username,password);
    }

    public boolean safeLogin3(String username, String password) {

        if(getPassword(username).equals(password)) {
            return false;
        }

        return validateDb(username,password);
    }

    public boolean safeLogin4(String username, String password) {

        if(password.equals(getPassword(username))) {
            return false;
        }

        return validateDb(username,password);
    }


    public abstract boolean validateDb(String username, String password);
    public abstract String getPassword(String username);
}
