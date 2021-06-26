package org.udg.pds.springtodo;

import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.udg.pds.springtodo.entity.IdObject;
import org.udg.pds.springtodo.entity.Tag;
import org.udg.pds.springtodo.entity.User;
import org.udg.pds.springtodo.service.ProductService;
import org.udg.pds.springtodo.service.TagService;
import org.udg.pds.springtodo.service.UserService;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Service
public class Global {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private MinioClient minioClient;

    private Logger logger = LoggerFactory.getLogger(Global.class);

    @Autowired
    private
    UserService userService;

    @Autowired
    private
    ProductService productService;

    @Autowired
    private
    TagService tagService;

    @Value("${todospring.minio.url:}")
    private String minioURL;

    @Value("${todospring.minio.access-key:}")
    private String minioAccessKey;

    @Value("${todospring.minio.secret-key:}")
    private String minioSecretKey;

    @Value("${todospring.minio.bucket:}")
    private String minioBucket;

    @Value("${todospring.base-url:#{null}}")
    private String BASE_URL;

    @Value("${todospring.base-port:8080}")
    private String BASE_PORT;


    @PostConstruct
    void init() {

        logger.info(String.format("Starting Minio connection to URL: %s", minioURL));
        try {
            minioClient = new MinioClient(minioURL, minioAccessKey, minioSecretKey);
        } catch (Exception e) {
            logger.warn("Cannot initialize minio service with url:" + minioURL + ", access-key:" + minioAccessKey + ", secret-key:" + minioSecretKey);
        }

        if (minioBucket == "") {
            logger.warn("Cannot initialize minio bucket: " + minioBucket);
            minioClient = null;
        }

        if (BASE_URL == null) BASE_URL = "http://localhost";
        BASE_URL += ":" + BASE_PORT;

        initData();
    }

    private void initData() {
        logger.info("Starting populating database ...");
        User user = userService.register("usuari", "Nacho","usuari@hotmail.com", "123456",
            "Girona", 972303030L, new Date(), false);

        productService.addProduct(user.getId(), "Iphone 11", "Iphone 11 en buen estado", 1100.0, new Date(), (byte) 1, new Date(), "Girona",new ArrayList<String>());
        productService.addProduct(user.getId(), "Samsung Galaxy S20", "Samsung Galaxy S20 en buen estado", 1000.0, new Date(), (byte) 0, new Date(), "Girona", new ArrayList<String>());
        productService.addProduct(user.getId(), "Huawei P40", "Huawei P40 en buen estado", 300.0, new Date(), (byte) 0, new Date(), "Girona" , new ArrayList<String>());
        productService.addProduct(user.getId(), "Xiaomi Mi 10", "Xiaomi Mi 10 en buen estado", 800.0, new Date(), (byte) 0, new Date(), "Girona" , new ArrayList<String>());


        user = userService.register("manolo", "Manolo","manolo@hotmail.com", "123456",
            "Tarragona", 972404040L, new Date(), false);

        productService.addProduct(user.getId(), "Xbox Series X", "Xbox Series X sin estrenar", 800.0, new Date(), (byte) 1, new Date(), "Tarragona" , new ArrayList<String>());
        productService.addProduct(user.getId(), "Play Station 5", "Play Station 5 sin estrenar", 600.0, new Date(), (byte) 0, new Date(), "Tarragona" , new ArrayList<String>());
        productService.addProduct(user.getId(), "Nintendo Switch", "Nintendo Switch en buen estado", 300.0, new Date(), (byte) 0, new Date(), "Tarragona", new ArrayList<String>());
        productService.addProduct(user.getId(), "Play Station 1", "Play Station 1 edicion coleccionista", 2000.0, new Date(), (byte) 0, new Date(), "Tarragona", new ArrayList<String>());

        user = userService.register("maricarmen", "Mari Carmen","maricarmen@hotmail.com", "123456",
            "Barcelona", 972505050L, new Date(), false);

        productService.addProduct(user.getId(), "XMSI GT75 Titan 9SG-285ES", "MSI GT75 Titan 9SG-285ES Intel Core i9-9980HK/64GB/2TB SSD/RTX 2080/17.3\"",
            4798.99, new Date(), (byte) 1, new Date(), "Tarragona", new ArrayList<String>());
        productService.addProduct(user.getId(), "MSI WT75 8SM-011ES", "MSI WT75 8SM-011ES Intel Core i7-8700/32GB/1TB+512GB SSD/P5200/17.3\" 4K",
            4798.99, new Date(), (byte) 1, new Date(), "Tarragona", new ArrayList<String>());
        productService.addProduct(user.getId(), "Gigabyte AERO 17 HDR YB-9ES4750SP", "Gigabyte AERO 17 HDR YB-9ES4750SP Intel Core i9-10980HK/64GB/1TB SSD/RTX 2080 SUPER/17.3\"",
            4699.0, new Date(), (byte) 1, new Date(), "Tarragona", new ArrayList<String>());
        productService.addProduct(user.getId(), "MSI GT76 Titan DT 9SGS-263ES", "MSI GT76 Titan DT 9SGS-263ES Intel Core i9-9900K/64GB/2TB SSD/RTX 2080 SUPER/17.3\"",
            4699.0, new Date(), (byte) 1, new Date(), "Tarragona", new ArrayList<String>());


        user = userService.register("lluc", "Lluc Pagès","lluc@hotmail.com", "lluc",
            "Girona", 601232323L, new Date(), false);
        user = userService.register("xavi", "Xavi","xavi@hotmail.com", "xavi",
            "Barcelona", 972300506L, new Date(), false);
        user = userService.register("iuri", "Iuri","iuri@hotmail.com", "Iuri",
            "Lleida", 972505057L, new Date(), false);
        user = userService.register("carles", "Carles","carles@hotmail.com", "carles",
            "Barcelona", 972505057L, new Date(), false);
        user = userService.register("alex", "Alex","alex@hotmail.com", "alex",
            "Tarragona", 972505057L, new Date(), false);

        //Rates
        userService.addRate("Una bona compra, molt bon tracte", new Date(), 4.0f, 1L, 2L);
        userService.addRate("Podria tenir millor qualitat, pero venedor molt bon tracte", new Date(), 4.0f, 1L, 3L);
        userService.addRate("No gaire content, impuntual i impresentable", new Date(), 1.5f, 1L, 4L);
        userService.addRate("Not bad", new Date(), 4.0f, 1L, 5L);
        userService.addRate("Good, everything fine", new Date(), 5.0f, 3L, 4L);
        userService.addRate("Nothing bad", new Date(), 4.0f, 2L, 1L);
        userService.addRate("Good, everything good", new Date(), 5.0f, 3L, 4L);
        userService.addRate("Not bad at all", new Date(), 4.0f, 3L, 6L);
    }

    public MinioClient getMinioClient() {
        return minioClient;
    }

    public String getMinioBucket() {
        return minioBucket;
    }

    public String getBaseURL() {
        return BASE_URL;
    }
}
