package com.semerson.networkassessment.utils;

import android.content.Context;
import android.support.annotation.IdRes;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.TextView;

public class UiObjectCreator {

    public static final String txtMoreDetails = "<a> More detail </a>";
    /**
     * Create a TextView.
     * @param id The ID for the textview
     * @param txtViewTxt The text to use
     * @param context The context
     * @param listener The listener to add for the textview click
     * @return The created TextView
     */
    public static TextView createTextView(@IdRes int id, String txtViewTxt, Context context, View.OnClickListener listener) {
        TextView textView = new TextView(context);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setOnClickListener(listener);
        textView.setId(id);

        String text = txtViewTxt;
        textView.setText(Html.fromHtml(txtViewTxt));
        return textView;
    }

    public static TextView createTextView(Context context, CharSequence text, int style) {
        TextView textView = new TextView(new ContextThemeWrapper(context, style ));
        textView.setText(text);
        return textView;
    }

    public static TextView createTextView(@IdRes int id, String txtViewTxt, Context context) {
        TextView textView = new TextView(context);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setId(id);

        String text = txtViewTxt;
        textView.setText(Html.fromHtml(txtViewTxt));
        return textView;
    }
}
