package br.com.dbank.App;

import br.com.dbank.model.Cliente;
import br.com.dbank.repository.SqlClienteRepository;

import java.time.LocalDate;

public class TesteCliente {
    public static void main(String[] args) {
        SqlClienteRepository repo = new SqlClienteRepository();

        // Testando Inserção com um CPF NOVO para evitar o erro de duplicata
        Cliente c = new Cliente();
        c.setNome("João do IFCE");
        c.setCpf("999.888.777-00"); // Use um CPF que você ainda não usou
        c.setDataNascimento(LocalDate.of(1995, 5, 20)); // Evita o NullPointerException

        repo.insert(c);
        System.out.println("✅ Cliente inserido!");

        // Testando a Busca (ajuste o nome do método conforme sua interface)
        // Cliente buscado = repo.selectByCpf("999.888.777-00");
        // System.out.println("Cliente encontrado: " + buscado.getNome());
    }
}
