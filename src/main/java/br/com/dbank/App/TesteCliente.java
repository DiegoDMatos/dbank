package br.com.dbank.App;

import br.com.dbank.model.Cliente;
import br.com.dbank.repository.SqlClienteRepository;

import java.time.LocalDate;

public class TesteCliente {
    public static void main(String[] args) {
        SqlClienteRepository repo = new SqlClienteRepository();


        Cliente c = new Cliente();
        c.setNome("João do IFCE");
        c.setCpf("999.888.777-00");
        c.setDataNascimento(LocalDate.of(1995, 5, 20)); // Evita o NullPointerException

        int id = repo.insert(c, "senha123");
        System.out.println("Cliente inserido com ID: " + id);

    }
}
