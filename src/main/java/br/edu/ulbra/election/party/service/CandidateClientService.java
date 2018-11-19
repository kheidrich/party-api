package br.edu.ulbra.election.party.service;

import br.edu.ulbra.election.party.client.CandidateClient;
import br.edu.ulbra.election.party.output.v1.CandidateOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateClientService {
    private final CandidateClient candidateClient;

    @Autowired
    public CandidateClientService(CandidateClient candidateClient) {
        this.candidateClient = candidateClient;
    }

    public List<CandidateOutput> getByPartyId(Long partyId){
        return this.candidateClient.getByPartyId(partyId);
    }

}
