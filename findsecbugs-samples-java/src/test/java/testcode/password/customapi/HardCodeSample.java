package testcode.password.customapi;

public class HardCodeSample {

    public void hardcode1() {
        Vault v = new Vault();
        v.setPassword("f1nds3cbugs");
    }

    public void hardcode2() {
        Vault v = new Vault();
        v.setMotDePasse("Soleil1234");
    }

    public void hardcode3() {
        Vault v = new Vault();
        v.setParola("Parola1");
    }

    public void hardcode4() {
        Vault v = new Vault();
        v.secretKey("1234567890");
    }

    public void falsePositive1(String pass) {
        Vault v = new Vault();
        v.setPassword(pass);
    }

    public void falsePositive2(String pass) {
        Vault v = new Vault();
        v.setMotDePasse(pass);
    }

    public void falsePositive3(String pass) {
        Vault v = new Vault();
        v.setParola(pass);
    }

    public void falsePositive4(String pass) {
        Vault v = new Vault();
        v.secretKey(pass);
    }
}
