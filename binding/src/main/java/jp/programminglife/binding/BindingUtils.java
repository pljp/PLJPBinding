package jp.programminglife.binding;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import org.apache.commons.collections4.map.AbstractReferenceMap.ReferenceStrength;
import org.apache.commons.collections4.map.ReferenceIdentityMap;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.programminglife.libpljp.android.CallbackSupport;


public final class BindingUtils {

    private BindingUtils() {}


    private static final ReferenceIdentityMap<View, HashMap<Method, CallbackSupport<?>>> listeners =
            new ReferenceIdentityMap<>(
                    ReferenceStrength.WEAK, ReferenceStrength.HARD, true);


    @SuppressWarnings("unchecked")
    public static void addListener(@NonNull View view, @NonNull Method method, @NonNull Object listener) {

        HashMap<Method, CallbackSupport<?>> map = listeners.get(view);
        if ( map == null ) {
            map = new HashMap<>();
            listeners.put(view, map);
        }

        Class<?>[] types = method.getParameterTypes();
        if ( types.length != 1 ) throw new IllegalArgumentException("methodの引数が1つではない");
        @SuppressWarnings("rawtypes")
        CallbackSupport cbs = map.get(method);
        if ( cbs == null ) {
            cbs = CallbackSupport.of(types[0]);
            map.put(method, cbs);
            try {
                method.invoke(view, cbs.getProxy());
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }
        cbs.add(listener);

    }


    @NonNull
    public static Method getMethod(@NonNull Class<?> cls, @NonNull String name, @NonNull Class<?> paramType) {

        try {
            return cls.getMethod(name, paramType);
        }
        catch (NoSuchMethodException ex) {
            throw new IllegalArgumentException();
        }

    }


    /**
     * オブジェクトoのBindable型のpublicフィールドをbundleに格納する。
     * Bindableの値がSerializableかつ非transientの場合だけ格納される。
     * @param o データオブジェクト。
     * @param bundle データを格納するBundleオブジェクト。
     * @return 引数bundleで渡されたBundleオブジェクト。bundleがnullの場合は新しく作られる。
     */
    @NonNull
    public static Bundle save(@Nullable Object o, @Nullable Bundle bundle) {

        if ( bundle == null )
            bundle = new Bundle();
        if ( o == null ) return bundle;

        Class<?> cls = o.getClass();
        Field[] fields = cls.getFields();
        for (Field f : fields) {
            if ( !Modifier.isTransient(f.getModifiers()) && Bindable.class.isAssignableFrom(f.getType()) ) {
                try {

                    Bindable<?> b = (Bindable<?>)f.get(o);
                    String name = f.getName();
                    Object value = b.get();
                    Serializable ser = b.getSerializableValue();
                    // 値がnullのときはnullを記録する。
                    // シリアライズできないとき(value != null && ser == null)は記録しない。
                    if ( value == null || ser != null )
                        bundle.putSerializable(name, ser);

                }
                catch (IllegalAccessException | IllegalArgumentException ex) {
                    v("", ex);
                }
            }
        }
        return bundle;

    }


    /**
     * オブジェクトoのBindableフィールドにbundleのデータをロードする。
     * データを復元した後、各Bindableの{@link Bindable#notifyValueRestored()}を呼び出す。
     * @param o 復元したデータをセットするオブジェクト。null以外の値。
     * @param bundle saveメソッドで保存されたデータ。nullの場合は何もしない。
     */
    public static void load(@NonNull Object o, @Nullable Bundle bundle) {

        if ( bundle == null ) return;

        Class<?> cls = o.getClass();
        List<Bindable> bindables = new ArrayList<>(bundle.size());
        for(String name : bundle.keySet()) {
            try {

                Field f = cls.getField(name);
                if ( Bindable.class.isAssignableFrom(f.getType()) ) {

                    Serializable ser = bundle.getSerializable(name);
                    Bindable<?> bindable = (Bindable<?>) f.get(o);
                    bindable.restore(ser);
                    bindables.add(bindable);
                }

            }
            catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException ex) {
                v("", ex);
            }

        }

        for (Bindable bindable : bindables)
            bindable.notifyValueRestored();
    }


    private static void v(String message, Throwable t) {
        Log.v("binding", message, t);
    }

}
