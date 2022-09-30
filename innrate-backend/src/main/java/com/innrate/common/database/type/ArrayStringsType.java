package com.innrate.common.database.type;


import org.hibernate.HibernateException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ArrayStringsType extends ArrayType {

    public ArrayStringsType() {
        super("text");
    }

    @Override
    public Object deepCopy(final Object o) throws HibernateException {
        return o == null ? null : ((String[]) o).clone();
    }

    @Override
    public boolean equals(final Object x, final Object y) {
        return Arrays.equals((String[]) x, (String[]) y);
    }

    @Override
    public int hashCode(final Object o) throws HibernateException {
        return o == null ? 0 : Arrays.hashCode((String[]) o);
    }

    @Override
    public Class<String[]> returnedClass() {
        return String[].class;
    }
}