package com.fjsh.dag.flow.dagdata;

import com.fjsh.dag.flow.common.ResultStatus;
import com.fjsh.dag.flow.jobflow.JobFlow;
import com.fjsh.dag.flow.jobflow.JobFlowRunner;
import com.fjsh.dag.flow.jobflow.JobFlowScheduler;

/**
 *
 * @Author: <fujiansheng.com>
 * @Description：
 * @Date: Created in :2019-02-24 20:48  
 * @Modified by:
 */
public class DemoJob extends JobFlowRunner {

    private String name;

    public DemoJob(JobFlow graph, String name) {
        super(graph, name);
        this.name = name;
        this.timeout = 5000000;
    }

    @Override
    public ResultStatus run(JobFlowScheduler scheduler) {
        System.out.println("start job " + name);
        CommonData.putData(name);

//            throw new Exception();
        System.out.println("finish job " + name);
        return ResultStatus.SUCCESS;

    }

    @Override
    public void fallback(JobFlowScheduler scheduler) {
        System.out.println("fallback job " + name);
    }


}
