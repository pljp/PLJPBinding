package jp.programminglife.binding.endpoint;

import android.view.View;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import jp.programminglife.binding.BindingUtils;
import jp.programminglife.binding.EndPoint;


public class ViewProp {

    private static final Method SET_ON_CLICK_LISTENER_METHOD = BindingUtils.getMethod(View.class, "setOnClickListener", View.OnClickListener.class);


    public static void addOnClickListener(View view, View.OnClickListener listener) {
        BindingUtils.addListener(view, SET_ON_CLICK_LISTENER_METHOD, listener);
    }


    public static final class Visibility<T> extends EndPoint<View, T, T> {

        @Override
        protected void onValueChanged() {
            setVisibility(getTargetNotNull(), convert());
        }


        private void setVisibility(View view, Object value) {

            if ( value == null ) {
                view.setVisibility(View.VISIBLE);
                return;
            }

            if ( value instanceof String )
                try {
                    value = new BigDecimal((String)value);
                }
                catch (NumberFormatException ex) {
                    view.setVisibility(View.VISIBLE);
                    return;
                }

            if ( value instanceof Number ) {
                int visibility = ((Number) value).intValue();
                if (visibility == View.GONE || visibility == View.INVISIBLE || visibility == View.VISIBLE)
                    view.setVisibility(visibility);
                else
                    view.setVisibility(View.VISIBLE);
                return;
            }

            if ( value instanceof Boolean ) {
                view.setVisibility((Boolean) value ? View.VISIBLE : View.GONE);
                return;
            }

            setVisibility(view, value.toString());
        }


        @Override
        protected void onRestored() {
            onValueChanged();
        }
    }

}
