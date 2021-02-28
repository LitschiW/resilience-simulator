package de.rss.fachstudie.MiSim.resources;

import de.rss.fachstudie.MiSim.entities.MessageObject;
import de.rss.fachstudie.MiSim.entities.Operation;
import de.rss.fachstudie.MiSim.entities.microservice.Microservice;
import de.rss.fachstudie.MiSim.models.MainModel;
import desmoj.core.simulator.Entity;
import desmoj.core.simulator.EventOf3Entities;
import desmoj.core.simulator.Model;

/**
 * A Thread describes a part of a microservice instance. This thread can performs work in form of operations.
 * <p>
 * id:  the service id it belongs to tid: the thread id (map to the number of existing threads in the service)
 */
public class Thread extends Entity {
    private int id;
    private int sid;
    private int tid;
    private int demand;
    private EventOf3Entities<Microservice, Thread, MessageObject> endEvent;
    private Microservice service;
    private MessageObject mobject;
    private double creationTime;
    private boolean isBlocked;
    private Operation operation;


    public Thread(Model owner, String name, boolean b, int demand, EventOf3Entities<Microservice, Thread, MessageObject> end, Microservice service, MessageObject mo, Operation operation) {
        super(owner, name, b);

        this.id = service.getId();
        this.sid = service.getSid();
//        this.tid = MainModel.serviceCPU.get(service.getId()).get(service.getSid()).getExistingThreads().size();
        this.demand = demand;
        this.endEvent = end;
        this.service = service;
        this.mobject = mo;
        this.operation = operation;
        creationTime = getModel().presentTime().getTimeAsDouble();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public double getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(double creationTime) {
        this.creationTime = creationTime;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        this.isBlocked = blocked;
    }

    public int getDemand() {
        return this.demand;
    }

    public void subtractDemand(double value) {
        if (demand - value > 0)
            this.demand -= value;
        else
            demand = 0;
    }

    public EventOf3Entities<Microservice, Thread, MessageObject> getEndEvent() {
        return endEvent;
    }

    public void setEndEvent(EventOf3Entities<Microservice, Thread, MessageObject> endEvent) {
        this.endEvent = endEvent;
    }

    public Microservice getService() {
        return service;
    }

    public void setService(Microservice service) {
        this.service = service;
    }

    public MessageObject getMobject() {
        return mobject;
    }

    public void setMobject(MessageObject mobject) {
        this.mobject = mobject;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public void scheduleEndEvent() {
        endEvent.schedule(service, this, mobject);
    }
}
