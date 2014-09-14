package com.sling.handlebars.script;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.context.FieldValueResolver;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.context.MethodValueResolver;
import org.junit.Test;

import javax.script.*;

import static org.junit.Assert.assertEquals;

public class HandlebarScriptEngineTest {

    @Test
    public void testHandleBars() throws Exception {
        Handlebars handlebars = new Handlebars();
        Context context = Context
                .newBuilder(new com.sling.handlebars.script.Bean())
                .resolver(
                        MethodValueResolver.INSTANCE,
                        MapValueResolver.INSTANCE,
                        JavaBeanValueResolver.INSTANCE,
                        FieldValueResolver.INSTANCE
                ).build();

        Template template = handlebars.compileInline("Hello {{this.printFull}}!");

        assertEquals("Hello Mr: FirstName LastName age: 30!", template.apply(context));
    }

    @Test
    public void testEngine() throws Exception {
        ScriptEngineManager factory = new ScriptEngineManager();
        // create a JavaScript engine
        ScriptEngine engine = factory.getEngineByName("Handlebars");
        ScriptContext context=new SimpleScriptContext();
        // evaluate JavaScript code from String
        SimpleBindings bindings = new SimpleBindings();
        bindings.put("context", new Bean());
        bindings.put(HandlebarScriptEngine.STRING_OUTPUT_MODE,Boolean.TRUE);
        context.setBindings(bindings,ScriptContext.ENGINE_SCOPE);
        assertEquals("Hello Mr: FirstName LastName age: 30!", engine.eval("Hello {{context.printFull}}!", context));
    }
}