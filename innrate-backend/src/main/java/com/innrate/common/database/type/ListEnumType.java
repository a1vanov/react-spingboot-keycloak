package com.innrate.common.database.type;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ListEnumType implements UserType, ParameterizedType {
    private final int[] arrayTypes = new int[]{Types.ARRAY};

    private Class<Enum<?>> mappedClass;

    protected Class<Enum<?>> getMappedClass() {
        return mappedClass;
    }

    protected void setMappedClass(Class<Enum<?>> mappedClass) {
        this.mappedClass = mappedClass;
    }

    public int[] sqlTypes() {
        return arrayTypes;
    }

    public Class<List> returnedClass() {
        return List.class;
    }

    public boolean equals(Object x, Object y) throws HibernateException {
        return Objects.equals(x, y);
    }

    public int hashCode(Object x) throws HibernateException {
        return x == null ? 0 : x.hashCode();
    }

    public Object deepCopy(Object value) throws HibernateException {
        if (value == null) {
            return null;
        }

        List<Enum<?>> list = (List<Enum<?>>) value;

        return new ArrayList<>(list);
    }

    public boolean isMutable() {
        return false;
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
            throws HibernateException, SQLException {

        if (names == null || names.length <= 0 || rs == null || rs.getArray(names[0]) == null) {
            return null;
        }

        Object[] array = (Object[]) rs.getArray(names[0]).getArray();

        return Stream.of(array)
                .map(s -> s == null ? null : Enum.valueOf((Class) mappedClass, s.toString()))
                .collect(Collectors.toList());
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
            throws HibernateException, SQLException {

        if (st == null) {
            throw new IllegalStateException("Not null statement is expected");
        }
        if (value == null) {
            st.setNull(index, arrayTypes[0]);
            return;
        }

        List<Enum<?>> enumList = (List<Enum<?>>) value;
        String[] enumArray = enumList.stream()
                .map(e -> e == null ? null : e.name())
                .toArray(String[]::new);

        Array array = session.connection().createArrayOf("text", enumArray);
        st.setArray(index, array);
    }

    public void setParameterValues(Properties parameters) {
        if (parameters.containsKey("enumClass")) {
            String enumClassName = parameters.getProperty("enumClass");
            try {
                setMappedClass((Class<Enum<?>>) Class.forName(enumClassName));
            } catch (ClassNotFoundException e) {
                throw new HibernateException("Specified enum class could not be found", e);
            }
        }
    }
}