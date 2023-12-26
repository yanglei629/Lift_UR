package com.yanglei.LIFT.impl;

import com.ur.urcap.api.domain.system.localization.Localization;
import com.yanglei.LIFT.impl.i18n.LanguagePack;
import com.yanglei.LIFT.impl.installation.LiftInstallationNodeService;
import com.yanglei.LIFT.impl.program.LiftProgramNodeService;
import com.ur.urcap.api.contribution.DaemonService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import org.osgi.framework.ServiceReference;

import java.util.Locale;


/**
 * Hello world activator for the OSGi bundle URCAPS contribution
 */
public class Activator implements BundleActivator {
    private LiftDaemonService daemonService;

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        System.out.println("Lift urCap says Hello !");
        // program node
        LiftProgramNodeService liftProgramNodeService = new LiftProgramNodeService();
        bundleContext.registerService(SwingProgramNodeService.class, liftProgramNodeService, null);
        //bundleContext.registerService(SwingProgramNodeService.class, new LiftProgramNodeService(), null);

        // install node
        daemonService = new LiftDaemonService();
        LiftInstallationNodeService installationNodeService = new LiftInstallationNodeService(daemonService);

        bundleContext.registerService(SwingInstallationNodeService.class, installationNodeService, null);
        bundleContext.registerService(DaemonService.class, daemonService, null);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        daemonService.getDaemon().stop();
        System.out.println("Daemon Status Exit: " + daemonService.getDaemon().getState());
        System.out.println("Backyard Lift urCap  says Goodbye !");
    }
}

