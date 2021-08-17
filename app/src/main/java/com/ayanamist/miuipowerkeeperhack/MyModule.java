package com.ayanamist.miuipowerkeeperhack;

import java.util.Locale;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MyModule implements IXposedHookLoadPackage {

    public static final String KEY_DEVICE = "ro.product.mod_device";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedHelpers.findAndHookMethod("com.miui.internal.os.Native", lpparam.classLoader, "getPropertyNative", String.class, new MyHook("com.miui.internal.os.Native.getPropertyNative"));
        XposedHelpers.findAndHookMethod("miui.os.SystemProperties", lpparam.classLoader, "get", String.class, new MyHook("miui.os.SystemProperties.get(String)"));
        XposedHelpers.findAndHookMethod("miui.os.SystemProperties", lpparam.classLoader, "get", String.class, String.class, new MyHook("miui.os.SystemProperties.get(String, String)"));
        XposedBridge.log("hook complete");
    }

    private static class MyHook extends XC_MethodHook {
        private final String contextName;

        private MyHook(String contextName) {
            this.contextName = contextName;
        }

        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            if (!KEY_DEVICE.equals(param.args[0])) {
                return;
            }
            String res = (String) param.getResult();
            if (res != null && !res.endsWith("_global")) {
                res += "_global";
                param.setResult(res);
                XposedBridge.log(String.format(Locale.ENGLISH, "%s %s replaced to %s", contextName, KEY_DEVICE, res));
            }
        }
    }
}
