package com.github.avarabyeu.samples;

import freemarker.cache.ClassTemplateLoader;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import org.apache.commons.lang3.time.DateUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

/**
 * @author Andrei Varabyeu
 */
public class TemplateEngine {

    private Configuration configuration;

    public TemplateEngine() {
        configuration = new Configuration(Configuration.VERSION_2_3_21);
        configuration.setTemplateLoader(new ClassTemplateLoader(this.getClass(), "/"));
        configuration.setDateTimeFormat("yyyy-mm-dd hh:mm:ss.000");

        try {
            TemplateModel templateModel = new BeansWrapperBuilder(Configuration.VERSION_2_3_21).build().getStaticModels().get(DateUtils.class.getCanonicalName());
            configuration.setSharedVariable("dateUtils", templateModel);
        } catch (TemplateModelException e) {
            throw new IllegalStateException("Unable to initialize template shared variables", e);
        }

    }

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
