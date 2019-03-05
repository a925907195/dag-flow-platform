package com.fjsh.dag.flow.dagdraw;

import com.google.common.collect.Maps;

import com.fjsh.dag.flow.common.ResultStatus;
import com.fjsh.dag.flow.jobflow.JobFlow;
import com.fjsh.dag.flow.jobflow.JobFlowRunner;
import com.fjsh.dag.flow.jobflow.JobFlowScheduler;
import com.fjsh.dag.flow.util.DagGraphUtil;

import org.enoir.graphvizapi.Attribute;
import org.enoir.graphvizapi.Edge;
import org.enoir.graphvizapi.Graph;
import org.enoir.graphvizapi.GraphType;
import org.enoir.graphvizapi.Graphviz;
import org.enoir.graphvizapi.Node;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 *根据配置信息生成的jobflowRunner 动态构造dag图，直观查询dag依赖信息
 */
public class DagDrawTest {

    private static String tmpPath = "dag-draw/sample";

    public static void main(String[] args) throws Exception{
        DagDrawTest ex = new DagDrawTest();
        ex.draw();
    }

    private void draw() throws Exception{
        JobFlow flow = new JobFlow("demoGraph", 10, 1);
        JobFlowRunner job1 = new DemoJob(flow, "1");
        JobFlowRunner job2 = new DemoJob(flow, "2");
        JobFlowRunner job31 = new DemoJob(flow, "3.1");
        flow.register(job1);
        flow.register(job2);
        flow.register(job31);


        flow.addDepends(job31, job1);

        JobFlowRunner job32 = new DemoJob(flow, "3.2");
        flow.register(job32);
        flow.addDepends(job32, job2);

        JobFlowRunner job4 = new DemoJob(flow, "4");
        flow.register(job4);
        flow.addDepends(job4, job31, job32);

        JobFlowRunner job5 = new DemoJob(flow, "5");
        flow.register(job5);
        flow.addDepends(job5, job31, job32);
//        flow.addDepends(job1,job5);//校验循环依赖
        try {
            //如果有循环依赖则失败
            flow.check();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //create dag pic
        DagGraphUtil.createDagPic(flow,tmpPath);

        //run dag compute
        JobFlowScheduler scheduler = flow.createScheduler();
        scheduler.doJob(job4);
        scheduler.doJob(job5);

    }

    private static class DemoJob extends JobFlowRunner {

        private String name;

        public DemoJob(JobFlow graph, String name) {
            super(graph, name);
            this.name = name;
            this.timeout = 5000;
        }

        @Override
        public ResultStatus run(JobFlowScheduler scheduler) {
            System.out.println("start job " + name);
            try {
                Thread.sleep((int) (Math.random() * 100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            throw new Exception();
            System.out.println("finish job " + name);
            return ResultStatus.SUCCESS;

        }

        @Override
        public void fallback(JobFlowScheduler scheduler) {
            System.out.println("fallback job " + name);
        }

    }
}
