/*
 * Copyright (C) 2012 - 2013 LinkerZ
 */

package org.linkerz.core.job;

import org.linkerz.core.callback.CallBack;
import org.linkerz.core.config.Configurable;
import org.linkerz.core.config.JobConfig;

import java.io.Serializable;

/**
 * The Class Job.
 *
 * @author Nguyen Duc Dung
 * @since 7/3/12, 3:11 AM
 */
public interface Job<R> extends Serializable, Configurable<JobConfig> {

    public static final String JOB_QUEUE = "jobQueue";

    /**
     * Get JobCallback.
     * @return
     */
    CallBack<R> getCallBack();

    /**
     * Return result of this job.
     * @return
     */
    R getResult();

    @Override
    JobConfig getConfig();

    @Override
    void setConfig(JobConfig config);
}
