package com.vertuoso.usuario.business.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vertuoso.usuario.business.converters.UsuarioConverter;
import com.vertuoso.usuario.business.dto.EnderecoDTO;
import com.vertuoso.usuario.business.dto.TelefoneDTO;
import com.vertuoso.usuario.business.dto.UsuarioDTO;
import com.vertuoso.usuario.infrastructure.entities.Endereco;
import com.vertuoso.usuario.infrastructure.entities.Telefone;
import com.vertuoso.usuario.infrastructure.entities.Usuario;
import com.vertuoso.usuario.infrastructure.exceptions.ConflictException;
import com.vertuoso.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.vertuoso.usuario.infrastructure.exceptions.UnauthorizedException;
import com.vertuoso.usuario.infrastructure.repositories.EnderecoRepository;
import com.vertuoso.usuario.infrastructure.repositories.TelefoneRepository;
import com.vertuoso.usuario.infrastructure.repositories.UsuarioRepository;
import com.vertuoso.usuario.infrastructure.security.JwtUtil;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UsuarioService(UsuarioRepository usuarioRepository, EnderecoRepository enderecoRepository, TelefoneRepository telefoneRepository, UsuarioConverter usuarioConverter, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.enderecoRepository = enderecoRepository;
        this.telefoneRepository = telefoneRepository;
        this.usuarioConverter = usuarioConverter;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(
                usuarioRepository.save(usuario)
        );
    }

    public String autenticarUsuario(UsuarioDTO usuarioDTO) throws UnauthorizedException {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(),
                            usuarioDTO.getSenha())
            );
            return "Bearer " + jwtUtil.generateToken(authentication.getName());
        } catch(BadCredentialsException | UsernameNotFoundException | AuthorizationDeniedException ex) {
            throw new UnauthorizedException("Usuário ou senha inválidos: ", ex.getCause());
        }
    }

    public void emailExiste(String email){
        try {
            boolean existe = verificaEmailExistente(email);
            if(existe){
                throw new ConflictException("Email ja cadastrado " + email);
            }
        }catch(ConflictException e){
            throw new ConflictException("Email ja cadastrado ", e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email){
        return usuarioRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public UsuarioDTO buscaUsuarioPorEmail(String email) {
        try {
            return usuarioConverter.paraUsuarioDTO(
                    usuarioRepository.findByEmail(email)
                        .orElseThrow(
                            () -> new ResourceNotFoundException("E-mail não encontrado " + email)));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("E-mail não encontrado " + email);
        }
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
            new ResourceNotFoundException("E-mail não localizado " + email));

        //Mesclou os dados que recebemos na requisição DTO com os dados do banco de dados
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);

        //Salvou os dados do usuário convertido e depois pegou o retorno e converteu para UsuarioDTO.
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO){
        Endereco entity = enderecoRepository.findById(idEndereco).orElseThrow(() ->
                new ResourceNotFoundException("Id não encontrado " + idEndereco));

        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO, entity);

        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }

    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO telefoneDTO){
        Telefone entity = telefoneRepository.findById(idTelefone).orElseThrow(() ->
                new ResourceNotFoundException("Id não encontrado " + idTelefone));

        Telefone telefone = usuarioConverter.updateTelefone(telefoneDTO, entity);

        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }

    public EnderecoDTO cadastraEndereco(String token, EnderecoDTO enderecoDTO){
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("E-mail não localizado " + email));

        Endereco endereco = usuarioConverter.paraEnderecoEntity(enderecoDTO, usuario.getId());

        return usuarioConverter.paraEnderecoDTO(
                enderecoRepository.save(endereco));
    }

    public TelefoneDTO cadastraTelefone(String token, TelefoneDTO telefoneDTO){
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("E-mail não localizado " + email));

        Telefone telefone = usuarioConverter.paraTelefoneEntity(telefoneDTO, usuario.getId());

        return usuarioConverter.paraTelefoneDTO(
                telefoneRepository.save(telefone));
    }
}
