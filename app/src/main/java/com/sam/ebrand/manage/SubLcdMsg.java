package com.sam.ebrand.manage;

/**
 * Created by sam on 2016/11/15.
 */
public class SubLcdMsg {

    int bBacklightOn;
    int bDefault;
    boolean bNeedToToast;

    public SubLcdMsg(final int n, final int n2) {
        init(n, n2, false);
    }

    public SubLcdMsg(final int n, final int n2, final boolean b) {
        init(n, n2, b);
    }

    private void init(final int bBacklightOn, final int bDefault, final boolean bNeedToToast) {
        this.bBacklightOn = bBacklightOn;
        this.bDefault = bDefault;
        this.bNeedToToast = bNeedToToast;
    }
}
