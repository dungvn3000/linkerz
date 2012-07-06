/*
 * Copyright (C) 2012 - 2013 LinkerZ
 */

package org.linkerz.job.queue.queue;

import org.linkerz.job.queue.job.Job;

/**
 * The Class Queue.
 *
 * @author Nguyen Duc Dung
 * @since 7/6/12, 1:01 AM
 */
public interface Queue<J extends Job> {

    boolean add(J job);

    J next();

    int size();

    void setMaxSize(int maxSize);
}