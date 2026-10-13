package com.jamersc.springboot.hcm_api.service.security;

import com.jamersc.springboot.hcm_api.exception.InvalidRequestException;
import org.hashids.Hashids;
import org.springframework.stereotype.Component;

@Component
public class HashidService {

    private final Hashids hashids;

    public HashidService(Hashids hashids) {
        this.hashids = hashids;
    }

    public String encode(Long id) {
        if (id == null) return null;
        return hashids.encode(id);
    }

    public Long decode(String hash) {
        if (hash == null || hash.trim().isEmpty()) return null;
        long[] decoded = hashids.decode(hash.trim());
        if (decoded.length == 0) {
            throw new InvalidRequestException("The security signature for this identifier is invalid or corrupted.");
        }
        return decoded[0];
    }
}
