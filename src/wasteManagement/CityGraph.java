package wasteManagement;

import org.graphstream.algorithm.AStar;
import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkDGS;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class CityGraph implements ViewerListener {
    private Graph graph;
    protected boolean loop = true;
    private String graphFile;

    public CityGraph(String id){
        graph = new SingleGraph(id);
        readGraph(id);
        graphFile = id;
    }

    public static Graph generateGraph(String id, String type, int averageDegree, int numNodes){

        Graph g = new SingleGraph(id);
        if(!(type.equals("Random") || type.equals("Barabàsi-Albert") || type.equals("Dorogovtsev mendes"))){
            System.out.println("Invalid type of graph");
            return null;
        }
        Generator gen = null;
        switch(type){
            case "Random":
                gen = new RandomGenerator(averageDegree);
                break;
            case "Barabàsi-Albert":
                gen = new BarabasiAlbertGenerator(averageDegree);
                break;
            case "Dorogovtsev mendes":
                gen = new DorogovtsevMendesGenerator();
                break;
        }

        gen.addSink(g);
        gen.begin();
        for(int i=0; i<numNodes; i++)
            gen.nextEvents();
        gen.end();

        return g;
    }

    public void readGraph(String filename){
        try {
            graph.read("data/"+filename);
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void saveGraph(Graph g){
        try {
            g.write(new FileSinkDGS(), "data/" + g.getId() + ".dgs");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void display(){

        for (Node n: graph) {
            n.addAttribute("ui.label", n.getId());
        }

        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
        setStyleSheet("stylesheet");
        Viewer viewer = graph.display();

        ViewerPipe fromViewer = viewer.newViewerPipe();
        fromViewer.addViewerListener(this);
        fromViewer.addSink(graph);
        while(loop) {
            fromViewer.pump();
        }
    }

    public void setStyleSheet(String file){
        graph.addAttribute("ui.stylesheet", "url(data/"+file+".css)");
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    @Override
    public void viewClosed(String viewName) {
        loop = false;
    }

    @Override
    public void buttonPushed(String id) {
        System.out.println("Button pushed on node "+id);
        Node node = graph.getNode(id);
        if(node.hasAttribute("waste")){
            Map waste = node.getAttribute("waste");
            Iterator entries = waste.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry thisEntry = (Map.Entry) entries.next();
                Object key = thisEntry.getKey();
                Object value = thisEntry.getValue();
                System.out.println("Type of residue: " + key +" -> " + value + " kg");
            }
        }
    }

    @Override
    public void buttonReleased(String id) {
        System.out.println("Button released on node "+id);
    }

    public String getGraphFile() {
        return graphFile;
    }

    public void setGraphFile(String graphFile) {
        this.graphFile = graphFile;
    }
}
