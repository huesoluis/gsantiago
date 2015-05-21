package com.example.android.gsantiago;

import android.util.Log;

import com.example.android.gsantiago.Falta;

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


import android.util.Log;

import com.example.android.gsantiago.Falta;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RssParserDom2
{
    private URL rssUrl;

    public RssParserDom2(String url)
    {
        try
        {
            Log.d("Construyendo en el pa", "e");

            this.rssUrl = new URL(url);
            Log.d("Construido parser", "e");
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
        Log.d("Entrandoparser", "ep");

        try {
            Log.d("ss", "ep");

            DocumentBuilder builder = factory.newDocumentBuilder();
            Log.d("consdocuento0", "ep");

            Document dom = builder.parse(this.getInputStream());
            Log.d("consdocuento05", "ep");

            Element root = dom.getDocumentElement();
            Log.d("consdocuento1", "ep");

            NodeList sesiones = root.getElementsByTagName("sesion");
            Log.d("consdocuento2", "ep");

            Log.d("Eparser", Integer.toString(faltas.size()) + sesiones.getLength());
//una vez creado listado de sesiones procesamos cada una
            for (int j = 0; j < sesiones.getLength(); j++) {
                Log.d("dsesiones", sesiones.item(j).getNodeName());


//            for (int i=0; i<sesiones.getLength(); i++) {
                // NodeList nlfaltas = root.getElementsByTagName("falta");
                //Node nfalta = nlfaltas.item(i);
                NodeList datosFaltas = sesiones.item(j).getChildNodes();
                Log.d("dsesion1", String.valueOf(j));

                //de cada sesion extraemos las faltas q contiene
                for (int k = 1; k < datosFaltas.getLength(); k = k + 2) {
                    NodeList nlfaltas = datosFaltas.item(k).getChildNodes();
                    //Falta f = new Falta();
//de cada falta la procesamos
                    Falta f = new Falta();
                    for (int p = 0; p < nlfaltas.getLength(); p = p + 1) {

                        Log.d("Faltas1", nlfaltas.item(p).getNodeName());
                        String etiqueta = nlfaltas.item(p).getNodeName();

                        if (etiqueta.equals("profesor_falta")) {
                            //f.setProfesorFalta(nlfaltas.item(p).getFirstChild().getNodeValue());
                            NodeList nlprof = nlfaltas.item(p).getChildNodes();
                            Log.d("pfalta0", Integer.toString(nlprof.getLength()));
                            NamedNodeMap nn=nlfaltas.item(p).getAttributes();
                            Log.d("atributos", nn.item(0).getNodeName()+nn.item(0).getNodeValue());
                                f.setProfesorFalta(nn.item(0).getNodeValue());
                            //Log.d("pfalta1", nlprof.item(0).getFirstChild().getNodeValue());
                            for (int pf = 0; pf < nlprof.getLength(); pf = pf + 1) {
                                Log.d("pfalta2", nlprof.item(pf).getNodeName());

                                String etiquetaprof = nlprof.item(pf).getNodeName();
                                if (etiquetaprof.equals("profesor_cubre")) {
                                    Log.d("pfalta3", nlprof.item(pf).getFirstChild().getNodeValue());

                                    f.setProfesorCubre(nlprof.item(pf).getFirstChild().getNodeValue());
                                } else if (etiquetaprof.equals("asignatura")) {
                                    Log.d("pfalta3", nlprof.item(pf).getFirstChild().getNodeValue());
                                    f.setAsignatura(nlprof.item(pf).getFirstChild().getNodeValue());
                                }

                            }

                        }
                        Log.d("adding1", f.getProfesorCubre());

                        faltas.add(f);

                    }

                    Log.d("fleidas2", Integer.toString(faltas.size()) + faltas.get(1).getProfesorCubre());
                }
            }
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
