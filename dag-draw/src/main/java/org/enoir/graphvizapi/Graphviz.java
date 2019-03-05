package org.enoir.graphvizapi;

import org.enoir.graphvizapi.exception.GraphException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The Graphviz Class. Can use this class to generate image byte array.
 * Created by frank on 2014/11/17.
 */
public class Graphviz {

    private static String DOT = "dot";
    private static String TMP_PATH = "/tmp";

    /**
     * Constructor. The default graphviz command is "dot". Default tmp file path is "/tmp".
     */
    public Graphviz() {
    }

    /**
     * Constructor with customize dot command like "/usr/bin/dot". And customize tmp file path.
     * Make sure user have writable permission.
     * @param dotPath
     * @param tmpPaht
     */
    public Graphviz(String dotPath,String tmpPaht){
        this.DOT = dotPath;
        this.TMP_PATH = tmpPaht;
    }

    /**
     * The tmp file path setter.
     * @param tmpPath tmp file path like "/home/user/tmp"
     */
    public void setTmpPath(String tmpPath){
        this.TMP_PATH = tmpPath;
    }

    /**
     * Generate Graph's image byte array.
     * @param graph the graph you want to generate.
     * @param type the file type.
     * @param dpi dpi
     * @return
     */
    public byte[] getGraphByteArray(Graph graph, String type, String dpi)
    {
        String dotSource = genDotStringByGraph(graph);
        File dot = null;
        byte[] img_stream = null;

        try {
            dot = writeDotSourceToFile(dotSource);
            if (dot != null){
                img_stream = get_img_stream(dot, type, "dot",dpi);
            }
        } catch (IOException ioe) {
            throw new GraphException("Can not delete tmp file.");
        } finally {
            if(dot!=null) {
                try {
                    dot.delete();
                } catch (Exception ex) {
                }
            }
        }
        return img_stream;
    }

    private String genDotStringByGraph(Graph graph){
        StringBuilder dotString = new StringBuilder();
        if(graph.getGraphType() == GraphType.DIGRAPH){
            dotString.append("digraph ");

        }else if(graph.getGraphType() == GraphType.GRPAH){

            dotString.append("graph ");
        }else{
            assert false;   //TYPE NOT FOUND
        }
        dotString.append(graph.getId());
        dotString.append(graph.genDotString());
        return dotString.toString();
    }

    private File writeDotSourceToFile(String str) throws IOException
    {
        File temp ;
        try {
            temp = File.createTempFile("graph_", ".dot.tmp", new File(Graphviz.TMP_PATH));
            FileWriter fout = new FileWriter(temp);
            fout.write(str);
            fout.close();
        }
        catch (Exception e) {
            return null;
        }
        return temp;
    }

    private byte[] get_img_stream(File dot, String type, String representationType,String dpi)
    {
        File imgFile = null;
        byte[] imgageStream = null;

        try {
            imgFile = File.createTempFile("graphviz_", "."+type, new File(Graphviz.TMP_PATH));
            Runtime rt = Runtime.getRuntime();
            String[] args = {DOT, "-T"+type, "-K"+representationType, "-Gdpi="+dpi, dot.getAbsolutePath(), "-o", imgFile.getAbsolutePath()};
            Process p = rt.exec(args);
            p.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                line = br.readLine();
                System.out.println(line);
            }

            FileInputStream finput = new FileInputStream(imgFile.getAbsolutePath());
            imgageStream = new byte[finput.available()];
            finput.read(imgageStream);
            if( finput != null ){
                finput.close();
            }
            if (imgFile.delete() == false) {
            }
        }
        catch (IOException ioe) {
            throw new GraphException(ioe.toString());
        }
        catch (InterruptedException ie) {
            throw new GraphException(ie.toString());
        }finally {
            try {
                if(dot !=null){
                    dot.delete();
                }
                if(imgFile != null){
                    imgFile.delete();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return imgageStream;
    }

}
