package br.edu.ulbra.election.party.service;


import br.edu.ulbra.election.party.exception.GenericOutputException;
import br.edu.ulbra.election.party.input.v1.PartyInput;
import br.edu.ulbra.election.party.model.Party;
import br.edu.ulbra.election.party.output.v1.GenericOutput;
import br.edu.ulbra.election.party.output.v1.PartyOutput;
import br.edu.ulbra.election.party.repository.PartyRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class PartyService {
    private final PartyRepository partyRepository;
    private final ModelMapper modelMapper;

    private static final String MESSAGE_INVALID_ID = "Invalid id";
    private static final String MESSAGE_PARTY_NOT_FOUND = "Party not found";
    private static final String MESSAGE_PARTY_DELETED = "Party deleted";

    @Autowired
    public PartyService(PartyRepository partyRepository, ModelMapper modelMapper) {
        this.partyRepository = partyRepository;
        this.modelMapper = modelMapper;
    }

    public List<PartyOutput> getAll() {
        Type partyOutputListType = new TypeToken<List<PartyOutput>>() {
        }.getType();
        return modelMapper.map(partyRepository.findAll(), partyOutputListType);
    }

    public PartyOutput create(PartyInput partyInput) {
        Party party = modelMapper.map(partyInput, Party.class);
        party = partyRepository.save(party);

        return modelMapper.map(party, PartyOutput.class);
    }

    public PartyOutput getById(Long partyId) {
        if (partyId == null)
            throw new GenericOutputException(MESSAGE_INVALID_ID);

        Party party = partyRepository.findById(partyId).orElse(null);
        if (party == null)
            throw new GenericOutputException(MESSAGE_PARTY_NOT_FOUND);

        return modelMapper.map(party, PartyOutput.class);
    }

    public PartyOutput update(Long partyId, PartyInput partyInput) {
        if (partyId == null)
            throw new GenericOutputException(MESSAGE_INVALID_ID);

        Party party = partyRepository.findById(partyId).orElse(null);
        if (party == null)
            throw new GenericOutputException(MESSAGE_PARTY_NOT_FOUND);

        party.setCode(partyInput.getCode());
        party.setName(partyInput.getName());
        party.setNumber(partyInput.getNumber());
        party = partyRepository.save(party);

        return modelMapper.map(party, PartyOutput.class);
    }

    public GenericOutput delete(Long partyId) {
        if (partyId == null)
            throw new GenericOutputException(MESSAGE_INVALID_ID);

        Party election = partyRepository.findById(partyId).orElse(null);
        if (election == null)
            throw new GenericOutputException(MESSAGE_PARTY_NOT_FOUND);

        partyRepository.delete(election);

        return new GenericOutput(MESSAGE_PARTY_DELETED);
    }
}
