package com.example.android.gransantiago;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class RssParserDom3
{
    private URL rssUrl;

    public RssParserDom3(String url)
    {
        try
        {
            Log.d("cpa", "cargando url"+url);

            this.rssUrl = new URL(url);
            Log.d("cpa", "construido parser");
        }
        catch (MalformedURLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Falta> parse()
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        ArrayList<Falta> faltas = new ArrayList<Falta>();
        Log.d("ep", "Entrandoparser");

        try {

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document dom = builder.parse(this.getInputStream());

            Element root = dom.getDocumentElement();

            NodeList sesiones = root.getElementsByTagName("sesion");


//una vez creado listado de sesiones procesamos cada una
            for (int j = 0; j < sesiones.getLength(); j++) {
                Log.d("dsesiones", sesiones.item(j).getNodeName()+sesiones.getLength());

                Log.d("dsesiones",sesiones.item(j).getAttributes().getNamedItem("id").getNodeValue());
                String sesion=sesiones.item(j).getAttributes().getNamedItem("id").getNodeValue();

                NodeList datosFaltas = sesiones.item(j).getChildNodes();
                for(int i=0;i<datosFaltas.getLength();i++) {

                    if (datosFaltas.item(i).getNodeName().equals("profesor_falta")) {
                        Falta f = new Falta();

                        f.setProfesorFalta(datosFaltas.item(i).getNodeValue());
                        String nombre_falta=datosFaltas.item(i).getChildNodes().item(1).getTextContent();
                        String nombre_cubre=datosFaltas.item(i).getChildNodes().item(5).getTextContent();
                        String profesor_cubre=datosFaltas.item(i).getChildNodes().item(3).getTextContent();
                        String asignatura=datosFaltas.item(i).getChildNodes().item(7).getTextContent();
Log.d("nv",datosFaltas.item(i).getChildNodes().item(3).getNodeName()+datosFaltas.item(i).getChildNodes().item(4).getNodeValue());
                        f.setProfesorFalta(nombre_falta);
                        f.setProfesorCubre(nombre_cubre);
                        f.setAsignatura(asignatura);
                        f.setSesion(sesion);
                        Log.d("fleidas", asignatura+profesor_cubre);

                        faltas.add(f);
                        }

                      }

                }
                //de cada sesion extraemos las faltas q contiene
                /*
                for (int k = 0; k < datosFaltas.getLength(); k = k++) {
                    NodeList nlfaltas = datosFaltas.item(k).getChildNodes();
                    Log.d("epc", datosFaltas.item(k).getNodeName()+datosFaltas.getLength());
                    Log.d("nlf", nlfaltas.item(k).getNodeName());

                    //Falta f = new Falta();
                    //de cada falta la procesamos
                    Falta f = new Falta();



                    Log.d("fleidas2", Integer.toString(faltas.size()) + faltas.get(1).getProfesorCubre());
                }*/

        }
        catch (Exception ex)
        {
            Log.d("faltas exception", ex.toString());

            throw new RuntimeException(ex);
        }

        return faltas;
    }

    private String obtenerTexto(Node dato)
    {
        StringBuilder texto = new StringBuilder();
        NodeList fragmentos = dato.getChildNodes();

        for (int k=0;k<fragmentos.getLength();k++)
        {
            texto.append(fragmentos.item(k).getNodeValue());
        }

        return texto.toString();
    }

    private InputStream getInputStream()
    {
        try
        {
            Log.d("Conectando...", "e");
            return rssUrl.openConnection().getInputStream();

        }
        catch (IOException e)
        {
            Log.d("ioexception",e.toString());

            throw new RuntimeException(e);
        }
    }
}
