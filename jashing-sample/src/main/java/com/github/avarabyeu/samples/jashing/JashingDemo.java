package com.github.avarabyeu.samples.jashing;

import com.github.avarabyeu.jashing.core.Jashing;
import com.github.avarabyeu.jashing.integration.vcs.CompositeVcsModule;
import com.github.avarabyeu.jashing.integration.vcs.git.GitModule;
import com.github.avarabyeu.jashing.integration.vcs.svn.SvnModule;

/**
 * @author Andrei Varabyeu
 */
public class JashingDemo {

    public static void main(String... args) {
        Jashing.builder().withPort(40004).registerModule(new CompositeVcsModule(new SvnModule(), new GitModule())).build().bootstrap();
    }
}
