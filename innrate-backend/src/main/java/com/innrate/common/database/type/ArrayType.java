package com.innrate.common.database.type;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;

@Slf4j
public abstract class ArrayType implements UserType {

    private final String sqlTypeName;

    protected ArrayType(String sqlTypeName) {
        this.sqlTypeName = sqlTypeName;
    }

    protected String getSqlTypeName() {
        return sqlTypeName;
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet,
                              String[] columnNames,
                              SharedSessionContractImplementor sharedSessionContractImplementor,
                              Object owner) throws HibernateException, SQLException {

        Array array = resultSet.getArray(columnNames[0]);
        return array == null ?
                null :
                optionallyCasted(array);
    }

    @SuppressWarnings("unchecked")
    private Object optionallyCasted(Array array) throws SQLException {
        Object arrayObject = array.getArray();
        Class<?> returnedClass = returnedClass();
        if (returnedClass.isInstance(arrayObject)) {
            return arrayObject;
        }

        Class<?> objectClass = arrayObject.getClass();
        if (!(objectClass.isArray()) || !(returnedClass.isArray())) {
            return arrayObject;
        }
        Object[] objects = (Object[]) arrayObject;
        try {
            return Arrays.copyOf(objects, objects.length, returnedClass());
        } catch (ArrayStoreException e) {
            log.warn("Failed to convert {} to {}", arrayObject, returnedClass);
        }
        return arrayObject;
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement,
                            Object value,
                            int paramIndex,
                            SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {

        Connection connection = preparedStatement.getConnection();

        Array array = value == null ? null : connection.createArrayOf(getSqlTypeName(), (Object[]) value);

        preparedStatement.setArray(paramIndex, array);
    }

    @Override
    public Object assemble(final Serializable cached, final Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    @Override
    public Serializable disassemble(final Object o) throws HibernateException {
        return (Serializable) o;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Object replace(final Object original, final Object target, final Object owner) throws HibernateException {
        return deepCopy(original);
    }

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.ARRAY};
    }
}
