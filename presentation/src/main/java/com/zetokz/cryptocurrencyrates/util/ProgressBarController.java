package com.zetokz.cryptocurrencyrates.util;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;

import com.zetokz.cryptocurrencyrates.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import kotlin.Deprecated;

/**
 * This class used to show progress bar for asynchronous requests
 */
@Deprecated(message = "Use simple progress dialog")
public class ProgressBarController {

    private static final String DEFAULT_TAG = "default_tag";
    private static final Object sLock = new Object();
    private static final WeakHashMap<ProgressDialog, Tag> sDialogs = new WeakHashMap<>();
    private static Application.ActivityLifecycleCallbacks sLifecycleCallbacks;

    /**
     * Hide default dialog
     */
    public static void hideProgressDialog() {
        hideProgressDialog(DEFAULT_TAG);
    }

    /**
     * Call this before first call of {@link #showProgressDialog}
     *
     * @param app application
     */
    public static void init(Application app) {
        if (sLifecycleCallbacks != null) {
            app.unregisterActivityLifecycleCallbacks(sLifecycleCallbacks);
        }
        sLifecycleCallbacks = new OnDestroyActivityCallback() {
            @Override
            public void onActivityDestroyed(Activity activity) {
                final int hashCode = activity.hashCode();
                synchronized (sLock) {
                    final List<ProgressDialog> dialogs = findDialogs(hashCode);
                    for (ProgressDialog dialog : dialogs) {
                        dialog.dismiss();
                        sDialogs.remove(dialog);
                    }
                }
            }
        };
        app.registerActivityLifecycleCallbacks(sLifecycleCallbacks);
    }

    /**
     * Hides all dialogs and unregisters activity lifecycle callbacks
     *
     * @param app application instance
     */
    public static void release(Application app) {
        if (sLifecycleCallbacks != null) {
            app.unregisterActivityLifecycleCallbacks(sLifecycleCallbacks);
        }
        hideProgressDialogsAll();
    }

    /**
     * Shows default dialog with a message
     *
     * @param context context
     * @param message the message to show in a dialog
     */
    public static void showProgressDialog(final Context context, final String message) {
        showProgressDialog(context, DEFAULT_TAG, message);
    }

    private static ProgressDialog findDialog(@NonNull String tag) {
        for (Map.Entry<ProgressDialog, Tag> entry : sDialogs.entrySet()) {
            if (tag.equals(entry.getValue().tag)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private static List<ProgressDialog> findDialogs(long activityHashcode) {
        List<ProgressDialog> result = new ArrayList<>();
        for (Map.Entry<ProgressDialog, Tag> entry : sDialogs.entrySet()) {
            if (activityHashcode == entry.getValue().activityHashcode) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    private static Activity getActivity(final Context context) {
        if (context != null) {
            Context baseContext = context;
            while (!(baseContext instanceof Activity) && baseContext instanceof ContextWrapper) {
                baseContext = ((ContextWrapper) baseContext).getBaseContext();
            }
            if (baseContext instanceof Activity) {
                return (Activity) baseContext;
            }
        }
        return null;
    }

    /**
     * Hide dialog with specific tag
     *
     * @param tag the tag to determine specific dialog
     */
    private static void hideProgressDialog(String tag) {
        if (tag == null) {
            return;
        }
        synchronized (sLock) {
            ProgressDialog dialog = findDialog(tag);
            if (dialog != null) {
                if (isAlive(dialog) && dialog.isShowing()) {
                    dialog.dismiss();
                }
                sDialogs.remove(dialog);
            }
        }
    }

    /**
     * Hide all dialogs
     */
    private static void hideProgressDialogsAll() {
        synchronized (sLock) {
            for (ProgressDialog dialog : sDialogs.keySet()) {
                if (isAlive(dialog) && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
            sDialogs.clear();
        }
    }

    private static boolean isAlive(Dialog dialog) {
        return dialog != null && isAlive(getActivity(dialog.getContext()));
    }

    private static boolean isAlive(final Activity activity) {
        return activity != null && !activity.isDestroyed();
    }

    /**
     * Shows a new dialog with a message for each different tag.
     *
     * @param context context
     * @param tag     the tag for determining specific dialog
     * @param message the message to show in a dialog
     */
    private static void showProgressDialog(final Context context, String tag, final String message) {
        final Activity activity = getActivity(context);
        if (!isAlive(activity) || activity.isFinishing()) {
            return;
        }
        if (tag == null) {
            tag = DEFAULT_TAG;
        }
        ProgressDialog dialog;
        synchronized (sLock) {
            dialog = findDialog(tag);

            if (!isAlive(dialog)) {
                if (dialog != null) {
                    sDialogs.remove(dialog);
                }
                dialog = new ProgressDialog(context, R.style.CryptoExchange_AlertDialogTheme_Progress);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                final Window window = dialog.getWindow();
                if (window != null) {
                    window.getAttributes().windowAnimations = R.style.CryptoExchange_AlertDialogTheme_Progress;
                }
                dialog.setCancelable(false);
                sDialogs.put(dialog, new Tag(tag, activity.hashCode()));
            }
        }
        dialog.setMessage(message);
        dialog.show();
    }

    /**
     * Shows default progress dialog without any message
     *
     * @param context context
     */
    private static void showProgressDialog(final Context context) {
        showProgressDialog(context, DEFAULT_TAG, null);
    }

    private static class Tag {

        final long activityHashcode;

        final String tag;

        private Tag(String tag, long activityHashcode) {
            this.tag = tag;
            this.activityHashcode = activityHashcode;
        }
    }

    private static abstract class OnDestroyActivityCallback implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }
    }
}