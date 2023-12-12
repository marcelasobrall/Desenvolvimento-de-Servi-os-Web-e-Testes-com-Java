package org.example.Service;

import org.example.Dto.UsuarioDTOInput;
import org.example.Dto.UsuarioDTOOutput;
import org.example.Model.Usuario;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UsuarioService {

    private List<Usuario> listaUsuarios = new ArrayList<>();
    private ModelMapper modelMapper = new ModelMapper();

    public List<UsuarioDTOOutput> listarUsuarios() {
        return listaUsuarios.stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioDTOOutput.class))
                .collect(Collectors.toList());
    }

    public void inserirUsuario(UsuarioDTOInput usuarioDTOInput) {
        Usuario usuario = modelMapper.map(usuarioDTOInput, Usuario.class);
        listaUsuarios.add(usuario);
    }

    public void alterarUsuario(Integer id, UsuarioDTOInput usuarioDTOInput) {
        Optional<Usuario> usuarioExistente = buscarUsuarioPorId(usuarioDTOInput.getId());

        if (usuarioExistente.isPresent()) {
            Usuario usuarioAtualizado = modelMapper.map(usuarioDTOInput, Usuario.class);
            listaUsuarios.set(listaUsuarios.indexOf(usuarioExistente.get()), usuarioAtualizado);
        }
    }

    public Optional<UsuarioDTOOutput> buscarUsuarioDTOOutputPorId(Integer id) {
        return buscarUsuarioPorId(id)
                .map(usuario -> modelMapper.map(usuario, UsuarioDTOOutput.class));
    }
    public Optional<Usuario> buscarUsuarioPorId(Integer id) {
        return listaUsuarios.stream()
                .filter(usuario -> usuario.getId().equals(id))
                .findFirst();
    }

    public void excluirUsuario(Integer id) {
        buscarUsuarioPorId(id).ifPresent(listaUsuarios::remove);
    }
}