package org.example.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Dto.UsuarioDTOInput;
import org.example.Dto.UsuarioDTOOutput;
import org.example.Model.Usuario;
import org.example.Service.UsuarioService;
import spark.Response;

import java.util.HashMap;

import spark.Request;

import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class UsuarioController {

    private final UsuarioService usuarioService;
    private final ObjectMapper objectMapper;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        this.objectMapper = new ObjectMapper();

        inicializarEndpoints();
    }

    private void inicializarEndpoints() {
        get("/usuarios", this::listarUsuarios, objectMapper::writeValueAsString);

        get("/usuarios/:id", this::buscarUsuarioPorId, objectMapper::writeValueAsString);

        post("/usuarios", this::inserirUsuario, objectMapper::writeValueAsString);

        delete("/usuarios/:id", this::excluirUsuarioPorId, objectMapper::writeValueAsString);

        put("/usuarios/:id", this::atualizarUsuario, objectMapper::writeValueAsString);
    }

    private Object listarUsuarios(Request request, Response response) {
        try {
            List<UsuarioDTOOutput> usuarios = usuarioService.listarUsuarios();
            response.status(200);
            return objectMapper.writeValueAsString(usuarios);
        } catch (Exception e) {
            response.status(500);
            return criarRespostaErro(e.getMessage());
        }
    }

    private Object buscarUsuarioPorId(Request request, Response response) {
        try {
            String idParam = request.params(":id");
            Integer id = Integer.parseInt(idParam);

            System.out.println("Tentando buscar usuário com ID: " + id);

            UsuarioDTOOutput usuarioDTOOutput = usuarioService.buscarUsuarioDTOOutputPorId(id)
                    .orElse(null);

            if (usuarioDTOOutput != null) {
                return usuarioDTOOutput;
            } else {
                response.status(404);
                return criarRespostaErro("Usuário não encontrado");
            }
        } catch (NumberFormatException e) {
            response.status(400);
            return criarRespostaErro("ID inválido");
        } catch (Exception e) {
            response.status(500);
            return criarRespostaErro(e.getMessage());
        }
    }


    private Object inserirUsuario(Request request, Response response) {
        try {
            UsuarioDTOInput usuarioDTOInput = objectMapper.readValue(request.body(), UsuarioDTOInput.class);
            usuarioService.inserirUsuario(usuarioDTOInput);
            response.status(201);
            return "";
        } catch (Exception e) {
            response.status(500);
            return criarRespostaErro(e.getMessage());
        }
    }

    private Object excluirUsuarioPorId(Request request, Response response) {
        try {
            String idParam = request.params(":id");
            Integer id = Integer.parseInt(idParam);
            usuarioService.excluirUsuario(id);
            response.status(204);
            return "";
        } catch (NumberFormatException e) {
            response.status(400);
            return criarRespostaErro("ID inválido");
        } catch (Exception e) {
            response.status(500);
            return criarRespostaErro(e.getMessage());
        }
    }

    private Object atualizarUsuario(Request request, Response response) {
        try {
            String idParam = request.params(":id");
            Integer id = Integer.parseInt(idParam);

            UsuarioDTOInput usuarioDTOInput = objectMapper.readValue(request.body(), UsuarioDTOInput.class);

            usuarioService.alterarUsuario(id, usuarioDTOInput);

            response.status(200);
            return "";
        } catch (NumberFormatException e) {

            response.status(400);
            return criarRespostaErro("ID inválido");
        } catch (Exception e) {

            response.status(500);
            return criarRespostaErro(e.getMessage());
        }
    }

    private Object criarRespostaErro(String mensagem) {
        return "{\"erro\":\"" + mensagem + "\"}";
    }

}