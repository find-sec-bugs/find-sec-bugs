package testcode.script;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ScriptEngineSample {



    public static void scripting(String userInput) throws ScriptException {

        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByExtension("js");

        Object result = scriptEngine.eval("test=1;" + userInput);

    }

    //The potential injection will require manual review of the code flow but some false positive can be avoid.
    public static void scriptingSafe() throws ScriptException {

        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByExtension("js");

        String code = "var test=3;test=test*2;";
        Object result = scriptEngine.eval(code);
    }
}
