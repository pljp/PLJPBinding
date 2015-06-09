package jp.programminglife.binding;


import android.app.Activity;
import android.view.View;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

import lombok.EqualsAndHashCode;


@EqualsAndHashCode
public final class Var implements Bindable<Object> {

    private final BindableObject<Object> bindable;


    public static Var create() {
        return new Var(BindableObject.create());
    }


    public static Var create(Object value) {
        return new Var(BindableObject.create(value));
    }


    private Var(BindableObject<Object> bindable) {
        this.bindable = bindable;
    }


    @NotNull
    @Override
    public <Target extends View, TargetValue> Target bind(@NotNull View rootView, int id,
            @NotNull EndPoint<? super Target, Object, TargetValue> endPoint) {
        return bindable.bind(rootView, id, endPoint);
    }


    @NotNull
    @Override
    public <Target extends View, TargetValue> Target bind(@NotNull View rootView, int id,
            @NotNull EndPoint<? super Target, Object, TargetValue> endPoint,
            boolean notify, Converter<Object, TargetValue> converter) {
        return bindable.bind(rootView, id, endPoint, notify, converter);
    }


    @NotNull
    @Override
    public <Target extends View, TargetValue> Target bind(@NotNull Activity activity, int id,
            @NotNull EndPoint<? super Target, Object, TargetValue> endPoint) {
        return bindable.bind(activity, id, endPoint);
    }


    @NotNull
    @Override
    public <Target extends View, TargetValue> Target bind(@NotNull Activity activity, int id,
            @NotNull EndPoint<? super Target, Object, TargetValue> endPoint,
            boolean notify, Converter<Object, TargetValue> converter) {
        return bindable.bind(activity, id, endPoint, notify, converter);
    }


    @Override
    public <Target, TargetValue> void bind(Target target,
            @NotNull EndPoint<? super Target, Object, TargetValue> endPoint) {bindable.bind(target, endPoint);}


    @Override
    public <Target, TargetValue> void bind(Target target,
            @NotNull EndPoint<? super Target, Object, TargetValue> endPoint,
            boolean notify, Converter<Object, TargetValue> converter) {
        bindable.bind(target, endPoint, notify, converter);
    }


    @Deprecated
    public <BindableValue> void observe(@NotNull Bindable<BindableValue> b) {bindable.observe(b);}


    @Override
    public <T> void attach(@NotNull Bindable<T> target, @NotNull Converter<Object, T> converter) {
        bindable.attach(target, converter);
    }


    @Override
    public void set(Object value) {bindable.set(value);}


    @Override
    public void set(Object o, EndPoint<?, Object, ?> source) {bindable.set(o, source);}


    @Override
    public void notifyValueChanged(EndPoint<?, Object, ?> source) {bindable.notifyValueChanged(source);}


    @Override
    public Object get() {return bindable.get();}


    @NotNull
    @Override
    public Object get(@NotNull Object defaultValue) {
        return bindable.get(defaultValue);
    }


    @Override
    public boolean isNull() {return bindable.isNull();}


    @Override
    public Serializable getSerializableValue() {return bindable.getSerializableValue();}


    @Override
    public void restore(Serializable ser) {bindable.restore(ser);}


    @Override
    public void notifyValueRestored() {bindable.notifyValueRestored();}


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
