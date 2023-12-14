package nl.bos;

import com.cordys.cap.utility.DeploymentUtility;
import com.cordys.cap.utility.internal.DeploymentUtilityImpl;
import com.cordys.cpc.bsf.busobject.*;
import com.cordys.cpc.bsf.classinfo.ClassInfo;
import com.cordys.cpc.bsf.soap.SOAPRequestObject;
import com.cordys.cpc.bsf.util.DataConverter;
import com.cordys.xml.dom.DOMFactory;
import com.eibus.applicationconnector.sql.DBConnectionPool;
import com.eibus.management.IManagedComponent;
import com.eibus.management.ManagedComponentFactory;
import com.eibus.util.LinkedList;
import com.eibus.util.Queue;
import com.eibus.util.Random;
import com.eibus.util.*;
import com.eibus.util.system.EIBProperties;
import com.eibus.util.system.Native;
import com.eibus.util.system.OrganizationContext;
import com.eibus.util.system.SystemInformation;
import com.eibus.xml.nom.Document;
import com.eibus.xml.nom.Node;
import com.eibus.xml.nom.XMLException;
import com.eibus.xml.xpath.XPath;
import com.eibus.xml.xpath.XPathMetaInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {

    private static final String XML_INPUT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<note>" +
            "<to>Tove</to><from>Jani</from><heading>Reminder</heading>" +
            "<body>Don't forget me this weekend!</body>" +
            "</note>";
    private Document document;

    @BeforeEach
    void setUp() {
        document = new Document();
        System.setProperty("CORDYS_INSTALL_DIR", "C:/CORDYS_22_4"); //point to the correct dir on your machine!
    }

    @Test
    void readXMLData() throws XMLException {
        int xmlResponse = document.load(XML_INPUT.getBytes());
        assertThat(xmlResponse).isNotZero();
    }

    @Test
    void writeXMLData() throws XMLException {
        int xmlResponse = document.load(XML_INPUT.getBytes());
        String xmlString = Node.writeToString(xmlResponse, true);
        System.out.println(xmlString);
        assertThat(xmlString).isNotEmpty();
    }

    @Test
    void getMatchingNodes() throws XMLException {
        int xmlResponse = document.load(XML_INPUT.getBytes());
        int[] xPathResult = XPath.getMatchingNodes("//note/body/text()", null, xmlResponse);
        for (int note : xPathResult) {
            String actual = Node.getData(note);
            assertThat(actual).isEqualTo("Don't forget me this weekend!");
        }
    }

    @Test
    void buildingXML() throws XMLException {
        String expected = "folderContent";

        int rootElement = document.load("<root xmlns:h=\"appworks-tips.com\"></root>".getBytes());
        int folder1Element = Node.createElement("folder_1", rootElement);
        int folder2Element = Node.createElement("folder_2", rootElement);
        int folder3Element = Node.createElementNS("folder_3", expected, null, "appworks-tips.com", rootElement);
        Node.createElement("document_1", folder1Element);
        Node.createElement("document_2", folder1Element);
        Node.createElement("document_3", folder2Element);
        Node.createElement("document_4", folder2Element);
        Node.createElement("document_5", folder3Element);

        System.out.println(Node.writeToString(rootElement, true));

        int firstMatch1 = XPath.getFirstMatch("//root/folder_1", null, rootElement);
        System.out.println(Node.writeToString(firstMatch1, true));

        XPathMetaInfo metaInfo = new XPathMetaInfo();
        metaInfo.addNamespaceBinding("", "");
        int firstMatch2 = XPath.getFirstMatch("//root/folder_3", metaInfo, rootElement);
        assertThat(Node.getData(firstMatch2)).isEqualTo(expected);
    }

    @Test
    void readProperties() {
        Properties properties = EIBProperties.getProperties();
        properties.forEach((key, value) -> {
            System.out.printf("key: %s; value: %s%n", key, value);
        });

        System.setProperty("CORDYS_INSTALL_DIR", "");
        String expected = String.format("%s\\%s\\%s",
                Paths.get("").toAbsolutePath(),
                EIBProperties.CONFIG_DIRECTORY_NAME,
                EIBProperties.PROPERTIES_FILE);
        assertThat(EIBProperties.getDefaultPropertiesFileName()).isEqualTo(expected);

        SystemInformation systemInformation = new SystemInformation();
        System.out.printf("OS: %s, FQDN: %s%n",
                systemInformation.getOSVersion(),
                SystemInformation.getHostName());

        System.out.printf("CP: %s, convert: %s%n",
                Native.getBaseCordysClassPath(),
                Native.convertUTCStringToTimestamp("2022-12-31T00:00:00.0000000000"));
    }

    @Test
    void nomToDom() throws XMLException, TransformerException {
        int xmlResponse = document.load(XML_INPUT.getBytes());
        org.w3c.dom.Document domDocument = DOMFactory.wrapDocument(xmlResponse);
        printDocument(domDocument, System.out);
        assertThat(Node.getName(xmlResponse)).isEqualTo(domDocument.getDocumentElement().getNodeName());
    }

    private void printDocument(org.w3c.dom.Document doc, OutputStream out) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.transform(new DOMSource(doc),
                new StreamResult(new OutputStreamWriter(out, StandardCharsets.UTF_8)));
    }

    @Test
    void utilitiesCommandLine() throws CommandLine.ParseException {
        String[] args = {"-ffile1", "-bbatch1", "param1", "param2"};
        //Parse a command line using short options only!
        CommandLine commandLine = new CommandLine(args, "", "-f-b");
        CommandLine.ParseResult parseResult = commandLine.parseResult();

        List<String> parameters = parseResult.getParameters();
        StringBuilder actualParams = new StringBuilder();
        parameters.forEach(actualParams::append);
        assertThat(actualParams.toString()).hasToString("param1param2");


        Map<String, List<String>> allOptions = parseResult.getAllOptions();
        StringBuilder actualOptions = new StringBuilder();
        allOptions.forEach((key, value) -> {
            actualOptions.append("key: ").append(key).append(", ").append("value: ").append(value);
        });
        assertThat(actualOptions).hasToString("key: b, value: [batch1]key: f, value: [file1]");
    }

    @Test
    void utilitiesLinkedList() {
        LinkedList linkedList = new LinkedList();
        linkedList.add("Hello World");
        linkedList.add(Boolean.TRUE);
        linkedList.add(Integer.MAX_VALUE);
        linkedList.elements().asIterator().forEachRemaining(item -> {
            System.out.printf("LinkedItem: %s%n", item);
        });

        Iterator<Object> objectIterator = linkedList.elements().asIterator();
        assertThat(objectIterator.next()).isInstanceOf(String.class);
        assertThat(objectIterator.next()).isInstanceOf(Boolean.class);
        assertThat(objectIterator.next()).isInstanceOf(Integer.class);
    }

    @Test
    void utilitiesQueue() {
        Queue queue = new Queue(3);
        queue.put("Hello World");
        queue.put(Boolean.FALSE);
        queue.put(Integer.MIN_VALUE);
        //queue.put(Integer.MIN_VALUE); will hold till queue.get() is triggered!
        StringBuilder actual = new StringBuilder();
        for (int i = 0; i < queue.getMaxSize(); i++) {
            actual.append(queue.get());
        }
        assertThat(actual.toString()).hasToString("Hello Worldfalse-2147483648");
    }

    @Test
    void utilitiesRandom() {
        Integer[] source = {1, 2, 3, 4};
        Integer[] target = new Integer[source.length];
        Random.swapRandom(source, target);
        Arrays.stream(target).forEach(integer -> System.out.printf("%s ", integer));
        assertThat(target)
                .containsAnyOf(1, 2, 3, 4)
                .doesNotContain(0)
                .doesNotHaveDuplicates()
                .hasSameSizeAs(source);
    }

    @Test
    void utilitiesStringSorter() {
        Vector<String> list = new Vector<>();
        list.add("a");
        list.add("z");
        list.add("c");
        list.add("Z");
        list.add("C");
        list.add("A");
        Enumeration<?> sort = StringSorter.sort(list.elements());
        Iterator<?> iterator = sort.asIterator();

        StringBuilder actual = new StringBuilder();
        iterator.forEachRemaining(item -> {
            actual.append(item).append(" ");
        });
        assertThat(actual.toString().trim()).hasToString("A C Z a c z");
    }

    @Test
    void classInformation() {
        ClassInfo classInfo = new ClassInfo(Node.class);
        assertThat(classInfo.getPersistence()).isEqualTo(ClassInfo.Persistence.UNDEFINED);
    }

    @Test
    void dataConversion() {
        assertThat(DataConverter.String2int("100")).isEqualTo(100);
        assertThat(DataConverter.Object2String(null)).isEmpty();
        assertThat(DataConverter.String2String("dummy")).hasToString("dummy");
        assertThat(DataConverter.String2byte("2")).isEqualTo((byte) 2);
    }

    //=================================
    //The below tests are disabled for a reason; I was not able to contact AWP (on a remote VM) after too much of my time!
    //=================================

    @Test
    @Disabled
    //Run IntelliJ as administrator for Windows registry changes: "Computer\HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\EventLog\Application\CordysLogger defaultInst"
    void getPackageStatus() {
        initSystemProps();

        String protocol = "http";
        String siteName = "appworks_tips";
        int port = 8080;
        DeploymentUtility deploymentUtility = new DeploymentUtilityImpl(OrganizationContext.getOrganizationName(), protocol, siteName, port);
        System.out.println(deploymentUtility.getPackageStatus("??"));
    }

    @Test
    @Disabled
    void callDBConnection() {
        IManagedComponent managedComponent = ManagedComponentFactory.getRootComponent();
        DBConnectionPool connectionPool = DBConnectionPool.getInstance(managedComponent, null, null, DBConnectionPool.LEGACY_FROM_DSO);
        System.out.println(connectionPool.getDriver());
    }

    @Test
    @Disabled
    //Run IntelliJ as administrator for Windows registry changes in first run!
    void initListener() {
        BsfContext context = null;
        try {
            Config wsAppConfig = new Config();
            Document document = new Document();
            //You can also read this from: C:\CORDYS_22_4\components\wsappserver\config\wsapps.properties
            wsAppConfig.setConfig(document.load("<configurations><configuration><initializeDB>false</initializeDB><bsf><property></property></bsf></configuration></configurations>".getBytes()));
            context = BSF.initBsfContext();

            BusObject busObject = new AnonymousBusObject(BusObjectConfig.NEW);
            context.getObjectManager().insertObject(busObject);
            busObject.setAttribute("propName", "attrName1", "attrValue1");
        } catch (XMLException e) {
            System.out.println(e.getMessage());
        } finally {
            if(context != null) {
                BSF.unregisterContext(context);
            }
        }
    }

    @Test
    @Disabled
    void soapRequestObject() {
        initSystemProps();

        String namespace = "";
        String methodName = "";
        String[] paramNames = {""};
        Object[] paramValues = {null};

        SOAPRequestObject soapRequestObject = new SOAPRequestObject(namespace, methodName, paramNames, paramValues);
        int response = soapRequestObject.execute();
        System.out.println(Node.getName(response));
    }

    private void initSystemProps() {
        String domain = "22.3.com";
        String host = "192.179.56.110";

        //Required property settings to call AWP remotely
        System.setProperty("cordys.instance.name", "defaultInst");
        System.setProperty("ldap.root", String.format("cn=cordys,cn=defaultInst,o=%s", domain));
        System.setProperty("bus.ldap.processor.host", String.format("${ldap.host:-%s}", host));
        System.setProperty("bus.ldap.processor.port", "${ldap.port:-6366}");
        System.setProperty("bus.ldap.processor.user", String.format("cn=Directory Manager,o=${ldap.domain:-%s}", domain));
        System.setProperty("awp.node.name", host);
    }
}
