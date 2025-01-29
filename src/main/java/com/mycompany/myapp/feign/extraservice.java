package com.mycompany.myapp.feign;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class extraservice {
    
        public static String extractIdFromLocationHeader(ResponseEntity<?> re) {
		String locationHeader = re.getHeaders().get("Location").get(0);
        return locationHeader.substring(locationHeader.lastIndexOf("/")+1);
	}
}
