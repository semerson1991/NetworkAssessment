package com.semerson.networkassessment.activities.Results;

import com.semerson.networkassessment.storage.results.VulnerabilityResult;

public interface ResultCallback {
    /**
     * Callback function for returning a value from the desired function called from the Vulnerability Result Object
     */
    String getResult(VulnerabilityResult vulnerabilityResult);
}
