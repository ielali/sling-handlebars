package com.sling.scripting.handlebars;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.context.FieldValueResolver;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.context.MethodValueResolver;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.scripting.api.AbstractSlingScriptEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.*;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * A ScriptEngine that uses <a href="http://handlebarsjs.com">Handlebars</a> templates
 * to render a Resource in HTML.
 * Created by imad.elali on 14/09/2014.
 */
public class HandlebarsScriptEngine extends AbstractSlingScriptEngine {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    public static final String STRING_OUTPUT_MODE = "com.sling.scripting.handlebars.stringOut";
    private volatile Handlebars handlebars;

    public HandlebarsScriptEngine(ScriptEngineFactory factory) {
        super(factory);
        handlebars = new Handlebars();
    }

    @Override
    public Object eval(Reader reader, ScriptContext context) throws ScriptException {
        Bindings bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);

        String scriptName = getFileName(context);
        Context handelbarsContext = Context
                .newBuilder(bindings)
                .resolver(
                        MethodValueResolver.INSTANCE,
                        MapValueResolver.INSTANCE,
                        JavaBeanValueResolver.INSTANCE,
                        FieldValueResolver.INSTANCE
                ).build();
        Writer out = context.getWriter();
        boolean outputAsString = isStringOutputMode(context);
        if (outputAsString) {
            out = new StringWriter();
        }
        try {
            Template template = handlebars.compile(new ReaderTemplateSource(scriptName, reader));
            context.getWriter().write(template.apply(handelbarsContext));
            context.getWriter().flush();
        } catch (Exception exp) {
            logger.error(String.format("Failure running %s script: [%s]", HandlebarsScriptEngineFactory.HANDLEBARS_NAME, scriptName), exp);
            throw new ScriptException(exp);
        }
        return outputAsString ? out.toString() : null;
    }

    private String getFileName(ScriptContext aCcontext) {
        Bindings bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);
        SlingScriptHelper helper = (SlingScriptHelper) bindings.get(SlingBindings.SLING);
        Object fileName;
        if (helper != null) {
            fileName = helper.getScript().getScriptResource().getPath();
        } else {
            fileName = aCcontext.getAttribute(ScriptEngine.FILENAME);
        }
        return fileName != null ? fileName.toString() : "String-Input";
    }

    private boolean isStringOutputMode(ScriptContext aCcontext) {
        Object flag = aCcontext.getAttribute(STRING_OUTPUT_MODE);
        return Boolean.TRUE.equals(flag);
    }
}
