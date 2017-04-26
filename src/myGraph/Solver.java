package myGraph;

import java.util.ArrayList;
import java.util.List;

public class Solver {

    private int numTrucks;
    private MyGraph graph;

    private List<MyPath> solution = null;
    private String typeSolution = null;
    private double sumWasteSol;
    private double distPercorredSol;
    private int numTrucksUsed;

    /**
     * Each solver, needs a new instance of graph.
     * i.e. if a solver1 used graph1, a solver2 must use other new instance of graph (graph2, for instance).
     * @param graph new instance of graph
     * @param numTrucks number of trucks available
     */
    public Solver(MyGraph graph, int numTrucks) {
        this.graph = graph;
        this.numTrucks = numTrucks;
    }

    public List<MyPath> getSolution() {
        return solution;
    }

    public String getTypeSolution() {
        return typeSolution;
    }

    public int getNumTrucks() {
        return numTrucks;
    }

    public double getSumWasteSol() {
        return sumWasteSol;
    }

    public double getDistPercorredSol() {
        return distPercorredSol;
    }

    public int getNumTrucksUsed() {
        return numTrucksUsed;
    }

    /**
     * Solve the instance of problem (graph + trucks available)
     * @param algorithm A*, dfs or bfs
     * @return paths found
     * @throws Exception invalid algorithm
     */
    public List<MyPath> solve(String algorithm) throws Exception {

        typeSolution = algorithm;

        if (algorithm.equals("A*"))
            solution = this.solveByAStar();
        else if (algorithm.equals("dfs"))
            solution = this.solveByDfs();
        else if (algorithm.equals("bfs"))
            solution = this.solveByBfs();
        else
            throw new Exception("Error: invalid algorithm. Algorithm must be \"A*\", \"dfs\" or \"bfs\"");

        this.setInfoOfSolution();
        return solution;
    }

    /**
     * Print info about the solution computed.
     * @throws Exception throwed if solution was not computed.
     */
    public void printInfoAboutSolution() throws Exception {
        if (solution == null)
            throw new Exception("Error: solution was not yet computed. Call solve() first!");

        System.out.println("Solution using algorithm " + typeSolution);
        System.out.println("Sum of total waste: " + sumWasteSol);
        System.out.println("Total distance percorred: " + distPercorredSol);
        System.out.println("Number of trucks used: " + numTrucksUsed);

    }

    private void setInfoOfSolution() {
        sumWasteSol = 0;
        distPercorredSol = 0;
        numTrucksUsed = solution.size();
        for (int i = 0; i < solution.size(); i++) {
            MyPath path = solution.get(i);
            sumWasteSol += path.getSumWasteCollected();
            distPercorredSol += path.getDistPercorred();
        }
    }

    private List<MyPath> solveByAStar() {
        List<MyPath> paths = new ArrayList<MyPath>();
        for (int i = 0; i < numTrucks; i++) {
            MyPath path = graph.findPath_AStar();
            if (graph.getTotalWasteOfAType(graph.getTypeWaste()) <= 0) {
                break;
            }
            else {
                paths.add(path);
                path.updateWasteInNodes();
            }
        }
        return paths;
    }

    private List<MyPath> solveByDfs() {
        return this.greedyAuxSolver("dfs");
    }

    private List<MyPath> solveByBfs() {
        return this.greedyAuxSolver("bfs");
    }

    /**
     * Get the nodes with waste (greedy).
     * For each node computes the path:
     * from -> ... -> nodeWithWaste -> ... -> to, using bfs or dfs
     * from = station, to central.
     *
     * @param algorithm bfs or dfs
     * @return paths
     */
    private List<MyPath> greedyAuxSolver(String algorithm) {

        List<MyPath> paths = new ArrayList<MyPath>();
        List<MyNode> nodesWithWaste = graph.getNodesWithWaste();
        MyNode from = graph.getFrom();
        MyNode to = graph.getTo();

        for (int i = 0; i < numTrucks && i < nodesWithWaste.size(); i++) {

            MyNode nodeWithWaste = nodesWithWaste.get(i);
            MyPath path1 = null, path2 = null;

            if (algorithm.equals("bfs")) {
                path1 = graph.findPath_bfs(from, nodeWithWaste);
                path2 = graph.findPath_bfs(nodeWithWaste, to);
            }
            else if (algorithm.equals("dfs")) {
                path1 = graph.findPath_dfs(from, nodeWithWaste);
                path2 = graph.findPath_dfs(nodeWithWaste, to);
            }

            List<MyNode> path1Nodes = path1.getNodesOfPath();
            List<MyNode> path2Nodes = path2.getNodesOfPath();
            path1Nodes.remove(path1Nodes.size()-1);
            path1Nodes.addAll(path2Nodes);

            MyPath finalPath = new MyPath(path1Nodes, graph.getMaxCapacityTruck(), graph.getTypeWaste());
            finalPath.updateWasteInNodes();
            paths.add(finalPath);
        }

        return paths;
    }

}
