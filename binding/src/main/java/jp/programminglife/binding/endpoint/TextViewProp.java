package jp.programminglife.binding.endpoint;

import android.widget.TextView;

import jp.programminglife.binding.Converter.ToStringConverter;
import jp.programminglife.binding.EndPoint;


public class TextViewProp extends ViewProp {

    public static final class Text<V> extends EndPoint<TextView, V, String> {

        public Text() {
            super(new ToStringConverter<V>());
        }


        @Override
        protected void onValueChanged() {
            getTargetNotNull().setText(convert());
        }


        @Override
        protected void onRestored() {
            onValueChanged();
        }
    }

}
