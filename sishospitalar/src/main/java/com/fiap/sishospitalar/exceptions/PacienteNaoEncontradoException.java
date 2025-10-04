package com.fiap.sishospitalar.exceptions;

public class PacienteNaoEncontradoException extends RuntimeException {
	private static final long serialVersionUID = -9090910006917962505L;

	public PacienteNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
}
