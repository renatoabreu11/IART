import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.Graph;
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

    public CityGraph(String id){
        graph = new SingleGraph(id);
        readGraph(id);
    }

    public void generateGraph(String type, int averageDegree){
        if(!(type.equals("Random") || type.equals("Barabàsi-Albert") || type.equals("Dorogovtsev mendes"))){
            System.out.println("Invalid type of graph");
            return;
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

        gen.addSink(graph);
        gen.begin();
        for(int i=0; i<100; i++)
            gen.nextEvents();
        gen.end();
    }

    public void readGraph(String filename){
        try {
            graph.read("data/"+filename+".dgs");
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void saveGraph(){
        try {
            graph.write(new FileSinkDGS(), "data/" + graph.getId() + ".dgs");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void display(){
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
        Map waste = graph.getNode(id).getAttribute("waste");
        Iterator entries = waste.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry thisEntry = (Map.Entry) entries.next();
            Object key = thisEntry.getKey();
            Object value = thisEntry.getValue();
            System.out.println(key);
            System.out.println(value);
        }
    }

    @Override
    public void buttonReleased(String id) {
        System.out.println("Button released on node "+id);
    }
}
