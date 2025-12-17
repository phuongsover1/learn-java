package com.manning.javapersistence.springdatajpa.converter;

import com.manning.javapersistence.springdatajpa.model.MonetaryAmount;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Currency;
import java.util.Properties;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.UserType;

public class MonetaryAmountUserType implements CompositeUserType, DynamicParameterizedType {

  private Currency convertTo;

  @Override
  public void setParameterValues(Properties properties) {
    String convertToParameter = properties.getProperty("convertTo");
    this.convertTo = Currency.getInstance(convertToParameter != null ? convertToParameter : "USD");
  }

  @Override
  public Class returnedClass() {
    return MonetaryAmount.class;
  }

  @Override
  public boolean isMutable() {
    return false;
  }

  @Override
  public Object deepCopy(Object value) {
    return value;
  }

  @Override
  public boolean equals(Object x, Object y) {
    return x == y || !(x == null || y == null) && x.equals(y);
  }

  @Override
  public int hashCode(Object x) {
    return x.hashCode();
  }


  public MonetaryAmount convert(MonetaryAmount amount, Currency toCurrency) {
    // multiply 2 for testing whether this convert function is called
    return new MonetaryAmount(amount.getAmount().multiply(new BigDecimal(2)), toCurrency);
  }

  public String[] getPropertyNames() {
    return new String[] {"value", "currency"};
  }

  @Override
  public Object getPropertyValue(Object component, int property) throws HibernateException {
    MonetaryAmount monetaryAmount = (MonetaryAmount) component;
    if (property == 0) return monetaryAmount.getAmount();
    else return monetaryAmount.getCurrency();
  }

  @Override
  public Type[] getPropertyTypes() {
    return new Type[] {
            StandardBasicTypes.BIG_DECIMAL,
            StandardBasicTypes.CURRENCY
    };
  }

  @Override
  public void setPropertyValue(Object o, int i, Object o1) throws HibernateException {
    throw new UnsupportedOperationException("MonetaryAmount is immutable");
  }

  @Override
  public Object nullSafeGet(ResultSet resultSet, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
    BigDecimal amount = resultSet.getBigDecimal(names[0]);
    if (resultSet.wasNull())
      return null;
    Currency currency =
            Currency.getInstance(resultSet.getString(names[1]));
    return new MonetaryAmount(amount, currency);
  }

  @Override
  public void nullSafeSet(PreparedStatement statement,
                          Object value,
                          int index,
                          SharedSessionContractImplementor session) throws SQLException {

    if (value == null) {
      statement.setNull(
              index,
              StandardBasicTypes.BIG_DECIMAL.sqlType());
      statement.setNull(
              index + 1,
              StandardBasicTypes.CURRENCY.sqlType());
    } else {
      MonetaryAmount amount = (MonetaryAmount) value;
      // When saving, convert to target currency
      MonetaryAmount dbAmount = convert(amount, convertTo);
      statement.setBigDecimal(index, dbAmount.getAmount());
      statement.setString(index + 1, convertTo.getCurrencyCode());
    }
  }

  @Override
  public Serializable disassemble(Object value, SharedSessionContractImplementor session) throws HibernateException {
    return value.toString();
  }

  @Override
  public Object assemble(Serializable cached, SharedSessionContractImplementor session, Object owner) throws HibernateException {
    return MonetaryAmount.fromString((String) cached);
  }

  @Override
  public Object replace(Object original, Object target, SharedSessionContractImplementor session, Object owner) throws HibernateException {
    return original;
  }
}
