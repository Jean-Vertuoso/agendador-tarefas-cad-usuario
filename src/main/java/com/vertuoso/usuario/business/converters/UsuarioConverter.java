package com.vertuoso.usuario.business.converters;

import java.util.ArrayList;
import java.util.List;

import com.vertuoso.usuario.business.dto.TelefoneDTO;
import com.vertuoso.usuario.infrastructure.entities.Telefone;
import org.springframework.stereotype.Component;

import com.vertuoso.usuario.business.dto.UsuarioDTO;
import com.vertuoso.usuario.business.dto.EnderecoDTO;
import com.vertuoso.usuario.infrastructure.entities.Endereco;
import com.vertuoso.usuario.infrastructure.entities.Usuario;

@Component
public class UsuarioConverter {

    // CONVERTER DE USUARIODTO PARA USUARIO
    public Usuario paraUsuario(UsuarioDTO usuarioDTO){
        Usuario usuario = new Usuario();
        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setSenha(usuarioDTO.getSenha());
        usuario.setEnderecos(paraListaEndereco(usuarioDTO.getEnderecos()));
        usuario.setTelefones(paraListaTelefone(usuarioDTO.getTelefones()));

        return usuario;
    }

    public List<Endereco> paraListaEndereco(List<EnderecoDTO> enderecoDTOS){
        return enderecoDTOS.stream().map(this::paraEndereco).toList();
    }

    public Endereco paraEndereco(EnderecoDTO enderecoDTO){
        Endereco endereco = new Endereco();
        endereco.setRua(enderecoDTO.getRua());
        endereco.setNumero(enderecoDTO.getNumero());
        endereco.setCidade(enderecoDTO.getCidade());
        endereco.setComplemento(enderecoDTO.getComplemento());
        endereco.setCep(enderecoDTO.getCep());
        endereco.setEstado(enderecoDTO.getEstado());
        return endereco;
    }

    public List<Telefone> paraListaTelefone(List<TelefoneDTO> telefoneDTOS){
        return telefoneDTOS.stream().map(this::paraTelefone).toList();
    }

    public Telefone paraTelefone(TelefoneDTO telefoneDTO){
        Telefone telefone = new Telefone();
        telefone.setDdd(telefoneDTO.getDdd());
        telefone.setNumero(telefoneDTO.getNumero());
        return telefone;
    }

    // CONVERTER DE USUARIO PARA USUARIODTO
    public UsuarioDTO paraUsuarioDTO(Usuario usuario){
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNome(usuario.getNome());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setSenha(usuario.getSenha());
        usuarioDTO.setEnderecos(paraListaEnderecoDTO(usuario.getEnderecos()));
        usuarioDTO.setTelefones(paraListaTelefoneDTO(usuario.getTelefones()));

        return usuarioDTO;
    }

    public List<EnderecoDTO> paraListaEnderecoDTO(List<Endereco> endereco){
        return endereco.stream().map(this::paraEnderecoDTO).toList();
    }

    public EnderecoDTO paraEnderecoDTO(Endereco endereco){
        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setRua(endereco.getRua());
        enderecoDTO.setNumero(endereco.getNumero());
        enderecoDTO.setCidade(endereco.getCidade());
        enderecoDTO.setComplemento(endereco.getComplemento());
        enderecoDTO.setCep(endereco.getCep());
        enderecoDTO.setEstado(endereco.getEstado());
        return enderecoDTO;
    }

    public List<TelefoneDTO> paraListaTelefoneDTO(List<Telefone> telefone){
        return telefone.stream().map(this::paraTelefoneDTO).toList();
    }

    public TelefoneDTO paraTelefoneDTO(Telefone telefone){
        TelefoneDTO telefoneDTO = new TelefoneDTO();
        telefoneDTO.setDdd(telefone.getDdd());
        telefoneDTO.setNumero(telefone.getNumero());
        return telefoneDTO;
    }
}
