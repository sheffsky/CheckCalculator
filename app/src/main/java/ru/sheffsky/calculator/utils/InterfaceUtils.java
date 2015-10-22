package ru.sheffsky.calculator.utils;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;

import ru.sheffsky.calculator.R;

public class InterfaceUtils {

    public enum buttonColor { GREEN , GRAY, RED }
    public enum buttonType { MINUS, PLUS }

    public static Integer settingMinQty = 1;
    public static Integer settingMaxQty = 99;
    public static Integer settingMinPersons = 1;
    public static Integer settingMaxPersons = 99;
    public static Integer settingMinPrice = 0;
    public static Integer settingMaxPrice = 999999;

    public static void setImageButtonColor(View view, buttonType bType, ImageButton button, buttonColor buttonColor) {

        Drawable originalIcon = null;

        switch (bType) {
            case MINUS:
                originalIcon = view.getResources().getDrawable(R.drawable.ic_minus_circle_outline_grey600_36dp);
                break;

            case PLUS:
                originalIcon = view.getResources().getDrawable(R.drawable.ic_plus_circle_outline_grey600_36dp);
                break;

        }
        if (originalIcon != null) {
            Drawable mutatedMinusIcon = originalIcon.mutate();

            switch (buttonColor) {
                case GRAY:
                    mutatedMinusIcon.setColorFilter(Color.parseColor("#535353"), PorterDuff.Mode.SRC_IN);
                    break;

                case GREEN:
                    mutatedMinusIcon.setColorFilter(Color.parseColor("#55A055"), PorterDuff.Mode.SRC_IN);
                    break;

                case RED:
                    mutatedMinusIcon.setColorFilter(Color.parseColor("#A05555"), PorterDuff.Mode.SRC_IN);
                    break;

            }

            button.setImageDrawable(mutatedMinusIcon);

        }

    }

}
