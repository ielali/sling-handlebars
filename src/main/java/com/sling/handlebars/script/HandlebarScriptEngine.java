package com.sling.handlebars.script;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.context.FieldValueResolver;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.context.MethodValueResolver;

import javax.script.*;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by imad.elali on 14/09/2014.
 */
public class HandlebarScriptEngine extends AbstractScriptEngine {
    public static final String STRING_OUTPUT_MODE = "com.sling.handlebars.script.stringOut";
    public static final String TEMPLATE_DIR = "com.sling.handlebars.script.template.dir";
    private volatile ScriptEngineFactory factory;
    private volatile Handlebars handlebars;

    public HandlebarScriptEngine() {
        this(null);
    }

    public HandlebarScriptEngine(ScriptEngineFactory factory) {
        this.factory = factory;
        handlebars = new Handlebars();
    }

    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {
        return eval(new StringReader(script), context);
    }

    @Override
    public Object eval(Reader reader, ScriptContext context) throws ScriptException {
        Writer out = context.getWriter();
        String fileName = getFilename(context);
        boolean outputAsString = isStringOutputMode(context);
        if (outputAsString) {
            out = new StringWriter();
        }
        Bindings engineScope = context.getBindings(ScriptContext.ENGINE_SCOPE);
        Context handelbarsContext = Context
                .newBuilder(engineScope)
                .resolver(
                        MethodValueResolver.INSTANCE,
                        MapValueResolver.INSTANCE,
                        JavaBeanValueResolver.INSTANCE,
                        FieldValueResolver.INSTANCE
                ).build();

        try {
            Template template = handlebars.compile(new ReaderTemplateSource(fileName,reader));
            out.write(template.apply(handelbarsContext));
            out.flush();
        } catch (Exception exp) {
            throw new ScriptException(exp);
        }
        return outputAsString ? out.toString() : null;
    }

    @Override
    public Bindings createBindings() {
        return new SimpleBindings();
    }

    @Override
    public ScriptEngineFactory getFactory() {
        if (factory == null) {
            synchronized (this) {
                if (factory == null) {
                    factory = new HandlebarsScriptEngineFactory();
                }
            }
        }
        return factory;
    }

    void setFactory(ScriptEngineFactory factory) {
        this.factory = factory;
    }

    private String getFilename(ScriptContext aCcontext) {
        Object fileName = aCcontext.getAttribute(ScriptEngine.FILENAME);
        return fileName != null ? fileName.toString() : "String-Input";
    }

    private boolean isStringOutputMode(ScriptContext aCcontext) {
        Object flag = aCcontext.getAttribute(STRING_OUTPUT_MODE);
        return Boolean.TRUE.equals(flag);
    }
}
