package org.mlarocca.containers.priorityqueue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mlarocca.containers.priorityqueue.Heap;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;


public class HeapTest {
    private Heap<String> heap;

    @Before
    public void setUp() throws Exception {
        heap = new Heap<>();
    }

    @Test
    public void top() throws Exception {
        Arrays.asList(2, 3, 4, 5).forEach(branchingFactor -> {
            heap = new Heap<>(branchingFactor);
            Optional<String> result = heap.top();

            assertFalse("top() should return an empty optional when the heap is empty", result.isPresent());
            heap.add("primo", 1);
            result = heap.top();
            assertTrue("top() should return an valid optional when the heap is empty", result.isPresent());
            assertEquals("top() should return the only element in the heap", "primo", result.get());

            heap.add("primo", 1);
            heap.add("secondo", -1);
            heap.add("a", 11);
            heap.add("b", 0);
            heap.add("c", -0.99);
            result = heap.top();
            assertEquals("top() should return the highest priority element in the heap", "secondo", result.get());
        });
    }

    @Test
    public void peek() throws Exception {
        Optional<String> result = heap.peek();

        assertFalse("peek() should return an empty optional when the heap is empty", result.isPresent());
        heap.add("primo", 1e14);
        result = heap.peek();
        assertTrue("peek() should return an valid optional when the heap is empty", result.isPresent());
        assertEquals("peek() should return the only element in the heap", "primo", result.get());

        heap.add("b", 0);
        heap.add("c", -0.99);
        heap.add("secondo", -1);
        heap.add("a", 11);
        result = heap.peek();
        assertEquals("peek() should return the highest priority element in the heap", "secondo", result.get());
    }

    @Test
    public void has() throws Exception {
        Arrays.asList(2, 3, 4, 5).forEach(branchingFactor -> {
            heap = new Heap<>(branchingFactor);
            assertFalse("contains() should return false on a empty heap", heap.contains("any"));
            heap.add("primo", 1e14);
            assertTrue("contains() should return true for an existing element", heap.contains("primo"));
            assertFalse("contains() should return false if the element is not in the heap", heap.contains("any"));

            heap.add("b", 0);
            heap.add("c", -0.99);
            heap.add("secondo", -1);
            heap.add("a", 11);
            assertTrue("contains() should return true for an existing element ('primo')", heap.contains("primo"));
            assertTrue("contains() should return true for an existing element ('secondo')", heap.contains("secondo"));
            assertTrue("contains() should return true for an existing element ('a')", heap.contains("a"));
            assertTrue("contains() should return true for an existing element ('b')", heap.contains("b"));
            assertTrue("contains() should return true for an existing element ('c')", heap.contains("c"));
            assertFalse("contains() should return false if the element is not in the heap", heap.contains("any"));

            heap.remove("b");
            heap.remove("c");

            assertTrue("contains() should return true for an existing element ('primo')", heap.contains("primo"));
            assertTrue("contains() should return true for an existing element ('secondo')", heap.contains("secondo"));
            assertTrue("contains() should return true for an existing element ('a')", heap.contains("a"));
            assertFalse("contains() should return false if the element is not in the heap ('c')", heap.contains("c"));
            assertFalse("contains() should return false if the element is not in the heap ('b')", heap.contains("b"));
            assertFalse("contains() should return false if the element is not in the heap ('any')", heap.contains("any"));

            heap.add("terzo", -44);
            assertTrue("contains() should return true for an existing element ('primo')", heap.contains("primo"));
            assertTrue("contains() should return true for an existing element ('secondo')", heap.contains("secondo"));
            assertTrue("contains() should return true for an existing element ('terzo')", heap.contains("terzo"));
            assertTrue("contains() should return true for an existing element ('a')", heap.contains("a"));
            assertFalse("contains() should return false if the element is not in the heap ('c')", heap.contains("c"));
            assertFalse("contains() should return false if the element is not in the heap ('b')", heap.contains("b"));
            assertFalse("contains() should return false if the element is not in the heap ('any')", heap.contains("any"));

            heap.remove("terzo");
            assertFalse("contains() should return false after the top element contains been removed using remove()", heap.contains("terzo"));

            heap.top();
            assertFalse("contains() should return false after the top element contains been removed using top()", heap.contains("secondo"));
        });
    }

    @Test
    public void add() throws Exception {

    }

    @Test
    public void remove() throws Exception {

    }

    @Test
    public void updatePriority() throws Exception {

    }

    @Test
    public void heapify() throws Exception {
        Arrays.asList(2, 3, 4, 5).forEach(branchingFactor -> {
            List<Integer> elements = Arrays.asList(1, 2, 3, 4, 5);
            List<Double> priorities = Arrays.asList(-1.0, 0.0, Math.E, 1.0, -3.14);
            Heap<Integer> iHeap = new Heap<>(elements, priorities, branchingFactor);

            assertEquals("Size should be 0 on empty Heap", elements.size(), iHeap.size());

            List<Integer> results = new ArrayList<>();
            List<Integer> expected = Arrays.asList(5, 1, 2, 4, 3);

            while (!iHeap.isEmpty()) {
                results.add(iHeap.top().get());
            }

            assertArrayEquals("Heap was not created correctly", results.toArray(), expected.toArray());
        });
    }

    @Test(expected = NullPointerException.class)
    public void heapifyConstructorOnNullElements() throws Exception {
        List<Double> priorities = Arrays.asList(-1.0, 0.0, 1.0, Math.E, -3.14);
        new Heap<>(null, priorities, 2);
    }

    @Test(expected = NullPointerException.class)
    public void heapifyConstructorOnNullPriorities() throws Exception {
        List<Double> elements = Arrays.asList(-1.0, 0.0, 1.0, Math.E, -3.14);
        new Heap<>(elements, null, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void heapifyConstructorOnDifferentSize() throws Exception {
        List<Integer> elements = Arrays.asList(1, 2, 3);
        List<Double> priorities = Arrays.asList(-1.0, 0.0, 1.0, Math.E, -3.14);
        new Heap<>(elements, priorities, 3);
    }

    @Test
    public void size() throws Exception {
        Assert.assertEquals("Size should be 0 on empty Heap", 0, heap.size());

        heap.add("a", 1);
        heap.add("bcd", -1);
        Assert.assertEquals("Size should change on add", 2, heap.size());
        heap.add("a",0);
        assertEquals("Size should NOT change when trying to add existing elements", 2, heap.size());
        heap.add("c", 3.1415);
        heap.add("d", 3.1415);
        assertEquals("Size should change on add more than 2 elements", 4, heap.size());
        heap.remove("d");
        assertEquals("Size should change on remove", 3, heap.size());
        heap.remove("bcd");
        assertEquals("Size should change on remove", 2, heap.size());
        heap.peek();
        assertEquals("Size should NOT change on peek", 2, heap.size());
        heap.top();
        assertEquals("Size should change on remove top", 1, heap.size());
        heap.top();
        assertEquals("Size should change on remove top", 0, heap.size());
    }

    @Test
    public void hasHigherPriority() throws Exception {
        int n = 10;
        Set<Heap.Pair> pairs = IntStream.range(0, n).mapToObj(i -> heap.new Pair<Integer>(i, Math.random())).collect(Collectors.toSet());
        for (Heap.Pair p : pairs) {
            for (Heap.Pair q : pairs) {
                assertEquals("Priority: Smallest means highest", p.getPriority() < q.getPriority(), heap.hasHigherPriority(p, q));
            }
        }
    }

    @Test
    public void getFirstChildIndex() throws Exception {
        assertEquals("First Child, edge case", heap.getFirstChildIndex(0), 1);
        assertEquals("First Child, random", heap.getFirstChildIndex(2), 5);
        assertEquals("Parent/Child transform should be invertible", heap.getParentIndex(heap.getFirstChildIndex(2)), 2);
    }

    @Test
    public void getParentIndex() throws Exception {
        assertEquals("First Child, edge case", heap.getParentIndex(0), 0);
        assertEquals("First Child, first level", heap.getParentIndex(1), 0);
        assertEquals("First Child, first level", heap.getParentIndex(2), 0);
        assertEquals("First Child, second level", heap.getParentIndex(3), 1);
        assertEquals("First Child, second level", heap.getParentIndex(4), 1);
        assertEquals("First Child, random", heap.getParentIndex(6), 2);
        assertEquals("Child/Parent transform should NOT be invertible", heap.getFirstChildIndex(heap.getParentIndex(2)), 1);

    }

}