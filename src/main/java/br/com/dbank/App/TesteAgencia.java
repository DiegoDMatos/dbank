package br.com.dbank.App;

import br.com.dbank.model.Agencia;
import br.com.dbank.repository.SqlAgenciaRepository;

public class TesteAgencia {
    public static void main(String[] args) {
        SqlAgenciaRepository repo = new SqlAgenciaRepository();

        Agencia ag = new Agencia();
        // Use um número bem alto e aleatório para evitar duplicatas
        ag.setNomeAgencia("Agência Cedro");
        ag.setEndereco("Rua do IFCE, 123");
        ag.setTelefone("8599999999");

        try {
            repo.insertAgencia(ag);
            System.out.println("✅ SUCESSO: Agência " + ag.getCodigoAgencia() + " inserida!");
        } catch (Exception e) {
            System.err.println("❌ FALHA AO INSERIR:");
            e.printStackTrace(); // Isso vai nos mostrar o motivo real no console
        }
    }
}
