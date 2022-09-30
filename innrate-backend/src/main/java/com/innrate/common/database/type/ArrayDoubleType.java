package com.innrate.common.database.type;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
public class ArrayDoubleType extends ArrayType {

	public ArrayDoubleType() {
		super("float8");
	}

	@Override
	public Object deepCopy(final Object o){
		return o == null ? null : ((Double[]) o).clone();
	}

	@Override
	public boolean equals(final Object x, final Object y) {
		return Arrays.equals((Double[]) x, (Double[]) y);
	}

	@Override
	public int hashCode(final Object o) throws HibernateException {
		return o == null ? 0 : Arrays.hashCode((Double[]) o);
	}

	@Override
	public Class<Double[]> returnedClass() {
		return Double[].class;
	}
}
