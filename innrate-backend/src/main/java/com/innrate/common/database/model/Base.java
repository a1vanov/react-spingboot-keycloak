package com.innrate.common.database.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import com.innrate.common.database.BaseListener;
import com.innrate.common.database.type.ArrayDoubleType;
import com.innrate.common.database.type.ArrayStringsType;
import com.innrate.common.database.type.JsonType;
import com.innrate.common.database.type.ListEnumType;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@TypeDefs({
        @TypeDef(
                name = "JsonType",
                typeClass = JsonType.class
        ),
        @TypeDef(
                name = "ArrayStringsType",
                typeClass = ArrayStringsType.class,
                defaultForType = String[].class
        ),
        @TypeDef(
                name = "ArrayDoubleType",
                typeClass = ArrayDoubleType.class,
                defaultForType = Double[].class
        ),
        @TypeDef(
                name = "ListEnumType",
                typeClass = ListEnumType.class
        )
})
@Getter
@Setter
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@EntityListeners(BaseListener.class)
public abstract class Base {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @org.hibernate.annotations.Type(type = "pg-uuid")
    @Column(name = "id")
    private UUID id;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "updated_date")
    private Instant updatedDate;

    @Override
    public String toString() {
        return id != null ? id.toString() : "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }

        if (o == null || getClass() != o.getClass()){
            return false;
        }

        Base base = (Base) o;
        return Objects.equals(getId(), base.getId())
                && Objects.equals(getCreatedDate(), base.getCreatedDate())
                && Objects.equals(getUpdatedDate(), base.getUpdatedDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCreatedDate(), getUpdatedDate());
    }
}
