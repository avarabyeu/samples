package com.github.avarabyeu.samples.guiceng;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.inject.Named;
import com.google.inject.Provider;


/**
 * Created by andrey.vorobyov on 3/18/15.
 */
@org.testng.annotations.Guice(moduleFactory = InjectionTest.ModuleFactory.class)
@UseParentInjector
public class InjectionDemoTest2 {

    @Inject
    @Named("child")
    private Provider<InjectionTest.TestObject> childObject;

    @Inject
    @Named("parent")
    private Provider<InjectionTest.TestObject> parentObject;

    @Inject
    private Injector injector;

    @Test
    public void executeSomeTest() {
        System.out.println(childObject.get() + " Thread: " + Thread.currentThread().getName());
        System.out.println(parentObject.get() + " Thread: " + Thread.currentThread().getId());

        System.out.println(injector.getInstance(Key.get(InjectionTest.TestObject.class, Names.named("parent"))));
    }


}
