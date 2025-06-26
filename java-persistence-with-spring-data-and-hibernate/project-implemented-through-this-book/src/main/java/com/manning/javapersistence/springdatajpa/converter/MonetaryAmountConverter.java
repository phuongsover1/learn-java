package com.manning.javapersistence.springdatajpa.converter;

import com.manning.javapersistence.springdatajpa.model.MonetaryAmount;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class MonetaryAmountConverter implements AttributeConverter<MonetaryAmount, String> {

  @Override
  public String convertToDatabaseColumn(MonetaryAmount monetaryAmount) {
    return monetaryAmount.toString();
  }

  @Override
  public MonetaryAmount convertToEntityAttribute(String dbData) {
    return MonetaryAmount.fromString(dbData);
  }

}
