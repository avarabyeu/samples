package com.github.avarabyeu.samples.guiceng;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import org.testng.IModuleFactory;
import org.testng.ITestContext;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @author Andrei Varabyeu
 */
public abstract class CachedModuleFactory<PM extends Module> implements IModuleFactory {

    private static final Cache<Class<? extends Module>, Injector> CACHE =
            CacheBuilder.<Class<? extends Module>, Injector>newBuilder().build();


    private static final List<Module> ADDITIONAL_MODULES = ImmutableList.<Module>builder()
            .add(new ManualScopeModule())
            .build();

    private final Callable<Injector> INJECTOR_LOADER = new Callable<Injector>() {
        @Override
        public Injector call() {
            return Guice.createInjector(Modules.combine(ImmutableList.<Module>builder().add(createParentModule()).addAll(ADDITIONAL_MODULES).build()));
        }
    };

    @Override
    final public Module createModule(ITestContext context, Class<?> testClass) {
        Module childModule = createTestModule(context, testClass);
        if (testClass.isAnnotationPresent(UseParentInjector.class)) {
            boolean cached = testClass.getAnnotation(UseParentInjector.class).cached();

            Injector injector = cached ? getCached(getParentModuleType()) : Guice.createInjector(createParentModule());
            context.addInjector(Collections.singletonList(childModule),
                    injector.createChildInjector(childModule));
            context.setAttribute("injector", injector);
        }
        return childModule;
    }

    private Injector getCached(final Class<PM> moduleType) {
        try {
            return CACHE.get(moduleType, INJECTOR_LOADER);
        } catch (ExecutionException e) {
            throw new UncheckedExecutionException(e.getCause());
        }
    }

    abstract protected PM createParentModule();

    abstract protected Class<PM> getParentModuleType();

    abstract protected Module createTestModule(ITestContext context, Class<?> testClass);


}
