package cambio.simulator.entities.patterns;

import cambio.simulator.entities.microservice.MicroserviceInstance;
import desmoj.core.simulator.Model;

/**
 * Represents a resilience pattern that is owned by a {@link MicroserviceInstance}.
 */
public abstract class InstanceOwnedPattern extends Pattern {

    protected final MicroserviceInstance owner;

    public InstanceOwnedPattern(Model model, String name, boolean showInTrace, MicroserviceInstance owner) {
        super(model, name, showInTrace);
        this.owner = owner;
    }

}
