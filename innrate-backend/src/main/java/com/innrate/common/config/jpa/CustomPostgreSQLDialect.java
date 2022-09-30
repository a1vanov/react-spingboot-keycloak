package com.innrate.common.config.jpa;

import org.hibernate.dialect.PostgreSQL9Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Component;

import java.sql.Types;

@Component
public class CustomPostgreSQLDialect extends PostgreSQL9Dialect {
    public CustomPostgreSQLDialect() {
        this.registerFunction("make_interval", new StandardSQLFunction("make_interval", StandardBasicTypes.TIMESTAMP));
        this.registerFunction("make_timestamp", new StandardSQLFunction("make_timestamp", StandardBasicTypes.TIMESTAMP));
        this.registerFunction("make_timestamptz", new StandardSQLFunction("make_timestamptz", StandardBasicTypes.TIMESTAMP));
        this.registerFunction("make_date", new StandardSQLFunction("make_date", StandardBasicTypes.DATE));
        this.registerFunction("make_time", new StandardSQLFunction("make_time", StandardBasicTypes.TIME));
        this.registerColumnType(Types.ARRAY, "float8[]");
        this.registerColumnType(Types.ARRAY, "text[]");
        this.registerColumnType(Types.JAVA_OBJECT, "jsonb");
    }
}