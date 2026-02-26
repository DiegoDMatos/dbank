package br.com.dbank.exception;

public class ContaNaoEncontradaException extends RuntimeException {
    public ContaNaoEncontradaException(String numero) {
        super("A conta número " + numero + " não foi encontrada no DBank.");
    }
}