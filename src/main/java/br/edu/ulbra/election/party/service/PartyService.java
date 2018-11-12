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
import java.nio.file.Path;
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
        validateInput(partyInput);
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

        validateInput(partyInput);

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

    private void validateInput(PartyInput input) {
        if (input.getCode() == null
                || (input.getCode().isEmpty()
                || input.getCode().length() > 10)
                || !validateCode(input.getCode()))
            throw new GenericOutputException("Invalid code");

        if (input.getName() == null
                || (input.getName().isEmpty()
                || input.getName().length() < 5))
            throw new GenericOutputException("Invalid name");

        if (input.getNumber() == null
                || input.getNumber().toString().length() != 2
                || !validateNumber(input.getNumber()))
            throw new GenericOutputException("Invalid number");
    }

    public boolean validateCode(String code) {
        Type partyOutputListType = new TypeToken<List<Party>>() {
        }.getType();

        List<Party> parties = modelMapper.map(partyRepository.findAll(), partyOutputListType);
        for (Party party : parties)
            if (party.getCode().equals(code))
                return false;

        return true;
    }

    public boolean validateNumber(Integer number) {
        Type partyOutputListType = new TypeToken<List<Party>>() {
        }.getType();

        List<Party> parties = modelMapper.map(partyRepository.findAll(), partyOutputListType);
        for (Party party : parties)
            if (party.getNumber().equals(number))
                return false;

        return true;
    }


}
