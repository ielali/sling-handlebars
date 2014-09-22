
package com.sling.scripting.handlebars;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.scripting.api.AbstractScriptEngineFactory;

import javax.script.ScriptEngine;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component(
        metatype = false,
        label="Apache Sling Handlebars Engine Factory",
        description="Handlebars engine based on Jknack")
@Service(value=javax.script.ScriptEngineFactory.class)
public class HandlebarsScriptEngineFactory extends AbstractScriptEngineFactory {
    /**
     * The extensions of Handlebars scripts (value is "hbs").
     */
    public final static String HANDLEBARS_SCRIPT_EXTENSION = "hbs";

    /**
     * The MIME type of Handlebars script files (value is "text/x-handlebars-template").
     */
    public final static String HANDLEBARS_MIME_TYPE = "text/x-handlebars-template";

    /**
     * The short name of the Handlebars script engine factory (value is
     * "freemarker").
     */
    public final static String SHORT_NAME = "handlebars";

    /**
     * The name of the Handlebars language (value is "Handlebars").
     */
    public static final String HANDLEBARS_NAME = "Handlebars";

    /**
     * The absolute path to the Handlebars version properties file (value is
     * "/freemarker/version.properties").
     */
    private static final String HANDLEBARS_VERSION_PROPERTIES = "/META-INF/maven/com.github.jknack/handlebars/pom.properties";

    /**
     * The name of the property containing the Handlebars version (value is
     * "version").
     */
    private static final String PROP_HANDLEBARS_VERSION = "version";

    /**
     * The default version of Handlebars if the version property cannot be read
     * (value is "2.3", which is the latest minor version release as of
     * 17.Dec.2007).
     */
    private static final String DEFAULT_HANDLEBARS_VERSION = "1.3.0";

    /**
     * The Handlebars language version extracted from the Handlebars version
     * properties file. If this file cannot be read the language version
     * defaults to ...
     */
    private final String languageVersion;

    public HandlebarsScriptEngineFactory() {
        setExtensions(HANDLEBARS_SCRIPT_EXTENSION);
        setNames(HANDLEBARS_NAME, SHORT_NAME);
        setMimeTypes(HANDLEBARS_MIME_TYPE);
        // extract language version from version.properties file
        String langVersion = null;
        InputStream ins = null;
        try {
            ins = getClass().getResourceAsStream(HANDLEBARS_VERSION_PROPERTIES);
            if (ins != null) {
                Properties props = new Properties();
                props.load(ins);
                langVersion = props.getProperty(PROP_HANDLEBARS_VERSION);
            }
        } catch (IOException ioe) {
            // don't really care, just use default
        } finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException ignore) {
                    // ignore
                }
            }
        }

        // if we could not extract the actual version, assume version 2.3
        // which is the current minor release as of 17.Dec.2007
        languageVersion = (langVersion == null)
                ? DEFAULT_HANDLEBARS_VERSION
                : langVersion;
    }

    public String getLanguageName() {
        return HANDLEBARS_NAME;
    }

    public String getLanguageVersion() {
        return languageVersion;
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
        return new HandlebarsScriptEngine(this);
    }
}
