package jp.programminglife.binding;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class EndPoint<Target, BindableValue, TargetValue> implements ConvertUtils.Convertible {

    private static final Converter DEFAULT_CONVERTER = new DefaultConverter();
    @NonNull
    private final Converter<BindableValue, TargetValue> defaultConverter;
    @Nullable
    private Target target;
    @NonNull
    private Converter<BindableValue, TargetValue> converter;
    @Nullable
    private Bindable<BindableValue> bindable;


    @SuppressWarnings("unchecked")
    public EndPoint() {
        this.converter = this.defaultConverter = DEFAULT_CONVERTER;
    }


    public EndPoint(@NonNull Converter<BindableValue, TargetValue> defaultConverter) {
        this.converter = this.defaultConverter = defaultConverter;
    }


    public final void set(@Nullable BindableValue value) {
        if ( bindable == null ) throw new NullPointerException("bindable == null");
        bindable.set(value, this);
    }


    @NonNull
    public final BindableValue get(@NonNull BindableValue defaultValue) {

        if ( bindable == null ) throw new NullPointerException("bindable == null");
        BindableValue v = bindable.get();
        return v != null ? v : defaultValue;
    }


    @Nullable
    public final BindableValue get() {
        if ( bindable == null ) throw new NullPointerException("bindable == null");
        return bindable.get();
    }


    @NonNull
    public final TargetValue convert(@NonNull BindableValue defaultValue) {

        TargetValue tv = converter.convert(get(defaultValue));
        if ( tv == null ) throw new NullPointerException();
        return tv;

    }


    @Nullable
    public final TargetValue convert() {

        BindableValue v = get();
        return converter.convert(v);
    }


    void setTarget(@Nullable Target target) {
        this.target = target;
    }


    /**
     * targetを返す。
     * @return target。nullの場合もある。
     */
    @Nullable
    protected final Target getTarget() {
        return target;
    }


    /**
     * targetを返す。もしtargetがnullだったらNullPointerExceptionをスローする。
     * @return target
     */
    @NonNull
    protected final Target getTargetNotNull() {
        if ( target == null ) throw new NullPointerException("target == null");
        return target;
    }


    @SuppressWarnings("unchecked")
    void setConverter(@Nullable Converter<BindableValue, TargetValue> converter) {
        this.converter = converter != null ? converter : defaultConverter;
    }


    @NonNull
    protected final Bindable<BindableValue> getBindable() {
        if ( bindable == null ) throw new NullPointerException("bindable == null");
        return bindable;
    }


    final void bind(@NonNull Bindable<BindableValue> bindable) {
        this.bindable = bindable;
        onBind();
    }


    final void notifyValueChanged() {
        onValueChanged();
    }


    final void notifyValueRestored() {
        onRestored();
    }


    /**
     * Bindableにバインドされた時に呼び出される。
     * targetが値の変更イベントを発行する場合、targetへのイベント登録はこのタイミングで行う。
     */
    protected void onBind() {}


    /**
     * Bindableの値が変更された時に呼び出される。
     */
    protected void onValueChanged() {}


    protected void onRestored() {}


    // Convertibleの実装

    @Override
    public boolean toBoolean(boolean defaultValue) {
        return ConvertUtils.toBoolean(convert(), defaultValue);
    }


    @Override
    public int toInt(int defaultValue) {
        return ConvertUtils.toInt(convert(), defaultValue);
    }


    @Override
    public long toLong(long defaultValue) {
        return ConvertUtils.toLong(convert(), defaultValue);
    }


    @Override
    public float toFloat(float defaultValue) {
        return ConvertUtils.toFloat(convert(), defaultValue);
    }


    @Override
    public double toDouble(double defaultValue) {
        return ConvertUtils.toDouble(convert(), defaultValue);
    }


    @NonNull
    @Override
    public Number toNumber(@NonNull Number defaultValue) {
        return ConvertUtils.toNumber(convert(), defaultValue);
    }


    @Nullable
    @Override
    public Number toNumber() {
        return ConvertUtils.toNumber(convert());
    }


    @NonNull
    @Override
    public String toString(@NonNull String defaultValue) {
        return ConvertUtils.toString(convert(), defaultValue);
    }


    @Nullable
    @Override
    public String toStringOrNull() {
        return ConvertUtils.toString(convert());
    }


    public static final class DefaultConverter implements Converter<Object, Object> {

        @NonNull
        @Override
        public Object convert(Object value) {
            return value;
        }
    }

}