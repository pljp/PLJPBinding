package jp.programminglife.binding;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Bindableの実装クラス。equals, hashCodeは保持する値だけを比較し、EndPointやSerializerは無視する。
 * @param <Value> 値の型。
 */
public final class BindableObject<Value> implements Bindable<Value> {

    public static <TT> BindableObject<TT> create() {
        return create(null, null);
    }


    public static <TT> BindableObject<TT> create(TT value) {
        return create(value, null);
    }


    private static <TT> BindableObject<TT> create(TT value, Serializer<TT> serializer) {
        BindableObject<TT> ret = new BindableObject<>();
        ret.set(value);
        ret.setSerializer(serializer);
        return ret;
    }


    @Nullable
    private Value value;
    transient private ArrayList<EndPoint<?, Value, ?>> endPoints;
    transient private Serializer<Value> serializer;


    protected BindableObject() {
        endPoints = new ArrayList<>();
    }


    @NonNull
    @Override
    public <Target extends View, TargetValue> Target bind(
            @NonNull View rootView, int id, @NonNull EndPoint<? super Target, Value, TargetValue> endPoint) {
        return bind(rootView, id, endPoint, true, null);
    }


    @NonNull
    @Override
    public <Target extends View, TargetValue> Target bind(
            @NonNull View rootView, int id, @NonNull EndPoint<? super Target, Value, TargetValue> endPoint,
            boolean notify, Converter<Value, TargetValue> converter) {

        @SuppressWarnings("unchecked")
        Target view = (Target)rootView.findViewById(id);
        if ( view == null ) throw new NullPointerException("Viewが見つからない");
        bind(view, endPoint, notify, converter);
        return view;

    }


    @NonNull
    @Override
    public <Target extends View, TargetValue> Target bind(
            @NonNull Activity activity, int id, @NonNull EndPoint<? super Target, Value, TargetValue> endPoint) {
        return bind(activity, id, endPoint, true, null);
    }


    @NonNull
    @Override
    public <Target extends View, TargetValue> Target bind(
            @NonNull Activity activity, int id, @NonNull EndPoint<? super Target, Value, TargetValue> endPoint,
            boolean notify, Converter<Value, TargetValue> converter) {

        @SuppressWarnings("unchecked")
        Target view = (Target)activity.findViewById(id);
        if ( view == null ) throw new NullPointerException("Viewが見つからない");
        bind(view, endPoint, notify, converter);
        return view;

    }


    @Override
    public <Target, TargetValue> void bind(
            Target target, @NonNull EndPoint<? super Target, Value, TargetValue> endPoint) {
        bind(target, endPoint, true, null);
    }


    @Override
    public <Target, TargetValue> void bind(
            Target target, @NonNull EndPoint<? super Target, Value, TargetValue> endPoint,
            boolean notify, Converter<Value, TargetValue> converter) {

        endPoint.setTarget(target);
        endPoint.setConverter(converter);
        addEndPoint(endPoint);
        if ( notify )
            endPoint.notifyValueChanged();
    }


    @Override
    public void bind(final Action<Value> observer) {
        bind(observer, true);
    }


    @Override
    public void bind(final Action<Value> observer, boolean notify) {
        bind(null, new EndPoint<Object, Value, Value>() {
            @Override
            protected void onBind() {}
            @Override
            protected void onRestored() {}
            @Override
            protected void onValueChanged() {observer.action(get());}
        }, notify, null);
    }


    /**
     * 新しいEndPointを追加する。
     */
    private EndPoint<?, Value, ?> addEndPoint(EndPoint<?, Value, ?> endPoint) {

        endPoints.add(endPoint);
        endPoint.bind(this);

        return endPoint;

    }


    @Override
    public <BindableValue> void observe(@NonNull Bindable<BindableValue> b) {

        b.bind(this, new EndPoint<Bindable<Value>, BindableValue, Object>() {

            /**
             * 監視対象のBindable(=b)からの変更通知をこのBindableに通知する。
             */
            @Override
            public void onValueChanged() {
                BindableObject.this.notifyValueChanged(null);
            }

        });

    }


    @Override
    public <T> void attach(@NonNull Bindable<T> target, @NonNull Converter<Value, T> converter) {

        bind(target, new EndPoint<Bindable<T>, Value, T>() {
            @Override
            protected void onValueChanged() {
                getTargetNotNull().set(convert());
            }
        }, true, converter);
    }


    @Override
    public void set(Value value) {
        set(value, null);
    }


    @Override
    public void set(Value value, EndPoint<?, Value, ?> source) {

        if ( this.value == null ) {
            if ( value == null ) return;
        }
        else
            if ( this.value.equals(value) ) return;

        this.value = value;
        notifyValueChanged(source);

    }


    @Override
    public void notifyValueChanged(EndPoint<?, Value, ?> source) {

        for (EndPoint<?, Value, ?> e : endPoints)
            if ( e != source )
                e.notifyValueChanged();

    }


    @Override
    public Value get() {
        return value;
    }


    @NonNull
    @Override
    public Value get(@NonNull Value defaultValue) {
        return value != null ? value : defaultValue;
    }


    @Override
    public boolean isNull() {
        return value == null;
    }


    @Override
    public Serializable getSerializableValue() {

        if ( value == null )
            return null;
        if ( serializer != null )
            return serializer.toSerializable(value);
        if ( value instanceof Serializable )
            return (Serializable)value;
        return new IllegalStateException("serializer == null && !(value instanceof Serializable)");

    }


    @Override
    @SuppressWarnings("unchecked")
    public void restore(Serializable ser) {

        if ( ser == null )
            value = null;
        else if ( serializer != null )
            value = serializer.fromSerializable(ser);
        else
            value = (Value)ser;
    }


    @Override
    public void notifyValueRestored() {

        for (EndPoint<?, Value, ?> endPoint : endPoints)
            endPoint.notifyValueRestored();
    }


    private void setSerializer(Serializer<Value> serializer) {
        this.serializer = serializer;
    }


    @NonNull
    public BindableObject<Value> serializer(@Nullable Serializer<Value> serializer) {
        this.serializer = serializer;
        return this;
    }


    // Convertibleの実装

    @Override
    public boolean toBoolean(boolean defaultValue) {
        return ConvertUtils.toBoolean(value, defaultValue);
    }


    @Override
    public int toInt(int defaultValue) {
        return ConvertUtils.toInt(value, defaultValue);
    }


    @Override
    public long toLong(long defaultValue) {
        return ConvertUtils.toLong(value, defaultValue);
    }


    @Override
    public float toFloat(float defaultValue) {
        return ConvertUtils.toFloat(value, defaultValue);
    }


    @Override
    public double toDouble(double defaultValue) {
        return ConvertUtils.toDouble(value, defaultValue);
    }


    @NonNull
    @Override
    public Number toNumber(@NonNull Number defaultValue) {
        return ConvertUtils.toNumber(value, defaultValue);
    }


    @Nullable
    @Override
    public Number toNumber() {
        return ConvertUtils.toNumber(value);
    }


    @NonNull
    @Override
    public String toString(@NonNull String defaultValue) {
        return ConvertUtils.toString(value, defaultValue);
    }


    @Nullable
    @Override
    public String toStringOrNull() {
        return ConvertUtils.toString(value);
    }


    @Override
    public boolean equals(Object o) {

        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;

        BindableObject<?> that = (BindableObject<?>) o;

        return value != null ? value.equals(that.value) : that.value == null;
    }


    @Override
    public int hashCode() {

        return value != null ? value.hashCode() : 0;
    }
}
