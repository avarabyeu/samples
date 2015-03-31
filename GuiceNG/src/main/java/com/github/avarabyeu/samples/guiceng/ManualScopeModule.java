package com.github.avarabyeu.samples.guiceng;

import com.google.inject.AbstractModule;

/**
 * @author Andrei Varabyeu
 */
class ManualScopeModule extends AbstractModule {
    @Override
    protected void configure() {
        ManualScope manualScope = new ManualScope();
        bind(ManualScope.class).toInstance(manualScope);
        bindScope(AnnotationScope.class, manualScope);
    }
}
