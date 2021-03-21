package com.rubicon.waterorder.model;

import javax.persistence.Entity;

public enum Status {
    Initialized, Requested, Started, Delivered, Cancelled
}
