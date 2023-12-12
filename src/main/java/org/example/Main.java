package org.example;

import org.example.Controller.UsuarioController;
import org.example.Dto.UsuarioDTOInput;
import org.example.Service.UsuarioService;

import static spark.Spark.awaitInitialization;

public class Main {
    public static void main(String[] args) {
        UsuarioService usuarioService = new UsuarioService();
        UsuarioController usuarioController = new UsuarioController(usuarioService);

        awaitInitialization();
    }
}

