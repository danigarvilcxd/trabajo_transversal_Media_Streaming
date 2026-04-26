package com.example.MediaStreaming.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	@GetMapping("/api/health")
	public String health() {
		return "OK";
	}
}
