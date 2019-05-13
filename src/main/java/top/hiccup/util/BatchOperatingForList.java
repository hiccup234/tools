package top.hiccup.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 批量操作List中的元素
 *
 * @author wenhy
 * @date 2018/4/18
 */
public class BatchOperatingForList {

	interface Strategy<E> {
		void execute(List<E> list);
	}

	/**
	 * 通过策略模式来实现对整个list做批量操作，不用关心边界问题
	 * @param list
	 * @param batchSize
	 * @param command
	 * @param <T>
	 */
	public static <T> void batchOperating(List<T> list, int batchSize, Strategy<T> command) {
		if(!(null != list && list.size() > 0)) {
			return ;
		}
		int listSize = list.size();
		int batchCount = listSize/batchSize;
		if(0 != listSize%batchSize) {
		    batchCount++;
		}
		List<T> batchList = null;
		for(int i=0; i<batchCount; i++) {
		    if(i != batchCount-1) {
		        batchList = list.subList(i*batchSize, (i+1)*batchSize);
		    } else {
			    batchList = list.subList(i*batchSize, listSize);
		    }
			if(null != batchList && 0 != batchList.size()) {
				command.execute(batchList);
			}
		}
	}


	public static void main(String[] args) {
		List<Long> list = new ArrayList<>(2000);
		Random rand = new Random(47);
		for(int i=0; i<200; i++) {
//			list.add(Long.valueOf(rand.nextInt(1000)));
			list.add(Long.valueOf(i+1001));
		}
		batchOperating(list, 10, new Strategy<Long>() {
			@Override
			public void execute(List<Long> list) {
				for(Long l : list) {
					System.out.print(l + " ");
				}
				System.out.println();
			}
		});
	}

}
