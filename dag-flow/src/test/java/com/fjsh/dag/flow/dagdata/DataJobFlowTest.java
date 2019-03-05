package com.fjsh.dag.flow.dagdata;


import com.fjsh.dag.flow.jobflow.JobFlow;
import com.fjsh.dag.flow.jobflow.JobFlowRunner;
import com.fjsh.dag.flow.jobflow.JobFlowScheduler;
import com.fjsh.dag.flow.util.DagGraphUtil;

public class DataJobFlowTest {
    public static void main(String[] args) throws Exception {

        JobFlow flow = new JobFlow("demoGraph", 10, 1);
        JobFlowRunner job1 = new DemoJob(flow, "1");
        JobFlowRunner job2 = new DemoJob(flow, "2");
        JobFlowRunner job31 = new DemoJob(flow, "3.1");
        flow.register(job1);
        flow.register(job2);
        flow.register(job31);


        flow.addDepends(job31,job1);

        JobFlowRunner job32 = new DemoJob(flow, "3.2");
        flow.register(job32);
        flow.addDepends(job32, job2);

        JobFlowRunner job4 = new DemoJob(flow, "4");
        flow.register(job4);
        flow.addDepends(job4, job31,job32);

        JobFlowRunner job5 = new DemoJob(flow, "5");
        flow.register(job5);
        flow.addDepends(job5,job31, job32);

        JobFlowRunner job6 = new DemoJob(flow, "6");
        flow.register(job6);
        flow.addDepends(job6,job5, job4);


//        flow.addDepends(job1,job5);//校验循环依赖
        flow.check();//检查是否有未注册的依赖以及是否有环
        //create dag pic
        DagGraphUtil.createDagPic(flow,"dag-draw/sample");
        JobFlowScheduler scheduler = flow.createScheduler();
        scheduler.doJob(job6);
        CommonData.printData();
    }
}
