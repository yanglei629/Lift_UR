package com.yanglei.LIFT.impl.installation;

import java.util.Locale;

import com.yanglei.LIFT.impl.LiftDaemonService;
import com.yanglei.LIFT.impl.Style;
import com.yanglei.LIFT.impl.V3Style;
import com.yanglei.LIFT.impl.V5Style;
import com.yanglei.LIFT.impl.i18n.CommandNamesResource;
import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.ContributionConfiguration;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.yanglei.LIFT.impl.i18n.LanguagePack;

public class LiftInstallationNodeService implements SwingInstallationNodeService<LiftInstallationNodeContribution, LiftInstallationNodeView> {
    private LiftDaemonService daemonService;

    public LiftInstallationNodeService(LiftDaemonService daemonService) {
        this.daemonService = daemonService;
    }

    @Override
    public void configureContribution(ContributionConfiguration configuration) {
    }

    @Override
    public String getTitle(Locale locale) {
        CommandNamesResource commandNames = new CommandNamesResource(locale);
        return commandNames.nodeName();
    }

    @Override
    public LiftInstallationNodeView createView(ViewAPIProvider apiProvider) {
        SystemAPI systemAPI = apiProvider.getSystemAPI();
        Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
        return new LiftInstallationNodeView(style);
    }

    @Override
    public LiftInstallationNodeContribution createInstallationNode(InstallationAPIProvider apiProvider,
                                                                   LiftInstallationNodeView view, DataModel model, CreationContext context) {
        return new LiftInstallationNodeContribution(apiProvider, model, view, daemonService);
    }
}
