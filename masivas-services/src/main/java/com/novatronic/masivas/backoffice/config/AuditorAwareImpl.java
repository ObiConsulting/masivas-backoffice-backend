package com.novatronic.masivas.backoffice.config;

import com.novatronic.masivas.backoffice.security.model.UserContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;

/**
 *
 * @author Obi Consulting
 */
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // your custom logic
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserContext user;
        if (auth != null) {
            user = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return Optional.ofNullable(user.getUsername());
        } else {
            return Optional.ofNullable(null);
        }

    }

}
