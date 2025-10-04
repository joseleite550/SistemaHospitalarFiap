package com.fiap.sishospitalar.handles;
import java.util.Map;

import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

import com.fiap.sishospitalar.exceptions.ConsultaNaoEncontradaException;
import com.fiap.sishospitalar.exceptions.MedicoNaoEncontradoException;
import com.fiap.sishospitalar.exceptions.PacienteNaoEncontradoException;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import io.jsonwebtoken.JwtException;

@Component
public class GraphQlExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {

        if (ex instanceof ConsultaNaoEncontradaException || ex instanceof MedicoNaoEncontradoException || ex instanceof PacienteNaoEncontradoException) {
            return GraphqlErrorBuilder.newError(env)
                    .message(ex.getMessage())
                    .extensions(Map.of("code", "NOT_FOUND"))
                    .build();
        }
        
        if (ex instanceof JwtException) {
            return GraphqlErrorBuilder.newError(env)
                    .message("Token inválido ou expirado")
                    .extensions(Map.of("code", "UNAUTHORIZED"))
                    .build();
        }
 

        return GraphqlErrorBuilder.newError(env)
                .message("Erro interno: " + ex.getMessage())
                .extensions(Map.of("code", "INTERNAL_ERROR"))
                .build();
    }
}
