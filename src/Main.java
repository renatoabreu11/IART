public class Main {
    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        CityGraph cg = new CityGraph("Dorogovtsev mendes", 2);
        cg.generateGraph();
        cg.display();
    }
}
