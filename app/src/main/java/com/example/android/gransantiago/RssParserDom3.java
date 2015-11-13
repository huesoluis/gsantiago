package com.example.android.gransantiago;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
            this.rssUrl = new URL(url);
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

        try {

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document dom = builder.parse(this.getInputStream());

            Element root = dom.getDocumentElement();

            NodeList sesiones = root.getElementsByTagName("sesion");


//una vez creado listado de sesiones procesamos cada una
            for (int j = 0; j < sesiones.getLength(); j++) {
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

        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }

        return faltas;
    }

    private InputStream getInputStream()
    {
        try
        {
            return rssUrl.openConnection().getInputStream();
        }
        catch (IOException e)
        {
            Log.d("ioexception",e.toString());

            throw new RuntimeException(e);
        }
    }
}
