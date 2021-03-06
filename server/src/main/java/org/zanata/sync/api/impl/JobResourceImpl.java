package org.zanata.sync.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zanata.sync.api.JobResource;
import org.zanata.sync.exception.JobNotFoundException;
import org.zanata.sync.model.JobProgress;
import org.zanata.sync.model.JobStatus;
import org.zanata.sync.model.JobStatusType;
import org.zanata.sync.model.JobSummary;
import org.zanata.sync.model.JobType;
import org.zanata.sync.service.SchedulerService;
import org.zanata.sync.service.WorkService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * @author Alex Eng <a href="mailto:aeng@redhat.com">aeng@redhat.com</a>
 */

@RequestScoped
public class JobResourceImpl implements JobResource {
    private static final Logger log =
            LoggerFactory.getLogger(JobResourceImpl.class);

    @Inject
    private SchedulerService schedulerServiceImpl;

    @Inject
    private WorkService workServiceImpl;

    @Override
    public Response getJobStatus(
        @QueryParam(value = "id") @DefaultValue("") String id,
        @QueryParam(value = "type") @DefaultValue("")
        JobType type) {
        try {
            if(StringUtils.isEmpty(id) || type == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(schedulerServiceImpl
                .getLatestJobStatus(new Long(id), type)).build();
        } catch (SchedulerException e) {
            log.error("get job status error", e);
            return Response.serverError().build();
        } catch (JobNotFoundException e) {
            log.warn("get job status not found", e);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response cancelRunningJob(
        @QueryParam(value = "id") @DefaultValue("") String id,
        @QueryParam(value = "type") @DefaultValue("") JobType type) {
        try {
            if(StringUtils.isEmpty(id)) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            schedulerServiceImpl.cancelRunningJob(new Long(id), type);
            return Response.ok().build();
        } catch (SchedulerException e) {
            log.error("cancel error", e);
            return Response.serverError().build();
        } catch (JobNotFoundException e) {
            log.warn("cancel job not found", e);
            return Response.status(
                    Response.Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response triggerJob(@DefaultValue("") String id,
            @DefaultValue("") JobType type) {
        try {
            if (StringUtils.isEmpty(id)) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            schedulerServiceImpl.triggerJob(new Long(id), type);
            return Response.ok().build();
        } catch (SchedulerException e) {
            log.error("trigger job error", e);
            return Response.serverError().build();
        } catch (JobNotFoundException e) {
            log.warn("job not found", e);
            return Response.status(
                    Response.Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response getJob(
        @QueryParam(value = "id") @DefaultValue("") String id,
        @QueryParam(value = "type") @DefaultValue("") JobType type,
        @QueryParam(value = "status") @DefaultValue("") JobStatusType status) {

        boolean filterByKey = !StringUtils.isEmpty(id) && type != null;

        if((!StringUtils.isEmpty(id) || type != null) && !filterByKey) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try {
            List<JobSummary> jobs = schedulerServiceImpl.getJobs();
            if(status == null && StringUtils.isEmpty(id) && type == null) {
                return Response.ok(jobs).build();
            } else {
                List<JobSummary> filteredList = new ArrayList<>();
                boolean filterByStatus = status != null;

                for (JobSummary summary : jobs) {
                    if (filterByKey && filterByStatus) {
                        JobKey key = type.toJobKey(new Long(id));
                        if (summary.getKey().equals(key.toString())
                                && isMatchStatus(summary.getLastJobStatus(),
                                        status)) {
                            filteredList.add(summary);
                            continue;
                        }
                    } else if (filterByKey) {
                        JobKey key = type.toJobKey(new Long(id));
                        if (summary.getKey().equals(key.toString())) {
                            filteredList.add(summary);
                            continue;
                        }
                    } else if (filterByStatus && isMatchStatus(
                            summary.getLastJobStatus(), status)) {
                        filteredList.add(summary);
                        continue;
                    }
                }
                return Response.ok(filteredList).build();
            }
        } catch (SchedulerException e) {
            log.error("fail getting running jobs", e);
            return Response.serverError().build();
        }
    }

    private boolean isMatchStatus(JobStatus jobStatus, JobStatusType status) {
        if(status.equals(JobStatusType.RUNNING)) {
            JobProgress currentProgress = jobStatus.getCurrentProgress();
            if(currentProgress != null && currentProgress.getStatus().equals(status)) {
                return true;
            } 
        }
        return jobStatus.getStatus().equals(status);
    }
}
