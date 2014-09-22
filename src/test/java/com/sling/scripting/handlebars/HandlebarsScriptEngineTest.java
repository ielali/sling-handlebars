package com.sling.scripting.handlebars;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.context.FieldValueResolver;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.context.MethodValueResolver;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HandlebarsScriptEngineTest extends RepositoryTestBase {

    @Test
    public void testHandleBars() throws Exception {
        Handlebars handlebars = new Handlebars();
        Context context = Context
                .newBuilder(new Bean())
                .resolver(
                        MethodValueResolver.INSTANCE,
                        MapValueResolver.INSTANCE,
                        JavaBeanValueResolver.INSTANCE,
                        FieldValueResolver.INSTANCE
                ).build();

        Template template = handlebars.compileInline("Hello {{this.printFull}}!");

        assertEquals("Hello Mr: FirstName LastName age: 30!", template.apply(context));
    }

}