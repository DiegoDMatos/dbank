package br.com.dbank.model;

import java.math.BigDecimal;

public class ContaCorrente extends Conta{

    @Override
    void sacar(BigDecimal valor) {

    }

    @Override
    void depositar(BigDecimal valor) {

    }

    private void transferir(BigDecimal valor, Conta conta) {
        if(valor.compareTo(conta.getSaldo()) == 0){
            throw new RuntimeException("Saldo insuficiente");
        }  else if (valor.compareTo(conta.getSaldo()) == 1){

        }

    }
}
