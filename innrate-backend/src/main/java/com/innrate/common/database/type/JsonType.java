package com.innrate.common.database.type;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class JsonType implements UserType {

    protected static final int SQLTYPE = Types.JAVA_OBJECT;

    private final ObjectMapper mapper;

    public JsonType() {
        this.mapper = new ObjectMapper();
    }

    @Override
    public int[] sqlTypes() {
        return new int[]{SQLTYPE};
    }

    @Override
    public Class<Map> returnedClass() {
        return Map.class;
    }

    @Override
    public Object deepCopy(final Object value) throws HibernateException {
        try {
            // use serialization to create a deep copy
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(value);
            oos.flush();
            oos.close();
            bos.close();

            ByteArrayInputStream bais = new ByteArrayInputStream(bos.toByteArray());
            return new ObjectInputStream(bais).readObject();
        } catch (ClassNotFoundException | IOException ex) {
            log.warn(ex.getMessage(), ex);
            throw new HibernateException(ex);
        }
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(final Object value) throws HibernateException {
        return (Serializable) this.deepCopy(value);
    }

    @Override
    public Object assemble(final Serializable cached, final Object owner) throws HibernateException {
        return this.deepCopy(cached);
    }

    @Override
    public Object replace(final Object original, final Object target, final Object owner) throws HibernateException {
        return this.deepCopy(original);
    }

    @Override
    public boolean equals(final Object obj1, final Object obj2) throws HibernateException {
        return Objects.equals(obj1, obj2);
    }

    @Override
    public int hashCode(final Object obj) throws HibernateException {
        return obj.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names,
                              SharedSessionContractImplementor sharedSessionContractImplementor,
                              Object o) throws HibernateException, SQLException {
        String cellContent = rs.getString(names[0]);

        if (cellContent == null) {
            return null;
        }

        if (cellContent.charAt(0) == '"' && cellContent.charAt(cellContent.length() - 1) == '"') {
            /* H2 Json type */
            cellContent = cellContent.substring(1, cellContent.length() - 1);
            cellContent = StringEscapeUtils.unescapeJson(cellContent);
        }

        try {
            TypeReference typeReference = new TypeReference<Map>() {
            };
            if (cellContent.length() > 0) {
                // [ {...}, ... ]
                if (cellContent.charAt(0) == '['
                        && cellContent.charAt(cellContent.length() - 1) == ']') {
                    typeReference = new TypeReference<List<Map>>() {
                    };
                }
            }

            return mapper.readValue(cellContent, typeReference);
        } catch (final Exception ex) {
            log.warn(ex.getMessage(), ex);
            return cellContent;
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement ps,
                            Object value,
                            int idx,
                            SharedSessionContractImplementor sharedSessionContractImplementor)
            throws HibernateException, SQLException {
        if (value == null) {
            ps.setNull(idx, Types.OTHER);
            return;
        }
        try {
            String json = mapper.writeValueAsString(value);
            ps.setObject(idx, json);
        } catch (final Exception ex) {
            log.warn(ex.getMessage(), ex);
            throw new RuntimeException("Failed to convert Invoice to String: " + ex.getMessage(), ex);
        }
    }
}
