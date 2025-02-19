package com.vertuoso.usuario.business.services;

import com.vertuoso.usuario.business.converters.UsuarioConverter;
import com.vertuoso.usuario.business.dto.UsuarioDTO;
import com.vertuoso.usuario.infrastructure.entities.Usuario;
import org.springframework.stereotype.Service;

import com.vertuoso.usuario.infrastructure.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;
    private UsuarioConverter usuarioConverter;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioConverter usuarioConverter) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioConverter = usuarioConverter;
    }

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(
                usuarioRepository.save(usuario)
        );
    }
}
