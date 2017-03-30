package ch03;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 
 * <p>Description: ConcurrentSkipListMap,使用跳表实现的map结构。
 * 跳表本质上是分层的多个链表，最上层元素最少，查找时从最上层开始 ,查找的时间复杂度是O(n)
 * 与map不同，对跳表的遍历会得到一个有顺序的结果</p>
 * @author ZhangShenao
 * @date 2017年3月30日 下午11:35:40
 */
public class ConcurrentSkipListMapTest {
	public static void main(String[] args) {
		Map<Integer,Integer> map = new ConcurrentSkipListMap<>();
		for (int i = 0;i < 30;i++){
			map.put(i, i);
		}
		
		//对跳表的遍历会返回一个有序的结果
		for (Map.Entry<Integer, Integer> entry : map.entrySet()){
			System.out.println(entry.getKey());
		}
	}
}
