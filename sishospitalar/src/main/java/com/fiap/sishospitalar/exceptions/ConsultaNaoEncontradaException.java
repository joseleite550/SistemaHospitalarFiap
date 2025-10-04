package com.fiap.sishospitalar.exceptions;

public class ConsultaNaoEncontradaException extends RuntimeException {
	private static final long serialVersionUID = 6694548656938972074L;

	public ConsultaNaoEncontradaException(String mensagem) {
		super(mensagem);
	}
}
