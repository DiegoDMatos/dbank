package br.com.dbank.model;

import java.math.BigDecimal;

public class ContaCorrente extends Conta{

    @Override
    public void sacar(BigDecimal valor) {

    }

    @Override
    public void depositar(BigDecimal valor) {

    }

    private void transferir(BigDecimal valor, Conta conta) {
        if(valor.compareTo(conta.getSaldo()) == 0){
            throw new RuntimeException("Saldo insuficiente");
        }  else if (valor.compareTo(conta.getSaldo()) == 1){

        }

    }
}
