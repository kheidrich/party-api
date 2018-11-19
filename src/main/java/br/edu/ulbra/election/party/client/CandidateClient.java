package br.edu.ulbra.election.party.client;

import br.edu.ulbra.election.party.output.v1.CandidateOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "candidate-service", url = "${url.candidate-service}")
public interface CandidateClient {

    @GetMapping("/v1/candidate/party/{partyId}")
    List<CandidateOutput> getByPartyId(@PathVariable(name = "partyId") Long partyId);
}
