package br.com.dbank.model;

import java.math.BigDecimal;

public abstract class Conta {
    private String titular;
    private String numeroConta;
    private BigDecimal saldo;
    private String agencia;


    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    abstract void sacar(BigDecimal valor);
    abstract void depositar(BigDecimal valor);
}
