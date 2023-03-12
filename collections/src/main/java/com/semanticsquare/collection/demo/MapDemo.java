package com.semanticsquare.collection.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

public class MapDemo {
	public static void main(String[] args) {
		// immutableKeysDemo();

		// lruCacheTest();
		System.out.println(StringUtils.lowerCase("SASASASSSA"));
		treeMapDemo();

	}

	private static void lruCacheTest() {

		System.out.println("\n\nInside lruCacheTest ...");
		Map<String, String> lruCache = new LRUCache<>(16, 0.75f, true);
		lruCache.put("a", "A");
		lruCache.put("b", "B");
		lruCache.put("c", "C");
		System.out.println(lruCache);

		lruCache.get("a");
		lruCache.get("a");
		lruCache.get("a");
		System.out.println(lruCache);

		lruCache.put("d", "D");
		System.out.println(lruCache);

		lruCache.put("e", "E");
		System.out.println(lruCache);

		lruCache.get("b");
		System.out.println(lruCache);

	}

	private static void treeMapDemo() {
		System.out.println("\n\nInside treeMapDemo ...");
		SortedMap<String, Integer> map1 = new TreeMap<>();
		map1.put("John", 25);
		map1.put("Raj", 29);
		map1.put("Anita", 23);

		System.out.println("Map1: " + map1);

		System.out.println("Iterating using entrySet ...");
		Set<Map.Entry<String, Integer>> mappings = map1.entrySet();
		for (Map.Entry<String, Integer> entry : mappings) {
			System.out.println("Name: " + entry.getKey() + ", Age: " + entry.getValue());
			if (entry.getKey().equals("John"))
				entry.setValue(99);
		}
		System.out.println("Map1: " + map1);

		// ((TreeMap<String, Integer>) map1).floorEntry("John").setValue(100);
	}

	private static void immutableKeysDemo() {

		System.out.println("\n\nInside immutableKeysDemo ...");
		List<Integer> list = new ArrayList<>();
		list.add(1);

		Map<List<Integer>, Integer> map = new HashMap<>();
		map.put(list, 1);
		System.out.println("get 'list' key: " + map.get(list));
		list.add(2);
		System.out.println("get 'list' key: " + map.get(list));

		Map<Student, String> map1 = new HashMap<>();
		Student s1 = new Student(1, "Phuong");
		map1.put(s1, "0923414705");
		System.out.println("s1 values: " + map1.get(s1));

		s1.setId(2);
		System.out.println("s1 values: " + map1.get(s1));

	}

	private static void hashMapDemo() {
		System.out.println("\n\nInside hashMapDemo ...");
		Map<String, Integer> map1 = new HashMap<>();
		map1.put("John", 25);
		map1.put("Raj", 29);
		map1.put("Anita", null);

		System.out.println("map1: " + map1);

		map1.put("Anita", 23);
		System.out.println("map1: " + map1);

		System.out.println("Contains John? " + map1.containsKey("John"));
		System.out.println("John's age: " + map1.get("John"));

		System.out.println("Iterating using keySet...");
		Set<String> names = map1.keySet();
		names.forEach((String name) -> System.out.println("Name: " + name + ", Age: " + map1.get(name)));

		System.out.println("Iterating using entrySet ...");
		Set<Map.Entry<String, Integer>> mappings = map1.entrySet();

		mappings.forEach((Map.Entry<String, Integer> mapping) -> System.out
				.println("name: " + mapping.getKey() + ", age: " + mapping.getValue()));

		map1.remove("Anita");
		System.out.println("map1: " + map1);

		Map<String, Map<String, Object>> profiles = new HashMap<>();
		Map<String, Object> profile = new HashMap<>();
		profile.put("age", 25);
		profile.put("dept", "CS");
		profile.put("city", "New York");

		profiles.put("John", profile);

		profile = new HashMap<>();
		profile.put("age", 29);
		profile.put("dept", "CS");
		profile.put("city", "New York");

		profiles.put("Raj", profile);

		System.out.println("userProfile: " + profiles);

		Map<String, Object> profile1 = profiles.get("John");
		int age = (Integer) profile1.get("age");
		System.out.println("Age: " + age);

		System.out.println("profiles: " + profiles);
		profiles.clear();
		System.out.println("profiles: " + profiles);
	}

}

class Student {
	private int id;
	private String name;

	public Student(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

class LRUCache<K, V> extends LinkedHashMap<K, V> {

	private static final int MAX_ENTRIES = 3;

	public LRUCache(int initialCapacity, float loadFactor, boolean accessOrder) {
		super(initialCapacity, loadFactor, accessOrder);
	}

	// Invoked by put and putAll after inserting a new entry into the mmap

	@Override
	protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
		return size() > MAX_ENTRIES;
	}
}
