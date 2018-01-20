package com.selecttvlauncher.Interface;

/**
 * Created by Ocs pl-79(17.2.2016) on 5/25/2016.
 */
public enum AppDialogUserActions {

    OK(1),

    CANCEL(2);

    private int action = -1;

    AppDialogUserActions(final int action) {

        this.action = action;
    }

    public int getAction() {

        return this.action;
    }
}
