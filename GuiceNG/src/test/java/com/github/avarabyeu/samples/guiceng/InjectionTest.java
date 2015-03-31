package com.github.avarabyeu.samples.guiceng;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.name.Names;
import org.testng.ITestContext;
import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.Collections;

/**
 * Created by andrey.vorobyov on 3/18/15.
 */

public class InjectionTest {


    @Test
    public void runDemoTests() {
        TestNG ng = new TestNG();

        XmlSuite suite = new XmlSuite();


        XmlTest test = new XmlTest();
        XmlClass clazz = new XmlClass(InjectionDemoTest.class.getName());
        test.setClasses(Collections.singletonList(clazz));
        test.setName(clazz.getName());
        test.setSuite(suite);
        suite.addTest(test);

        XmlTest test2 = new XmlTest();
        XmlClass clazz2 = new XmlClass(InjectionDemoTest2.class.getName());
        test2.setClasses(Collections.singletonList(clazz2));
        test2.setSuite(suite);
        test2.setName(clazz2.getName());
        suite.addTest(test2);

        suite.setParallel("classes");
        suite.setThreadCount(2);
        ng.setXmlSuites(Collections.singletonList(suite));

//        ng.setTestClasses(new Class[]{InjectionDemoTest.class, InjectionDemoTest2.class});
        ng.addListener(new GuiceScopeListener());
//        ng.setThreadCount(2);
//        ng.setParallel("instances");
        ng.run();
    }


    public static class ModuleFactory extends CachedModuleFactory<AbstractModule> {


        @Override
        protected AbstractModule createParentModule() {
            return new AbstractModule() {
                @Override
                protected void configure() {
                    bind(TestObject.class).annotatedWith(Names.named("parent")).toProvider(new Provider<TestObject>() {
                        @Override
                        public TestObject get() {
                            System.out.println("Obtaining from thread... " + Thread.currentThread().getId());
                            return new TestObject("this is parent!");
                        }
                    }).in(AnnotationScope.class);
                }
            };
        }

        @Override
        protected Class<AbstractModule> getParentModuleType() {
            return AbstractModule.class;
        }

        @Override
        protected Module createTestModule(ITestContext context, Class<?> testClass) {
            return new ChildModule();
        }


    }

    public static class ChildModule extends AbstractModule {

        @Override
        protected void configure() {
            bind(TestObject.class).annotatedWith(Names.named("child")).toProvider(new Provider<TestObject>() {
                @Override
                public TestObject get() {
                    return new TestObject("this is child!");
                }
            }).in(AnnotationScope.class);
        }
    }

    public static class TestObject {
        private String text;

        public TestObject(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        @Override
        public String toString() {
            return text + super.toString();
        }
    }

}
