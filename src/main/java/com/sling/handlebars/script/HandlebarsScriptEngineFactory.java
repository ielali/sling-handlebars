
package com.sling.handlebars.script;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HandlebarsScriptEngineFactory implements ScriptEngineFactory {
    public String getEngineName() {
        return "Handlebars";
    }

    public String getEngineVersion() {
        return "0.1";
    }

    public List<String> getExtensions() {
        return extensions;
    }

    public String getLanguageName() {
        return "Handlebars";
    }

    public String getLanguageVersion() {
        return "1.3.0";
    }

    public String getMethodCallSyntax(String obj, String m, String... args) {
        //Calling with arguments is not supported in Handlebars
        StringBuilder buf = new StringBuilder();
        buf.append("{{");
        buf.append(obj);
        buf.append(".");
        buf.append(m);
        buf.append("}}");
        return buf.toString();
    }

    public List<String> getMimeTypes() {
        return mimeTypes;
    }

    public List<String> getNames() {
        return names;
    }

    public String getOutputStatement(String toDisplay) {
        StringBuilder buf = new StringBuilder();
        buf.append("{{log \"");
        int len = toDisplay.length();
        for (int i = 0; i < len; i++) {
            char ch = toDisplay.charAt(i);
            switch (ch) {
                case '"':
                    buf.append("\\\"");
                    break;
                case '\\':
                    buf.append("\\\\");
                    break;
                default:
                    buf.append(ch);
                    break;
            }
        }
        buf.append("\"}}");
        return buf.toString();
    }

    public String getParameter(String key) {
        if (key.equals(ScriptEngine.ENGINE)) {
            return getEngineName();
        } else if (key.equals(ScriptEngine.ENGINE_VERSION)) {
            return getEngineVersion();
        } else if (key.equals(ScriptEngine.NAME)) {
            return getEngineName();
        } else if (key.equals(ScriptEngine.LANGUAGE)) {
            return getLanguageName();
        } else if (key.equals(ScriptEngine.LANGUAGE_VERSION)) {
            return getLanguageVersion();
        } else if (key.equals("THREADING")) {
            return "MULTITHREADED";
        } else {
            return null;
        }
    }

    public String getProgram(String... statements) {
        StringBuilder buf = new StringBuilder();
        if (statements.length != 0) {
            int i = 0;
            for (; i < statements.length - 1; i++) {
                buf.append(statements[i]);
                buf.append(", ");
            }
            buf.append(statements[i]);
        }
        return buf.toString();
    }

    public ScriptEngine getScriptEngine() {
        HandlebarScriptEngine engine = new HandlebarScriptEngine();
        engine.setFactory(this);
        return engine;
    }

    private static List<String> names;
    private static List<String> extensions;
    private static List<String> mimeTypes;

    static {
        names = Collections.unmodifiableList(Arrays.asList("Handlebars","handlebars"));
        extensions = Collections.unmodifiableList(Arrays.asList("hbs"));
        mimeTypes = Collections.unmodifiableList(Arrays.asList("text/x-handlebars-template"));
    }
}
