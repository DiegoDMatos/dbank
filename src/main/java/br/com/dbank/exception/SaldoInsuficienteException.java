package br.com.dbank.exception;

public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException(String messagem) {

        super(messagem);
    }
}
