package com.raysofthesun.poswebjava.propose.controllers;

import com.raysofthesun.poswebjava.propose.models.prospect.Prospect;
import com.raysofthesun.poswebjava.propose.services.ProspectService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/propose/prospect")
public class ProspectController {
	final protected ProspectService prospectService;

	public ProspectController(ProspectService prospectService) {
		this.prospectService = prospectService;
	}

	@PutMapping("/add")
	public Mono<String> addProspect(@RequestBody Prospect prospect) {
		return this.prospectService.addProspect(prospect);
	}

	@GetMapping("/{prospectId}")
	public Mono<Prospect> getProspectById(@PathVariable String prospectId) {
		return prospectService.getProspectById(prospectId);
	}

	@DeleteMapping("/{prospectId}")
	public Mono<String> deleteProspectById(@PathVariable String prospectId) {
		return prospectService.deleteProspectById(prospectId);
	}
}
