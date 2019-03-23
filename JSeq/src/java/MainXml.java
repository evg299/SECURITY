/**
 * Created by Evgeny(e299792459@gmail.com) on 13.02.14.
 */

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import ru.evg299.jseq.xmlseq.cryptopro.XmlDeffender;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;

public class MainXml {
    private static String signedFile = "c:/sar-from-b64-sss.xml";
    private static String purgeFile = "c:/sar-from-b64.xml";

    private static String containerName = "CntLok";
    private static String containerPass = "123123";


    public static void main(String[] args) throws Exception {
        sign();
        // ver();
    }

    public static void ver() throws Exception {
        Document signedXmlDocument = readDocumentFromFile(signedFile);
        System.out.println(XmlDeffender.verDocument(signedXmlDocument));
    }

    public static void sign() throws Exception {
        Document xmlDocument = readDocumentFromFile(purgeFile);
        XmlDeffender.signDocument(xmlDocument, containerName, containerPass);
        saveToFile(xmlDocument, signedFile);
    }

    public static void saveToFile(Node node, String fn) throws Exception {
		/* Сохранение подписанного XML-документа в файл */

        // определение потока, в который осуществляется запись подписанного
        // XML-документа
        final FileOutputStream os = new FileOutputStream(fn);
        // инициализация объекта копирования содержимого XML-документа в поток
        final TransformerFactory tf = TransformerFactory.newInstance();
        // создание объекта копирования содержимого XML-документа в поток
        final Transformer trans = tf.newTransformer();
        // копирование содержимого XML-документа в поток
        trans.transform(new DOMSource(node), new StreamResult(os));
        os.close();
    }

    public static Document readDocumentFromFile(String fn) throws Exception {
        // инициализация объекта чтения XML-документа
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        // установка флага, определяющего игнорирование пробелов в содержимом
        // элементов при обработке XML-документа
        dbf.setIgnoringElementContentWhitespace(true);

        // установка флага, определяющего преобразование узлов CDATA в текстовые
        // узлы при обработке XML-документа
        dbf.setCoalescing(true);

        // установка флага, определяющего поддержку пространств имен при
        // обработке XML-документа
        dbf.setNamespaceAware(true);

        // загрузка содержимого подписываемого документа на основе установленных
        // флагами правил
        final DocumentBuilder documentBuilder = dbf.newDocumentBuilder();

		/* Загружаем подписанный XML-документ из файла */
        return documentBuilder.parse(new File(fn));
    }
}
