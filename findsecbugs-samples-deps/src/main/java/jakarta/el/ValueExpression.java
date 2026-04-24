package jakarta.el;

public abstract class ValueExpression {

    public abstract <T> T getValue(ELContext context);

}
