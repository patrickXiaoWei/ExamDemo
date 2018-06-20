package com.migu.schedule;

import com.migu.schedule.constants.ReturnCodeKeys;
import com.migu.schedule.info.TaskConsuInfo;
import com.migu.schedule.info.TaskInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/*
*类名和方法不能修改
 */
public class Schedule {

	// 服务节点列表
	public static List<TaskInfo> tasks = new ArrayList<TaskInfo>();
	// 挂起的任务
	public static List<TaskConsuInfo> hangUpTasks = new ArrayList<TaskConsuInfo>();
	// 运行中的任务
	public static List<TaskConsuInfo> runningTasks = new ArrayList<TaskConsuInfo>();

	private List<Integer> consum = new ArrayList<Integer>();;

	private Map<Integer, Integer> consumptionMap = new HashMap<Integer, Integer>();

	public int init() {
		// TODO 方法未实现
		tasks.clear();
		hangUpTasks.clear();
		runningTasks.clear();

		return ReturnCodeKeys.E001;
	}

	public int registerNode(int nodeId) {
		// TODO 方法未实现

		if (nodeId <= 0) {
			return ReturnCodeKeys.E004;
		}
		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getNodeId() == nodeId) {

				return ReturnCodeKeys.E005;
			}
		}
		TaskInfo info = new TaskInfo();
		info.setNodeId(nodeId);
		tasks.add(info);

		return ReturnCodeKeys.E003;
	}

	public int unregisterNode(int nodeId) {
		// TODO 方法未实现
		if (nodeId <= 0) {
			return ReturnCodeKeys.E004;
		}

		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getNodeId() == nodeId) {
				tasks.remove(tasks.get(i));
				for (TaskConsuInfo info : runningTasks) {
					if (info.getTaskId() == tasks.get(i).getTaskId())
						hangUpTasks.add(info);
				}
				return ReturnCodeKeys.E006;
			}
		}

		return ReturnCodeKeys.E007;
	}

	public int addTask(int taskId, int consumption) {
		// TODO 方法未实现
		if (taskId <= 0) {
			return ReturnCodeKeys.E009;
		}

		for (int i = 0; i < hangUpTasks.size(); i++) {
			// System.out.println("addTask " + i + " : " +
			// conInfos.get(i).getTaskId());
			if (hangUpTasks.get(i).getTaskId() == taskId) {
				return ReturnCodeKeys.E010;
			}
		}

		TaskConsuInfo conInfo = new TaskConsuInfo();
		conInfo.setTaskId(taskId);
		conInfo.setConsumption(consumption);
		hangUpTasks.add(conInfo);

		return ReturnCodeKeys.E008;
	}

	public int deleteTask(int taskId) {
		// TODO 方法未实现
		if (taskId <= 0) {
			return ReturnCodeKeys.E009;
		}

		for (int i = 0; i < hangUpTasks.size(); i++) {
			if (hangUpTasks.get(i).getTaskId() == taskId) {
				hangUpTasks.remove(hangUpTasks.get(i));
				return ReturnCodeKeys.E011;
			}
		}
		for (int i = 0; i < runningTasks.size(); i++) {
			if (runningTasks.get(i).getTaskId() == taskId) {
				runningTasks.remove(runningTasks.get(i));
				return ReturnCodeKeys.E011;
			}
		}

		return ReturnCodeKeys.E012;
	}

	public int scheduleTask(int threshold) {

		if(hangUpTasks != null && hangUpTasks.size() > 0){
			for (TaskConsuInfo info : hangUpTasks) {
				consum.add(info.getConsumption());
				consumptionMap.put(info.getTaskId(), info.getConsumption());
			}
			//按从大到小排序
			Collections.sort(consum, new Comparator<Integer>() {
				public int compare(Integer o1, Integer o2) {
					if (o1 > o2) {
						return -1;
					} else if (o1 < o2) {
						return 1;
					}
					return 0;
				}
			});
			for(int index = 0 ; index < consum.size(); index ++){
				System.out.println("consum: " + consum.get(index));
			}
		}
		
		
        
		return ReturnCodeKeys.E014;
	}

	public int queryTaskStatus(List<TaskInfo> tasks) {
		if (tasks == null) {
			return ReturnCodeKeys.E016;
		}

		for (TaskInfo task : tasks) {
			tasks.add(task);
		}
		return ReturnCodeKeys.E015;
	}

	// 服务节点数量及对应的任务
	static Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();

	public static void divideDebit(List<Integer> taskList, Integer nodeNum, boolean direction) {
		if (taskList.size() >= nodeNum) {// 任务大于服务节点数量
			for (int i = 0; i < nodeNum; i++) {
				Integer index;
				if (direction) {
					index = i + 1;
				} else {
					index = nodeNum - i;
				}
				List<Integer> list = map.get(index);
				list.add(taskList.get(i));
				map.put(index, list);
			}
			// 去除已经分配的账单
			List<Integer> newDebitList = new ArrayList<Integer>();
			for (int i = 0; i < taskList.size(); i++) {
				if (i > nodeNum - 1) {
					newDebitList.add(taskList.get(i));
				}
			}
			//
			if (newDebitList.size() > 0) {
				// 下次分配账单，按反方向分配
				divideDebit(newDebitList, nodeNum, !direction);
			}
		} else if (taskList.size() < nodeNum) {// 账单小于人数
			for (int i = 0; i < taskList.size(); i++) {
				List<Integer> list = map.get(i + 1);
				list.add(taskList.get(i));
				map.put(i + 1, list);
			}
		}

	}

}
