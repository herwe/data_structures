package graph;

import java.util.*;

public class MyUndirectedGraph<T> implements UndirectedGraph<T> {

    private int vertices;
    private int edges;
    private List<Node<T>> nodeList = new LinkedList<>();

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

        vertices++;
        return nodeList.add(new Node<T>(newNode));
    }

    @Override
    public boolean connect(T firstNode, T secondNode, int cost) {
        if (cost <= 0) {
            return false;
        }

        boolean nodesExist = existNode(firstNode) && existNode(secondNode);

        boolean isFound = false;
        for (int i = 0; i < nodeList.size(); i++) {
            if (nodeList.get(i).data.equals(firstNode)) {
                if (nodesExist) {
                    nodeList.get(i).neighbours.put(secondNode, cost);
                }
            }

            if (nodeList.get(i).data.equals(secondNode)) {
                if (nodesExist) {
                    nodeList.get(i).neighbours.put(firstNode, cost);
                    isFound = true;
                    edges++;
                }
            }
        }

        return isFound;
    }

    private boolean existNode(T node) {
        boolean nodeExists = false;
        for (int j = 0; j < nodeList.size(); j++) {
            if (nodeList.get(j).data.equals(node)) {
                nodeExists = true;
            }
        }
        return nodeExists;
    }

    private Node<T> getNode(T node) {
        for (int j = 0; j < nodeList.size(); j++) {
            if (nodeList.get(j).data.equals(node)) {
                return nodeList.get(j);
            }
        }
        return null;
    }

    @Override
    public boolean isConnected(T firstNode, T secondNode) {
        Node<T> first = getNode(firstNode);
        return first != null && first.neighbours.containsKey(secondNode);
    }

    @Override
    public int getCost(T firstNode, T secondNode) {

        if(!existNode(secondNode)){
            return -1;
        }
        for (int i = 0; i < nodeList.size(); i++) {
            if (nodeList.get(i).data.equals(firstNode)) {
                if (nodeList.get(i).neighbours.get(secondNode) == null) {
                    return -1;
                }
                return nodeList.get(i).neighbours.get(secondNode);
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

