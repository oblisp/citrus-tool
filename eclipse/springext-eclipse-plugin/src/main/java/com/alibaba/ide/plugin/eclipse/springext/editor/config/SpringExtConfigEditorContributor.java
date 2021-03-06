package com.alibaba.ide.plugin.eclipse.springext.editor.config;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;

import com.alibaba.ide.plugin.eclipse.springext.SpringExtPlugin;
import com.alibaba.ide.plugin.eclipse.springext.editor.config.namespace.dom.DomDocumentUtil;

public class SpringExtConfigEditorContributor extends MultiPageEditorActionBarContributor {
    private final static String MENU_ID = "springext";
    private final static String GROUP_ID = SpringExtConfigEditor.EDITOR_ID;
    private final CleanupUnusedNamespacesAction cleanupUnusedNamespacesAction = new CleanupUnusedNamespacesAction();
    private final UpgradeToUnqualifiedStyleAction upgradeToUnqualifiedStyleAction = new UpgradeToUnqualifiedStyleAction();
    private SpringExtConfigData config;

    private IContextService contextService;
    private IContextActivation cleanupUnusedNamespacesContextActivation;
    private IHandlerService handlerService;
    private IHandlerActivation cleanupUnusedNamespacesHandlerActivation;

    @Override
    public void init(IActionBars bars) {
        super.init(bars);

        handlerService = (IHandlerService) getPage().getWorkbenchWindow().getService(IHandlerService.class);
        cleanupUnusedNamespacesHandlerActivation = handlerService
                .activateHandler(cleanupUnusedNamespacesAction.getActionDefinitionId(), new ActionHandler(
                        cleanupUnusedNamespacesAction));

        contextService = (IContextService) PlatformUI.getWorkbench().getService(IContextService.class);
        cleanupUnusedNamespacesContextActivation = contextService.activateContext(SpringExtConfigEditor.class.getName()
                + ".scope");
    }

    @Override
    public void dispose() {
        if (handlerService != null && cleanupUnusedNamespacesHandlerActivation != null) {
            handlerService.deactivateHandler(cleanupUnusedNamespacesHandlerActivation);
        }

        if (contextService != null && cleanupUnusedNamespacesContextActivation != null) {
            contextService.deactivateContext(cleanupUnusedNamespacesContextActivation);
        }
    }

    @Override
    public void contributeToToolBar(IToolBarManager toolBarManager) {
        toolBarManager.add(new GroupMarker(GROUP_ID));
        toolBarManager.appendToGroup(GROUP_ID, cleanupUnusedNamespacesAction);
        toolBarManager.appendToGroup(GROUP_ID, new Separator());
        toolBarManager.appendToGroup(GROUP_ID, upgradeToUnqualifiedStyleAction);
    }

    @Override
    public void contributeToMenu(IMenuManager menuManager) {
        IMenuManager springExtConfigEditorMenu = new MenuManager("SpringExt", MENU_ID);
        menuManager.insertBefore("window", springExtConfigEditorMenu);

        springExtConfigEditorMenu.add(cleanupUnusedNamespacesAction);
        springExtConfigEditorMenu.add(new Separator());
        springExtConfigEditorMenu.add(upgradeToUnqualifiedStyleAction);
    }

    @Override
    public void setActiveEditor(IEditorPart part) {
        if (part instanceof SpringExtConfigEditor) {
            config = ((SpringExtConfigEditor) part).getData();
        }

        super.setActiveEditor(part);
    }

    @Override
    public void setActivePage(IEditorPart activeEditor) {
        cleanupUnusedNamespacesAction.setEnabled(true);
    }

    private class CleanupUnusedNamespacesAction extends Action {
        public CleanupUnusedNamespacesAction() {
            super("Cleanup Unused Namespaces", IAction.AS_PUSH_BUTTON);
            setImageDescriptor(SpringExtPlugin.getDefault().getImageRegistry().getDescriptor("clear-ns"));
            setActionDefinitionId("com.alibaba.ide.plugin.eclipse.springext.commands.cleanupUnusedNamespaces");
        }

        @Override
        public void run() {
            if (config != null) {
                DomDocumentUtil.cleanupUnusedNamespaceDefinitions(config);
                config.forceRefreshPages();
            }
        }
    }

    private class UpgradeToUnqualifiedStyleAction extends Action {
        public UpgradeToUnqualifiedStyleAction() {
            super("Upgrade to Webx 3.2.x Format", IAction.AS_PUSH_BUTTON);
            setImageDescriptor(SpringExtPlugin.getDefault().getImageRegistry().getDescriptor("upgrade32"));
        }

        @Override
        public void run() {
            if (config != null
                    && MessageDialog.openConfirm(Display.getDefault().getActiveShell(),
                            "Upgrading to Webx 3.2.x configuration format",
                            "Webx 3.2.x has a slightly different configuration file format, "
                                    + "which is not compatible to its previous versions.  "
                                    + "If you decided to upgrade to Webx 3.2.x or newer versions, "
                                    + "you should also upgrade all your configuration files to the new format.\n\n"
                                    + "Are you sure you want to do the upgrading on file \""
                                    + config.getEditingFile().getName() + "\"?\n\n" + "(This action is UNDO-able)")) {
                DomDocumentUtil.upgradeToUnqualifiedStyle(config);
                config.forceRefreshPages();
            }
        }
    }
}
