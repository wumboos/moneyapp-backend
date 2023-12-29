package com.wumboos.app.moneyapp;

import java.util.UUID;

import org.jmolecules.event.types.DomainEvent;

public record TransactionCreatedEvent(UUID id, String title, String description, boolean published){

}
