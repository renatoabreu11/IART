import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

public class CityGraph implements ViewerListener {
    private Graph graph;
    private int averageDegree;
    protected boolean loop = true;

    public CityGraph(String id, int averageDegree){
        if(!(id.equals("Random") || id.equals("Barabàsi-Albert") || id.equals("Dorogovtsev mendes"))){
            System.out.println("Invalid type of graph");
            return;
        }
        graph = new SingleGraph(id);
        this.averageDegree = averageDegree;
    }

    public void generateGraph(){
        Generator gen = null;
        switch(graph.getId()){
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

        gen.addSink(graph);
        gen.begin();
        for(int i=0; i<100; i++)
            gen.nextEvents();
        gen.end();
    }

    public void display(){
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
        Viewer viewer = graph.display();

        ViewerPipe fromViewer = viewer.newViewerPipe();
        fromViewer.addViewerListener(this);
        fromViewer.addSink(graph);
        while(loop) {
            fromViewer.pump();
        }
    }

    public void setGraphColour(String color){
        graph.addAttribute("ui.stylesheet", "graph { fill-color: " + color + "; }");
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public int getAverageDegree() {
        return averageDegree;
    }

    public void setAverageDegree(int averageDegree) {
        this.averageDegree = averageDegree;
    }

    @Override
    public void viewClosed(String viewName) {
        loop = false;
    }

    @Override
    public void buttonPushed(String id) {
        System.out.println("Button pushed on node "+id);
    }

    @Override
    public void buttonReleased(String id) {
        System.out.println("Button released on node "+id);
    }
}
