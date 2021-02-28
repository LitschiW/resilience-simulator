package de.rss.fachstudie.MiSim.entities.patterns;

import de.rss.fachstudie.MiSim.entities.microservice.MicroserviceInstance;

import java.util.Collection;
import java.util.Comparator;

/**
 * Strategy that chooses the least utilized Microservice Instance by current relative Queue demand.
 */
class UtilizationBalanceStrategy implements LoadBalancingStrategy {

    /**
     * Returns a the instance of the list, which currently has the lowest demand left.
     */
    @Override
    public MicroserviceInstance getNextInstance(Collection<MicroserviceInstance> runningInstances) {
        return runningInstances.stream()
                .min(Comparator.comparingDouble(MicroserviceInstance::getUsage))
                .orElse(null);
    }
}