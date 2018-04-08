package com.nerdscorner.mvp.events.mvp.interfaces.view;

import com.nerdscorner.mvp.events.mvp.interfaces.base.MvpComponent;
import com.nerdscorner.mvp.events.utils.busevents.FileCreator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ViewComponent extends MvpComponent {
    private static final String BASE_INTERFACE_VIEW_TEMPLATE = "/templates/interfaces/interface_view_template";
    private static final String BASE_VIEW_TEMPLATE = "/templates/interfaces/base_view_template";
    private static final String VIEW_TEMPLATE = "/templates/interfaces/view_template";
    private static final String VIEW_JAVA = "View.java";
    private static final String BASE_VIEW = "BaseView";
    private static final String BASE_VIEW_JAVA = "BaseView.java";
    private static final String VIEW_IMPL_JAVA = "ViewImpl.java";

    private static final String RELATIVE_PATH = File.separator + "ui" + File.separator + "mvp" + File.separator + "view";

    public ViewComponent(String basePath, String basePackage, String screenName) {
        super(BASE_INTERFACE_VIEW_TEMPLATE, screenName + VIEW_JAVA, basePath, basePackage, screenName, VIEW_TEMPLATE, screenName + VIEW_IMPL_JAVA);
    }

    @Override
    public boolean build() {
        if (baseComponentName == null || baseComponentFullName == null) {
            try {
                //Create BaseView.java
                InputStream componentTemplate = getClass().getResourceAsStream(BASE_VIEW_TEMPLATE);
                File component = new File(basePath + RELATIVE_PATH, BASE_VIEW_JAVA);
                FileCreator.createFile(componentTemplate, component, basePackage, screenName);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        baseComponentName = BASE_VIEW;
        baseComponentFullName = basePackage + ".ui.mvp.view." + BASE_VIEW;
        return super.build();
    }

    @Override
    protected String relativePath() {
        return RELATIVE_PATH;
    }

    @Override
    protected String relativeImplPath() {
        return RELATIVE_PATH + File.separator + IMPLEMENTATIONS;
    }

    @Override
    protected String relativeInterfacePath() {
        return RELATIVE_PATH + File.separator + INTERFACES;
    }
}
