package com.vertuoso.usuario.business.services;

import com.vertuoso.usuario.business.dto.ViaCepDTO;
import com.vertuoso.usuario.infrastructure.clients.ViaCepClient;
import com.vertuoso.usuario.infrastructure.exceptions.IllegalArgumentException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ViaCepService {

    private final ViaCepClient client;

    public ViaCepService(ViaCepClient client) {
        this.client = client;
    }

    public ViaCepDTO buscarDadosEndereco(String cep) {
        return client.buscaDadosEndereco(processarCep(cep));
    }

    private String processarCep(String cep){
        String cepFormatado = cep.replace(" ", "")
                                .replace("-", "");

        if(!cepFormatado.matches("\\d+") || !Objects.equals(cepFormatado.length(), 8)){
            throw new IllegalArgumentException("O cep contém caracteres inválidos. Favor verificar");
        }

        return cepFormatado;
    }
}
