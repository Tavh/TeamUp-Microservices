package com.teamup.project.service_communicator;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.teamup.project.entities.EventEntity;
import com.teamup.project.exceptions.ApplicationException;

@FeignClient("expired-event-service")
public interface ExpiredEventServiceClient{

	@PostMapping("/expired-events")
	public ResponseEntity<String> transferEventToExpiredServer (@RequestBody EventEntity event) throws ApplicationException;
}
