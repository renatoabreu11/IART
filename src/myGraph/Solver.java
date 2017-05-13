package myGraph;

import org.graphstream.graph.Graph;
import wasteManagement.Waste;
import wasteManagement.WasteManagement;

import java.util.ArrayList;
import java.util.List;

public class Solver {

    private int numTrucks;
    private MyGraph graph;
    private WasteManagement management;

    private List<MyPath> solution = null;
    private String typeSolution = null;
    private double sumWasteSol;
    private double distPercorredSol;
    private int numTrucksUsed;
    private double containerCap;

    /**
     * Contructor of Solver.
     * alfa + beta = 1 must be guaranteed.
     *
     * @param gs graph of GraphStreamLibray
     * @param management store the waste in nodes
     * @param typeWaste waste to collect
     * @param alfa value of importance given to distance percorred, between 0 and 1.
     * @param beta value of importance given to waste collected, between 0 and 1.
     * @param numTrucks maximum number of trucks that solver will use to find a solution-
     */
    public Solver(Graph gs, WasteManagement management, Waste typeWaste, double alfa, double beta, int numTrucks) throws Exception {
        if (Math.abs(1 - alfa - beta) > 0.01)
            throw new Exception("Error: alfa + beta = 1 must be guaranteed.");
        this.graph = new MyGraph(gs, management, typeWaste, alfa, beta);
        this.numTrucks = numTrucks;
        this.management = management;
        this.containerCap = management.getContainerCap();
    }

    public List<MyPath> getSolution() {
        return solution;
    }

    public MyPath getPath(int index) {
        if (solution == null || index >= solution.size() || index < 0)
            return null;
        return solution.get(index);
    }

    public int getNumTrucksUsed() {
        return numTrucksUsed;
    }

    public MyGraph getGraph() {return graph;}

    /**
     * Solve the instance of problem.
     * @param algorithm A*, dfs or bfs
     * @throws Exception invalid algorithm
     */
    public void solve(String algorithm) throws Exception {

        typeSolution = algorithm;

        switch (algorithm) {
            case "A*":
                solution = this.solveByAStar();
                break;
            case "dfs":
                solution = this.solveByDfs();
                break;
            case "bfs":
                solution = this.solveByBfs();
                break;
            default:
                throw new Exception("Error: invalid algorithm. Algorithm must be \"A*\", \"dfs\" or \"bfs\"");
        }

        this.setInfoOfSolution();
        this.graph.readWasteFromManagement(management); // reset waste
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
        for (MyPath path : solution) {
            sumWasteSol += path.getSumWasteCollected();
            distPercorredSol += path.getDistPercorred();
        }
    }

    private List<MyPath> solveByAStar() {
        List<MyPath> paths = new ArrayList<>();
        for (int i = 0; i < numTrucks; i++) {

            MyPath path = graph.findPath_AStar();
            if (!graph.isNecessaryCollectWaste(containerCap)) {
                break;
            }
            else {
                paths.add(path);
                assert path != null;
                path.updateWasteInNodes();
                graph.setWasteProximityFactor();
            }
        }
        return paths;
    }

    public boolean foundPaths() {
        return solution.size() > 0;
    }

    private List<MyPath> solveByDfs() {
        List<MyPath> paths = new ArrayList<>();

        for (int i = 0; i < numTrucks; i++) {

            MyPath path = graph.findPath_dfs();
            if (!graph.isNecessaryCollectWaste(containerCap)) {
                break;
            }
            else {
                paths.add(path);
                assert path != null;
                path.updateWasteInNodes();
            }
        }
        return paths;
    }

    private List<MyPath> solveByBfs() {
        List<MyPath> paths = new ArrayList<>();

        for (int i = 0; i < numTrucks; i++) {

            MyPath path = graph.findPath_bfs();
            if (!graph.isNecessaryCollectWaste(containerCap)) {
                break;
            }
            else {
                paths.add(path);
                assert path != null;
                path.updateWasteInNodes();
            }
        }
        return paths;
    }

    public String getInfo() {
        StringBuilder generalInfo = new StringBuilder("Sum of total waste: "
                + sumWasteSol +
                "\nTotal distance traveled: "
                + distPercorredSol
                + "\nNumber of trucks used: "
                + numTrucksUsed + "\n\n");

        ArrayList<String> residueBuildup = management.getResidueInfo();
        for (String s : residueBuildup) {
            generalInfo.append("\n").append(s);
        }
        return generalInfo.toString();
    }
}
