package de.rss.fachstudie.MiSim.entities.networking;

import de.rss.fachstudie.MiSim.entities.microservice.MicroserviceInstance;
import desmoj.core.simulator.Model;

/**
 * @author Lion Wagner
 */
public class InternalRequest extends Request {

    private final MicroserviceInstance sender;

    public InternalRequest(Model model, boolean showInTrace, NetworkDependency dependency, MicroserviceInstance sender) {
        super(model,
                String.format("Cascading Request %s(%s)", dependency.getTarget_op().getOwner(), dependency.getTarget_op()),
                showInTrace,
                dependency.getParent_request(),
                dependency.getTarget_op());
        dependency.updateChild_request(this);
        this.sender = sender;
    }

    public MicroserviceInstance getSender() {
        return sender;
    }

}