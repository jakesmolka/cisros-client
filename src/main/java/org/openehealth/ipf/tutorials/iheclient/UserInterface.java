package org.openehealth.ipf.tutorials.iheclient;

import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import org.apache.camel.CamelContext;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.openehealth.ipf.commons.ihe.xds.core.requests.DocumentReference;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetDocumentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.activation.DataHandler;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.*;

public class UserInterface extends JFrame {
    private JPanel panel1;
    private JButton button1;
    private JButton button2;
    private JTextArea textArea1;

    @Autowired
    private IHEWebServiceClient client;

    @Autowired
    private ApplicationContext applicationContext;

    public UserInterface() {
        add(panel1);

        setTitle("Test");
        setSize(1000, 800);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String bodyString = "";
                try {
                    bodyString = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("composition.json"));
                } catch (IOException exep) {
                    exep.printStackTrace();
                }

                HttpResponse response = HttpRequest
                        .post("http://localhost:8088/ehr/12345/composition")
                        .body(bodyString)
                        .send();

                textArea1.setText(response.statusCode() + " - " + response.body());
            }
        });

        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Load Spring Context
                ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                        "/context.xml");
                IHEWebServiceClient iheClient = (IHEWebServiceClient) context.getBean("client");
                CamelContext camel = iheClient.getCamelContext();

                // prepare data
                String input = "";
                try {
                    input = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("composition.json"));
                } catch (IOException exce) {
                    exce.printStackTrace();
                }
                JSONObject json = new JSONObject(input);
                String entryUuid = "urn:uuid:" + json.getJSONObject("uid").getString("value");

                GetDocumentsQuery query = new GetDocumentsQuery();
                //query.setUniqueIds(Collections.singletonList(entryUuid));
                query.setUuids(Collections.singletonList(entryUuid));

                // invoke query
                QueryResponse queryResponse = new QueryResponse();
                try {
                    queryResponse = iheClient.iti18StoredQuery(query, "localhost", 9091, "xds-iti18");
                } catch (Exception exce) {
                    exce.printStackTrace();
                }

                String id = queryResponse.getDocumentEntries().get(0).getUniqueId();

                // prepare data
                RetrieveDocumentSet retrieve = new RetrieveDocumentSet();
                DocumentReference doc = new DocumentReference();
                doc.setDocumentUniqueId(id);
                doc.setRepositoryUniqueId("something");
                retrieve.getDocuments().add(doc);

                // invoke retrieve
                RetrievedDocumentSet docSet = new RetrievedDocumentSet();
                try {
                    docSet = iheClient.iti43RetrieveDocumentSet(retrieve, "localhost", 9091, "xds-iti43");
                } catch (Exception exce) {
                    exce.printStackTrace();
                }

                String content = getContent(docSet.getDocuments().get(0).getDataHandler());
                textArea1.setText(docSet.getStatus().toString() + "\n" + content);
            }
        });
    }

    private static String getContent(DataHandler dataHandler) {
        InputStream inputStream = null;
        try {
            inputStream = dataHandler.getInputStream();
            StringWriter writer = new StringWriter();
            IOUtils.copy(inputStream, writer, "UTF-8");
            inputStream.close();
            return writer.toString();
        } catch (IOException exce) {
            exce.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {

        UserInterface userInterface = new UserInterface();
        userInterface.setVisible(true);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(0, 0));
        panel1.add(panel2, BorderLayout.SOUTH);
        button2 = new JButton();
        button2.setText("ITI-18 and ITI-43");
        panel2.add(button2, BorderLayout.EAST);
        button1 = new JButton();
        button1.setText("Create Composition");
        panel2.add(button1, BorderLayout.WEST);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new BorderLayout(0, 0));
        panel1.add(panel3, BorderLayout.CENTER);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel3.add(scrollPane1, BorderLayout.CENTER);
        textArea1 = new JTextArea();
        textArea1.setText("Status text");
        scrollPane1.setViewportView(textArea1);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
