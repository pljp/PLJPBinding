package jp.programminglife.binding;


import android.app.Activity;
import android.view.View;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public class BindableNonNull<Value> implements Bindable<Value> {

    private final BindableObject<Value> bindable;


    @NotNull
    public static <V> BindableNonNull<V> create(@NotNull V value) {
        return new BindableNonNull<>(value);
    }


    private BindableNonNull(Value value) {
        bindable = BindableObject.create(value);
    }


    @NotNull
    @Override
    public <Target extends View, TargetValue> Target bind(
            @NotNull View rootView, int id,
            @NotNull EndPoint<? super Target, Value, TargetValue> endPoint) {
        return bindable.bind(rootView, id, endPoint);
    }


    @NotNull
    @Override
    public <Target extends View, TargetValue> Target bind(
            @NotNull View rootView, int id,
            @NotNull EndPoint<? super Target, Value, TargetValue> endPoint,
            boolean notify, Converter<Value, TargetValue> converter) {
        return bindable.bind(rootView, id, endPoint, notify, converter);
    }


    @NotNull
    @Override
    public <Target extends View, TargetValue> Target bind(
            @NotNull Activity activity, int id,
            @NotNull EndPoint<? super Target, Value, TargetValue> endPoint) {
        return bindable.bind(activity, id, endPoint);
    }


    @NotNull
    @Override
    public <Target extends View, TargetValue> Target bind(
            @NotNull Activity activity, int id,
            @NotNull EndPoint<? super Target, Value, TargetValue> endPoint,
            boolean notify, Converter<Value, TargetValue> converter) {
        return bindable.bind(activity, id, endPoint, notify, converter);
    }


    @Override
    public <Target, TargetValue> void bind(
            Target target,
            @NotNull EndPoint<? super Target, Value, TargetValue> endPoint) {bindable.bind(target, endPoint);}


    @Override
    public <Target, TargetValue> void bind(
            Target target,
            @NotNull EndPoint<? super Target, Value, TargetValue> endPoint,
            boolean notify, Converter<Value, TargetValue> converter) {
        bindable.bind(target, endPoint, notify, converter);
    }


    @Override
    public <T> void attach(
            @NotNull Bindable<T> target,
            @NotNull Converter<Value, T> converter) {bindable.attach(target, converter);}


    @Override
    public void set(@NotNull Value value) {bindable.set(value);}


    @Override
    public void set(@NotNull Value value, EndPoint<?, Value, ?> source) {bindable.set(value, source);}


    @Override
    public void notifyValueChanged(EndPoint<?, Value, ?> source) {bindable.notifyValueChanged(source);}


    @NotNull
    @Override
    public Value get() {return bindable.get();}


    @NotNull
    @Override
    public Value get(@NotNull Value defaultValue) {return bindable.get(defaultValue);}


    @Override
    public boolean isNull() {return bindable.isNull();}


    @Nullable
    @Override
    public Serializable getSerializableValue() {return bindable.getSerializableValue();}


    @Override
    public void restore(Serializable ser) {bindable.restore(ser);}


    @Override
    public void notifyValueRestored() {bindable.notifyValueRestored();}


    @NotNull
    public BindableNonNull<Value> serializer(
            @Nullable Serializer<Value> serializer) {bindable.serializer(serializer); return this;}


    @Override
    public boolean toBoolean(boolean defaultValue) {return bindable.toBoolean(defaultValue);}


    @Override
    public int toInt(int defaultValue) {return bindable.toInt(defaultValue);}


    @Override
    public long toLong(long defaultValue) {return bindable.toLong(defaultValue);}


    @Override
    public float toFloat(float defaultValue) {return bindable.toFloat(defaultValue);}


    @Override
    public double toDouble(double defaultValue) {return bindable.toDouble(defaultValue);}


    @NotNull
    @Override
    public Number toNumber(@NotNull Number defaultValue) {return bindable.toNumber(defaultValue);}


    @Nullable
    @Override
    public Number toNumber() {return bindable.toNumber();}


    @NotNull
    @Override
    public String toString(@NotNull String defaultValue) {return bindable.toString(defaultValue);}


    @Nullable
    @Override
    public String toStringOrNull() {return bindable.toStringOrNull();}
}
