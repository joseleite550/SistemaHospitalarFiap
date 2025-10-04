package com.fiap.sishospitalar.exceptions;

public class MedicoNaoEncontradoException extends RuntimeException {
	private static final long serialVersionUID = 9187810966493865218L;

	public MedicoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
}
