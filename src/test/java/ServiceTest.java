import org.example.Dto.UsuarioDTOInput;
import org.example.Service.UsuarioService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceTest {

    @Test
    public void testInserirUsuario() {

        UsuarioService usuarioService = new UsuarioService();

        UsuarioDTOInput usuarioDTOInput = new UsuarioDTOInput();
        usuarioDTOInput.setNome("Novo Usuário");
        usuarioDTOInput.setSenha("senha123");

        usuarioService.inserirUsuario(usuarioDTOInput);

        int tamanhoLista = usuarioService.listarUsuarios().size();

        assertEquals(1, tamanhoLista, "A inserção do usuário falhou.");
    }
}
