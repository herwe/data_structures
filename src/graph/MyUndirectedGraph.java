package graph;

import java.util.*;

public class MyUndirectedGraph<T> implements UndirectedGraph<T> {

    private int vertices;
    private int edges;
    private List<Node<T>> nodeList = new ArrayList<>();
    private Map<T, Integer> nodeListIndex = new HashMap<>();

    private static class Node<T> {
        private T data;
        private Map<T, Integer> neighbours;
        private boolean visited;
        private int distSoFar;
        private Node<T> cameFrom;

        Node(T data) {
            this.data = data;
            this.neighbours = new HashMap<>();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?> node = (Node<?>) o;
            return Objects.equals(data, node.data);
        }

        @Override
        public int hashCode() {
            return Objects.hash(data);
        }
    }


    @Override
    public int getNumberOfNodes() {
        return vertices;
    }

    @Override
    public int getNumberOfEdges() {
        return edges;
    }

    @Override
    public boolean add(T newNode) {
        if (newNode == null) {
            throw new NullPointerException();
        }

        if (existNode(newNode)) {
            return false;
        }

        nodeListIndex.put(newNode, vertices);
        vertices++;
        return nodeList.add(new Node<T>(newNode));
    }

    @Override
    public boolean connect(T firstNode, T secondNode, int cost) {
        if (existNode(firstNode) && existNode(secondNode) && cost > 0) {
            Node<T> node1 = new Node<>(firstNode);
            if (node1.data.equals(firstNode)) {
                nodeList.get(nodeListIndex.get(firstNode)).neighbours.put(secondNode, cost);
            }

            if (nodeList.get(nodeListIndex.get(secondNode)).data.equals(secondNode)) {
                nodeList.get(nodeListIndex.get(secondNode)).neighbours.put(firstNode, cost);
                edges++;
            }
            return true;
        }
        return false;
    }

    private boolean existNode(T node) {
        return nodeListIndex.containsKey(node);
    }

    private Node<T> getNode(T node) {
        return nodeList.get(nodeListIndex.get(node));
    }

    @Override
    public boolean isConnected(T firstNode, T secondNode) {
        Node<T> first = getNode(firstNode);
        return first != null && first.neighbours.containsKey(secondNode);
    }

    @Override
    public int getCost(T firstNode, T secondNode) {
        if (existNode(firstNode) && existNode(secondNode)) {
            if (nodeList.get(nodeListIndex.get(firstNode)).neighbours.get(secondNode) != null) {
                return nodeList.get(nodeListIndex.get(firstNode)).neighbours.get(secondNode);
            }
        }

        return -1;
    }

    @Override
    public List<T> depthFirstSearch(T startData, T endData) {

        List<T> list = new LinkedList<>();
        depthFirstSearch(startData, endData, list);
        return list;
    }

    //Returns true if end node found
    private boolean depthFirstSearch(T startData, T endData, List<T> list) {
        list.add(startData);
        Node v = getNode(startData);
        v.visited = true;

        if (v.data == endData) {
            return true;
        }

        for (Object o : v.neighbours.keySet()) {
            Node<T> w = getNode((T) o);
            if (!w.visited) {
                if (depthFirstSearch((T) o, endData, list)) {
                    return true;
                }
            }
        }
        list.remove(startData);
        v.visited = false;
        return false;
    }

    private void resetNodes() {
        for (Node<T> node : nodeList) {
            node.visited = false;
            node.distSoFar = Integer.MAX_VALUE;
            node.cameFrom = null;
        }
    }

    @Override
    public List<T> breadthFirstSearch(T start, T end) {
        resetNodes();
        Deque<Node<T>> toExamine = new LinkedList<>();

        Node<T> startNode = getNode(start);
        startNode.distSoFar = 0;
        toExamine.addLast(startNode);

        while (!toExamine.isEmpty()) {
            Node<T> currNode = toExamine.removeFirst();

            for (Map.Entry<T, Integer> entry : currNode.neighbours.entrySet()) {
                if (currNode.distSoFar + 1 < getNode(entry.getKey()).distSoFar) {
                    getNode(entry.getKey()).distSoFar = currNode.distSoFar + 1;
                    getNode(entry.getKey()).cameFrom = currNode;
                    toExamine.addLast(getNode(entry.getKey()));
                }
            }
        }
        LinkedList<T> result = new LinkedList<>();
        if (getNode(end).distSoFar < Integer.MAX_VALUE) {
            Node<T> currNode = getNode(end);
            result.addFirst(currNode.data);
            while (currNode != getNode(start)) {
                currNode = currNode.cameFrom;
                result.addFirst(currNode.data);
            }
        }
        return result;
    }

    private static class Edge<T> {
        private T firstNode;
        private T secondNode;
        private int cost;

        Edge(T firstNode, T secondNode, int cost) {
            this.firstNode = firstNode;
            this.secondNode = secondNode;
            this.cost = cost;
        }
    }

    @Override
    public UndirectedGraph<T> minimumSpanningTree() {
        resetNodes();
        MyUndirectedGraph<T> mst = new MyUndirectedGraph<T>();

        mst.add(nodeList.get(0).data);
        nodeList.get(0).visited = true;

        Edge<T> cheapestEdge = new Edge(null, null, Integer.MAX_VALUE);
        boolean more = true;
        while (more) {
            more = false;
            cheapestEdge = new Edge<T>(null, null, Integer.MAX_VALUE);
            for (Node<T> node : mst.nodeList) {
                for (Map.Entry<T, Integer> entry : getNode(node.data).neighbours.entrySet()) {
                    if (!getNode(entry.getKey()).visited && entry.getValue() < cheapestEdge.cost) {
                        cheapestEdge = new Edge<T>(node.data, entry.getKey(), entry.getValue());
                        more = true;
                    }
                }

            }
            if (more) {
                mst.add(cheapestEdge.secondNode);
                getNode(cheapestEdge.secondNode).visited = true;
                mst.connect(cheapestEdge.firstNode, cheapestEdge.secondNode, cheapestEdge.cost);
            }
        }

        return mst;
    }
}

