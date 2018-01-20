package com.selecttvlauncher.BeanClass;

/**
 * Created by ${Madhan} on 4/21/2016.
 */
public class SpinnerBean {

    int spinnerId;
    String spinnerName;

    public SpinnerBean(int spinnerId, String spinnerName)
    {
        this.spinnerId=spinnerId;
        this.spinnerName=spinnerName;
    }


    public int getSpinnerId() {
        return spinnerId;
    }

    public void setSpinnerId(int spinnerId) {
        this.spinnerId = spinnerId;
    }

    public String getSpinnerName() {
        return spinnerName;
    }

    public void setSpinnerName(String spinnerName) {
        this.spinnerName = spinnerName;
    }
}
