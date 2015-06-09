package jp.programminglife.binding.endpoint;

import android.view.View;
import android.widget.CompoundButton;

import jp.programminglife.binding.Converter.ToBooleanConverter;
import jp.programminglife.binding.EndPoint;


public class CompoundButtonProp extends ButtonProp {

    public static final class Checked extends EndPoint<CompoundButton, Object, Boolean> {

        private View.OnClickListener listener;


        public Checked() {
            super(new ToBooleanConverter<>());
        }


        @Override
        protected void onBind() {

            listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    set(((CompoundButton)v).isChecked());
                }
            };
            addOnClickListener(getTarget(), listener);

        }

        @Override
        protected void onValueChanged() {

            boolean checked = convert(Boolean.FALSE);
            CompoundButton compoundButton = getTargetNotNull();
            if ( checked != compoundButton.isChecked() )
                compoundButton.setChecked(checked);

        }

    }
}
