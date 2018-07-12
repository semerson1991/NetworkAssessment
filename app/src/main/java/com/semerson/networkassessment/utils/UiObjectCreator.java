package com.semerson.networkassessment.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.authentication.AccountActivity;
import com.semerson.networkassessment.activities.authentication.LoginActivity;
import com.semerson.networkassessment.activities.user.awareness.UserAwareness;

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

  //  public static SpannableString createHyperlinkString(S)

    public static TextView createTextView(@IdRes int id, String txtViewTxt, Context context) {
        TextView textView = new TextView(context);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setId(id);

        String text = txtViewTxt;
        textView.setText(Html.fromHtml(txtViewTxt));
        return textView;
    }

    public static TextView createNetworkRequiredTextView(final Context context) {
        TextView textView = new TextView(context);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        SpannableString securityBoxText = new SpannableString(Html.fromHtml(" <u> connect to your security box </u>"));
        ClickableSpan clickableText = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent accountActivity = new Intent(context, AccountActivity.class);
                context.startActivity(accountActivity);
            }
        };

        securityBoxText.setSpan(clickableText, 0, securityBoxText.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        securityBoxText.setSpan(new ForegroundColorSpan(Color.BLUE), 0, securityBoxText.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableStringBuilder.append(System.getProperty("line.separator"));
        spannableStringBuilder.append(Html.fromHtml("<font color=\"black\"> <b>"+"Please " + "</b>"));
        spannableStringBuilder.append(securityBoxText);
        spannableStringBuilder.append(Html.fromHtml("<font color=\"black\"> <b>"+" for full access to the application" + "</b>"));
        textView.setText(spannableStringBuilder);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    public static TextView createLoginRequiredTextView(final Context context) {
        TextView textView = new TextView(context);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        SpannableString loginText = new SpannableString(Html.fromHtml(" <u> Login </u>"));
        ClickableSpan clickableText = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent loginActivity = new Intent(context, LoginActivity.class);
                context.startActivity(loginActivity);
            }
        };

        loginText.setSpan(clickableText, 0, loginText.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        loginText.setSpan(new ForegroundColorSpan(Color.BLUE), 0, loginText.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableStringBuilder.append(System.getProperty("line.separator"));
        spannableStringBuilder.append(Html.fromHtml("<font color=\"black\"> <b>"+"Please " + "</b>"));
        spannableStringBuilder.append(loginText);
        spannableStringBuilder.append(Html.fromHtml("<font color=\"black\"> <b>"+" then connect to your security box for full access" + "</b>"));
        textView.setText(spannableStringBuilder);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    /**
     * The  Activity is the activity to navigate to upon clicking the spannable string
     * @param context
     * @param activity
     */
    public static SpannableString createSpannableString(final Context context, final Activity activity, String text) {
        SpannableString spannableString = new SpannableString(Html.fromHtml(" <u>'"+text+"'</u>"));

        ClickableSpan clickableText = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent newActivity = new Intent(context, activity.getClass());
                context.startActivity(newActivity);
            }
        };
        spannableString.setSpan(clickableText, 0, spannableString.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 0, spannableString.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

}
