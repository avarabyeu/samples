package com.github.avarabyeu.samples.jashing;

import com.github.avarabyeu.jashing.core.JashingServlet;
import com.github.avarabyeu.jashing.integration.vcs.CompositeVcsModule;
import com.github.avarabyeu.jashing.integration.vcs.git.GitModule;
import com.github.avarabyeu.jashing.integration.vcs.svn.SvnModule;
import com.google.inject.Module;

import javax.servlet.annotation.WebServlet;

/**
 * Created by andrey.vorobyov on 2/24/15.
 */
@WebServlet("/*")
//@WebFilter("/*")
public class JashingServletFilter extends JashingServlet {


    @Override
    public Module[] getModules() {
        return new Module[]{new CompositeVcsModule(new SvnModule(), new GitModule())};
    }


}
