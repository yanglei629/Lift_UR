package com.yanglei.LIFT.impl;

import com.yanglei.LIFT.impl.installation.LiftInstallationNodeService;
import com.yanglei.LIFT.impl.program.LiftProgramNodeService;
import com.ur.urcap.api.contribution.DaemonService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;


/**
 * Hello world activator for the OSGi bundle URCAPS contribution
 */
public class Activator implements BundleActivator {
    private LiftDaemonService daemonService;

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        System.out.println("Backyard Lift urCap says Hello !");

        bundleContext.registerService(SwingProgramNodeService.class, new LiftProgramNodeService(), null);

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

