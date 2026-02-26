package br.com.dbank.exception;

public class SenhaIncorretaException extends RuntimeException {
    public SenhaIncorretaException() {
        super("A senha informada está incorreta. Operação cancelada.");
    }
}