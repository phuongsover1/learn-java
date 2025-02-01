package com.packt.modern.api.scalar;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsRuntimeWiring;
import graphql.schema.idl.RuntimeWiring;

import static graphql.scalars.ExtendedScalars.GraphQLBigDecimal;

@DgsComponent
public class BigDecimalScalar {

    @DgsRuntimeWiring
    public RuntimeWiring.Builder addScalar(RuntimeWiring.Builder builder) {
        return builder.scalar(GraphQLBigDecimal);
    }
}
