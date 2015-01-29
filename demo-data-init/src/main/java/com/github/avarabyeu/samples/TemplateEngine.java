package com.github.avarabyeu.samples;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

/**
 * @author Andrei Varabyeu
 */
class TemplateEngine {

    private Configuration configuration;

    public TemplateEngine(Configuration freemarkerConfig) {
        this.configuration = freemarkerConfig;

    }

    /* this method might be extracted into separate interface if you gonna use template merge it in tests */
    public byte[] merge(String template, Map<?, ?> properties) {
        Writer out = null;
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            out = new OutputStreamWriter(byteStream);
            configuration.getTemplate(template).process(properties, out);
            return byteStream.toByteArray();
        } catch (TemplateException e) {
            throw new IllegalArgumentException("Unable to merge template with name " + template, e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to find template with name " + template, e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    //do nothing
                }
            }
        }

    }
}
