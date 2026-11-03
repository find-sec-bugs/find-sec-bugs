package testcode.script;

import jakarta.el.ELContext;
import jakarta.el.ExpressionFactory;
import jakarta.el.ValueExpression;
import jakarta.faces.context.FacesContext;

public class JakartaElExpressionSample {

    public void unsafeEL(String expression) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
        ELContext elContext = context.getELContext();
        ValueExpression vex = expressionFactory.createValueExpression(elContext, expression, String.class);
        String result = (String) vex.getValue(elContext);
        System.out.println(result);
    }

    public void safeEL() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
        ELContext elContext = context.getELContext();
        ValueExpression vex = expressionFactory.createValueExpression(elContext, "1+1", String.class);
        String result = (String) vex.getValue(elContext);
        System.out.println(result);
    }

    public void unsafeELMethod(ELContext elContext, ExpressionFactory expressionFactory, String expression) {
        expressionFactory.createMethodExpression(elContext, expression, String.class, new Class[]{Integer.class});
    }

    public void safeELMethod(ELContext elContext, ExpressionFactory expressionFactory) {
        expressionFactory.createMethodExpression(elContext, "1+1", String.class, new Class[]{Integer.class});
    }
}
