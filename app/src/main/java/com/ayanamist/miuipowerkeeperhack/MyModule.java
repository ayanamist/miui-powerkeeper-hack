package com.ayanamist.miuipowerkeeperhack;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MyModule implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedHelpers.findAndHookMethod("com.miui.internal.os.Native", lpparam.classLoader, "getPropertyNative", String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (!"ro.product.mod_device".equals(param.args[0])) {
                    return;
                }
                String res = (String) param.getResult();
                if (res != null && !res.endsWith("_global")) {
                    res += "_global";
                    param.setResult(res);
                }
            }
        });
        XposedBridge.log("hook complete");
    }
}
