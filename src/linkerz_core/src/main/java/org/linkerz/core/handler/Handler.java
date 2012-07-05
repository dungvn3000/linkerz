/*
 * Copyright (C) 2012 - 2013 LinkerZ
 */

package org.linkerz.core.handler;

import org.linkerz.core.job.Job;

/**
 * The Class Handler.
 *
 * @author Nguyen Duc Dung
 * @since 7/4/12, 2:01 PM
 */
public interface Handler<J extends Job> {

    /**
     * Check whether this handler is for this class or not.
     * @param clazz
     * @return
     */
    boolean isFor(Class<J> clazz);

    /**
     * Handle the job.
     * @param job
     */
    void handle(J job) throws Exception;
}