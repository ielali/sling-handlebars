package com.sling.handlebars.script;

import com.github.jknack.handlebars.io.AbstractTemplateSource;

import java.io.IOException;
import java.io.Reader;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * An {@link java.net.URL} {@link com.github.jknack.handlebars.io.TemplateSource}.
 *
 * @author edgar.espina
 * @since 0.11.0
 */
public class ReaderTemplateSource extends AbstractTemplateSource {

    /**
     * The resource. Required.
     */
    private Reader reader;

    /**
     * The file's name.
     */
    private String filename;

    /**
     * Creates a new {@link com.sling.handlebars.script.ReaderTemplateSource}.
     *
     * @param filename The file's name.
     * @param reader   The reader. Required.
     */
    public ReaderTemplateSource(final String filename, final Reader reader) {
        this.filename = notEmpty(filename, "The filename is required.");
        this.reader = notNull(reader, "A reader is required.");
    }

    @Override
    public String content() throws IOException {
        final int bufferSize = 1024;
        try {
            char[] cbuf = new char[bufferSize];
            StringBuilder sb = new StringBuilder(bufferSize);
            int len;
            while ((len = reader.read(cbuf, 0, bufferSize)) != -1) {
                sb.append(cbuf, 0, len);
            }
            return sb.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    @Override
    public String filename() {
        return filename;
    }

    @Override
    public long lastModified() {
        return 0;
    }

}
