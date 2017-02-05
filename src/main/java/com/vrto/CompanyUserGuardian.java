package com.vrto;

import org.springframework.stereotype.Component;

import java.util.Map;

import static com.vrto.Ids.KNOWN_USER_DIFFERENT_COMPANY;
import static com.vrto.Ids.UNKNOWN_COMPANY;
import static com.vrto.Ids.UNKNOWN_USER;

/**
 * Guards {@code companyId} and {@code userId} URL variables.
 * Vetoes access (throws {@link AccessViolationException}) if:
 * <ul>
 *     <li>{@code companyId} does not exist within the system</li>
 *     <li>{@code userId} does not exist within the system</li>
 *     <li>{@code companyId + userId} combination is invalid (both may exist, bu the user belongs to an another company)</li>
 * </ul>
 */
@Component
public class CompanyUserGuardian extends UrlParameterGuardian {

    @Override
    protected void checkUrlVariables(Map<String, Object> parameters) {
        if (shouldCheckCompanyAndUser(parameters)) {
            checkAccess((Long) parameters.get("companyId"), (Long) parameters.get("userId"));
        }
    }

    private boolean shouldCheckCompanyAndUser(Map<String, Object> parameters) {
        return parameters.containsKey("companyId") && parameters.containsKey("userId");
    }

    private void checkAccess(long companyId, long userId) {
        // could be bunch of repository/validator calls in the real world
        if (companyId == UNKNOWN_COMPANY) {
            throw new AccessViolationException("Unknown company: " + companyId);
        }
        if (userId == UNKNOWN_USER || userId == KNOWN_USER_DIFFERENT_COMPANY) {
            throw new AccessViolationException("Unknown user: " + userId);
        }
    }
}
