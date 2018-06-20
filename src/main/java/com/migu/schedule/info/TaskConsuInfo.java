package com.migu.schedule.info;

public class TaskConsuInfo {

	 	private int taskId; //任务节点编号
	    private int consumption;//资源消耗率
		public int getTaskId() {
			return taskId;
		}
		public void setTaskId(int taskId) {
			this.taskId = taskId;
		}
		public int getConsumption() {
			return consumption;
		}
		public void setConsumption(int consumption) {
			this.consumption = consumption;
		}
		@Override
		public String toString() {
			return "TaskConsuInfo [taskId=" + taskId + ", consumption=" + consumption + "]";
		}
	
}
