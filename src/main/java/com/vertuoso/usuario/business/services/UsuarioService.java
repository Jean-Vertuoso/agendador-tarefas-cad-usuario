package com.vertuoso.usuario.business.services;

import com.vertuoso.usuario.infrastructure.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vertuoso.usuario.business.converters.UsuarioConverter;
import com.vertuoso.usuario.business.dto.UsuarioDTO;
import com.vertuoso.usuario.infrastructure.entities.Usuario;
import com.vertuoso.usuario.infrastructure.exceptions.ConflictException;
import com.vertuoso.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.vertuoso.usuario.infrastructure.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;
    private UsuarioConverter usuarioConverter;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioConverter usuarioConverter, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioConverter = usuarioConverter;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(
                usuarioRepository.save(usuario)
        );
    }

    public void emailExiste(String email){
        try {
            boolean existe = verificaEmailExistente(email);
            if(existe){
                throw new ConflictException("Email ja cadastrado" + email);
            }
        }catch(ConflictException e){
            throw new ConflictException("Email ja cadastrado", e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email){
        return usuarioRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public Usuario buscaUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("E-mail não encontrado: " + email)
        );
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deletaUsuarioPorEmail(String email){
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto){
        //Aqui buscamos o email do usuário através do token (tirar a obrigatoriedade do email)
        String email = jwtUtil.extrairEmailToken(token.substring(7));

        //Criptografia de senha
        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null);

        //Busca os dados do usuário no banco de dados
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
            new ResourceNotFoundException("E-mail não localizado"));

        //Mesclou os dados que recebemos na requisição DTO com os dados do banco de dados
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);

        //Salvou os dados do usuário convertido e depois pegou o retorno e converteu para UsuarioDTO.
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }
}
