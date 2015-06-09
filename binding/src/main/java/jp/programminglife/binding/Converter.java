package jp.programminglife.binding;


import org.jetbrains.annotations.Nullable;

public interface Converter<From, To> {

    /**
     * valueをToの型に変換する。
     * @param value 変換元の値。
     * @return 返還後の値。null以外の値が渡されたときはnullを返してはいけない。
     */
    @Nullable
    To convert(@Nullable From value);


    public static final class ToIntegerConverter<F> implements Converter<F, Integer> {

        private static final Integer ZERO = 0;

        @Override
        public Integer convert(@Nullable F value) {

            if ( value == null ) return null;
            return ConvertUtils.toInt(value, ZERO);
        }
    }


    public static final class ToStringConverter<F> implements Converter<F, String> {

        @Override
        public String convert(@Nullable F value) {

            if ( value == null ) return null;
            return ConvertUtils.toString(value, "");
        }
    }


    public static class ToBooleanConverter<F> implements Converter<F,Boolean> {

        @Nullable
        @Override
        public Boolean convert(@Nullable F value) {
            if ( value == null ) return null;
            return ConvertUtils.toBoolean(value, Boolean.FALSE);
        }
    }


    public static class SimpleCastConverter<F, T> implements Converter<F,T> {

        @Nullable
        @Override
        @SuppressWarnings("unchecked")
        public T convert(@Nullable F value) {
            return (T)value;
        }
    }


/*
    <Target> To convert(EndPoint<Target, From, To> endPoint, From defaultvalue);


    public static final class ToIntegerConverter<F> implements Converter<F, Integer> {

        @Override
        public <Target> Integer convert(EndPoint<Target, F, Integer> endPoint, F defaultvalue) {
            if ( endPoint.getgetValue() instanceof Integer )
                return (Integer)bindable.getValue();
            return ConvertUtils.toInt(bindable.getValue(), 0);
        }
    }

*/
}
