package com.innrate.common;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * This class contains methods to help us get around
 * some design issue in code of third-party libraries without coping-pasting their code.
 */
public class ReflectUtil {

    /**
     * Reads a value of a specified field, including private
     *
     * @param o         the object
     * @param fieldName the field name
     * @param <T>       value type
     * @return the read value
     */
    @SuppressWarnings("unchecked")
    public static <T> T readField(Object o, String fieldName) {
        try {
            Object value = FieldUtils.readField(o, fieldName, true);
            return (T) value;

        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Field '" + fieldName + "' must exist in class.");
        }
    }

    /**
     * Creates a new instance of a specified class. Works for non-public classes or constructors.
     *
     * @param className the class name
     * @param args      the instance constructor arguments
     * @param <T>       the instance type
     * @return created instance
     */
    public static <T> T newInstance(String className, Object... args) {
        Class<?>[] argTypes = ClassUtils.toClass(args);
        Constructor<T> constructor = getConstructor(className, argTypes);
        return newInstance(constructor, args);
    }

    /**
     * Creates a new instance by a specified constructor.
     *
     * @param constructor the constructor
     * @param args        the instance constructor arguments
     * @param <T>         the instance type
     * @return created instance
     */
    public static <T> T newInstance(Constructor<T> constructor, Object... args) {
        try {
            constructor.setAccessible(true);
            return constructor.newInstance(args);

        } catch (IllegalAccessException
                | InstantiationException
                | InvocationTargetException e) {

            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * Returns a specified class constructor.
     *
     * @param className the class name
     * @param argTypes  the constructor argument types
     * @param <T>       the class type
     * @return found constructor
     */
    public static <T> Constructor<T> getConstructor(String className, Class<?>... argTypes) {
        try {
            Class<T> c = (Class) Class.forName(className);

            /* Note:
             * getConstructor() returns only public constructors.
             * So we needed to search for any Declared constructor. */
            return c.getDeclaredConstructor(argTypes);

        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
