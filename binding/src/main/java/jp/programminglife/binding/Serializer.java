package jp.programminglife.binding;

import java.io.Serializable;


public interface Serializer<T> {
    Serializable toSerializable(T obj);
    T fromSerializable(Serializable ser);
}
